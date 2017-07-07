package com.lucky.db.executor;

import com.lucky.db.executor.transaction.TransactionImpl;

/**
 * @Author:chaoqiang.zhou
 * @Description:实现了AutoCloseAble接口，到时候直接trycatche捕捉，自动关闭连接操作
 * @Date:Create in 10:53 2017/7/7
 */
public interface Transaction extends Executor, AutoCloseable {
    /**
     * 获取当前上下文中的事务对象
     *
     * @return
     */
    static Transaction get() {
        return TransactionImpl.current.get();
    }

    /**
     * 回滚事务
     */
    void rollback();

    /**
     * 提交事务
     */
    void commit();

    /**
     * 关闭事务, 如果当前有未关闭的事务则自动回滚
     */
    @Override
    void close();
}
