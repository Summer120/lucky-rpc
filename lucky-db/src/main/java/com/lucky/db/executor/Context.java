package com.lucky.db.executor;

/**
 * @Author:chaoqiang.zhou
 * @Description:共享的上下文的信息
 * @Date:Create in 10:30 2017/7/3
 */
public class Context {

    //共享的db信息，用于实现事务的上下文的信息
    public static final ThreadLocal<Executor> CURRENT = new ThreadLocal<>();


}


