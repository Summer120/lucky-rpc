package com.lucky.db.executor.transaction;

import com.lucky.db.executor.Executor;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Author:chaoqiang.zhou
 * @Description:事务操作的执行器，单独抽离出来 参照java1.8中得lambda十字
 * @Date:Create in 15:25 2017/6/29
 */
public interface TransactionExecutor extends Executor {


    /**
     * 启动一个事务
     *
     * @return
     */
    Executor begin();

    /**
     * 启动一个事务
     *
     * @param action 事务操作
     * @return
     */
    void begin(Consumer<Executor> action);

    /**
     * 启动一个事务
     *
     * @param func 事务操作
     * @return 操作结果
     */
    <T> T begin(Function<Executor, T> func);


}
