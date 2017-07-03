package com.lucky.db.executor.transaction;

import com.lucky.db.executor.Context;
import com.lucky.db.executor.DataBase;
import com.lucky.db.executor.Executor;
import lucky.util.log.Logger;
import lucky.util.log.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Author:chaoqiang.zhou 参考：http://blog.csdn.net/zq9017197/article/details/6321391
 * @Description:参照这个类来实现org.springframework.transaction.support.TransactionTemplate;
 * @Date:Create in 14:56 2017/6/29
 */
public class TransactionTemplate extends DefaultTransactionDefinition implements InitializingBean {

    public static final Logger logger = LoggerFactory.getLogger(TransactionTemplate.class);
    //所有的Spring使用事务管理器，每个不同平台的事务管理器都实现了接口：PlatformTransactionManager
    private PlatformTransactionManager transactionManager;
    private DataSource dataSource;

    public TransactionTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
        this.transactionManager = new DataSourceTransactionManager(dataSource);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.transactionManager == null) {
            throw new IllegalArgumentException("Property 'transactionManager' is required");
        }
    }


    public <T> T execute(Function<Executor, T> action, boolean setContext) throws TransactionException {
        TransactionStatus status = this.transactionManager.getTransaction(this);
        T result = null;
        Executor executor = null;
        try {

            if (setContext) {
                executor = Context.CURRENT.get();
            } else {
                executor = new DataBase(this.dataSource);

            }
            result = action.apply(executor);
        } catch (RuntimeException ex) {
            // Transactional code threw application exception -> rollback
            rollbackOnException(status, ex);
            throw ex;
        } catch (Error err) {
            // Transactional code threw error -> rollback
            rollbackOnException(status, err);
            throw err;
        }
        this.transactionManager.commit(status);
        return result;

    }


    public void execute(Consumer<Executor> action, boolean setContext) throws TransactionException {
        TransactionStatus status = this.transactionManager.getTransaction(this);
        Executor executor = null;
        try {
            if (setContext) {
                executor = Context.CURRENT.get();
            } else {
                executor = new DataBase(this.dataSource);

            }
            action.accept(executor);
        } catch (RuntimeException ex) {
            // Transactional code threw application exception -> rollback
            rollbackOnException(status, ex);
            throw ex;
        } catch (Error err) {
            // Transactional code threw error -> rollback
            rollbackOnException(status, err);
            throw err;
        }
        this.transactionManager.commit(status);
    }

    private void rollbackOnException(TransactionStatus status, Throwable ex) throws TransactionException {
        this.logger.debug("Initiating transaction rollback on application exception", ex);

        try {
            this.transactionManager.rollback(status);
        } catch (TransactionSystemException var4) {
            this.logger.error("Application exception overridden by rollback exception", ex);
            var4.initApplicationException(ex);
            throw var4;
        } catch (RuntimeException var5) {
            this.logger.error("Application exception overridden by rollback exception", ex);
            throw var5;
        } catch (Error var6) {
            this.logger.error("Application exception overridden by rollback error", ex);
            throw var6;
        }
    }

}
