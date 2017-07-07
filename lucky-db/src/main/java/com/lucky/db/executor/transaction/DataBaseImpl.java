package com.lucky.db.executor.transaction;

import com.lucky.db.exception.DataSourceException;
import com.lucky.db.executor.DataBase;
import com.lucky.db.executor.Transaction;
import lucky.util.log.Logger;
import lucky.util.log.LoggerFactory;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Author:chaoqiang.zhou
 * @Description:同时有了所有得方法,后期考虑datasource如何缓存起来 一定要保证用的是一个数据库得连接操作
 * @Date:Create in 15:31 2017/6/29
 */
public class DataBaseImpl extends SqlTemplate implements DataBase {
    public static Logger logger = LoggerFactory.getLogger(DataBaseImpl.class);
    public DataSource dataSource;
    public Connection connection;
    public boolean completed;

    public DataBaseImpl(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public Transaction begin() {
        return new TransactionImpl(dataSource);
    }


    //使用java8中得lambda操作，spring中使用的是接口
    @Override
    public void begin(Consumer<Transaction> action) {
        this.begin(action, false);
    }

    @Override
    public <T> T begin(Function<Transaction, T> func) {
        return this.begin(func, false);
    }

    @Override
    public <T> T begin(Function<Transaction, T> func, boolean setContext) {
        Transaction t = new TransactionImpl(dataSource);

        if (setContext) {
            TransactionImpl.current.set(t);
        }

        try {
            T result = func.apply(t);
            t.commit();
            return result;
        } catch (DataSourceException e) {
            t.rollback();
            return null;
        } catch (Exception e) {
            t.rollback();
            throw e;
        } finally {
            t.close();
            if (setContext) {
                TransactionImpl.current.remove();
            }
        }
    }

    @Override
    public void begin(Consumer<Transaction> action, boolean setContext) {
        Transaction t = new TransactionImpl(dataSource);

        if (setContext) {
            TransactionImpl.current.set(t);
        }

        try {
            action.accept(t);
            t.commit();
        } catch (DataSourceException e) {
            t.rollback();
        } catch (Exception e) {
            t.rollback();
            throw e;
        } finally {
            t.close();
            if (setContext) {
                TransactionImpl.current.remove();
            }
        }
    }


    /***
     * 普通的获取链接操作，自动提交
     * @return
     */
    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            if (dataSource instanceof BasicDataSource) {
                BasicDataSource ds = (BasicDataSource) dataSource;
                logger.debug("db pool > max:{}, active:{}, idle:{}", ds.getMaxTotal(), ds.getNumActive(), ds.getNumIdle());
            }
            throw new DataSourceException(e);
        }
    }
}
