package com.lucky.db.executor.transaction;

import com.lucky.db.exception.DataSourceException;
import com.lucky.db.executor.Transaction;
import lucky.util.log.Logger;
import lucky.util.log.LoggerFactory;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 10:59 2017/7/7
 */
public class TransactionImpl extends SqlTemplate implements Transaction, AutoCloseable {


    private static Logger logger = LoggerFactory.getLogger(TransactionImpl.class);
    //缓存每个线程的当前的上下文对象操作
    public static ThreadLocal<Transaction> current = new ThreadLocal<>();
    private Connection connection;
    private boolean completed;

    private DataSource dataSource;

    public TransactionImpl(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public void rollback() {
        try {
            this.completed = true;
            if (this.connection != null) {
                this.connection.rollback();
                this.connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    @Override
    public void commit() {
        try {
            this.completed = true;
            this.connection.commit();
            this.connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    @Override
    public void close() {
        // 如果事务没有提交或回滚则先回滚
        if (!completed) {
            try {
                this.rollback();
            } catch (Exception e) {
                logger.error("rollback failed", e);
            }
        }

        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                throw new DataSourceException(e);
            } finally {
                this.connection = null;
            }
        }
    }

    @Override
    public Connection getConnection() {
        try {
            if (this.connection == null) {
                this.connection = dataSource.getConnection();
                this.connection.setAutoCommit(false);
            }
            return this.connection;
        } catch (SQLException e) {
            if (dataSource instanceof BasicDataSource) {
                BasicDataSource ds = (BasicDataSource) dataSource;
                logger.debug("db pool > max:{}, active:{}, idle:{}", ds.getMaxTotal(), ds.getNumActive(), ds.getNumIdle());
            }
            throw new DataSourceException(e);
        }
    }
}
