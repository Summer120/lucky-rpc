package com.lucky.db.executor;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 10:48 2017/7/7
 */
public interface DataBase extends Executor {
    /**
     * 启动一个事务
     *
     * @return
     */
    Transaction begin();

    /**
     * 启动一个事务
     *
     * @param action 事务操作
     * @return
     */
    void begin(Consumer<Transaction> action);

    /**
     * 启动一个事务
     *
     * @param func 事务操作
     * @return 操作结果
     */
    <T> T begin(Function<Transaction, T> func);

    /**
     * 启动一个事务
     *
     * @param func 事务操作
     * @return 操作结果
     */
    <T> T begin(Function<Transaction, T> func, boolean setContext);

    /**
     * 启动一个事务
     *
     * @param action     事务操作
     * @param setContext 是否设置上下文事务
     * @return
     */
    void begin(Consumer<Transaction> action, boolean setContext);
}
