package com.lucky.db.executor.context;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 12:49 2017/6/26
 */
public interface InsertClause extends SQLContext {
    /**
     * 获取插入后，自增的主键操作
     *
     * @param returnKeys
     * @param <T>
     * @return
     */
    <T> T result(Boolean returnKeys);
}
