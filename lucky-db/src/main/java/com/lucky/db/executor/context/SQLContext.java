package com.lucky.db.executor.context;

import com.lucky.db.executor.result.BuildResult;

import javax.sql.DataSource;

/**
 * @Author:chaoqiang.zhou
 * @Description:最抽象的接口信息
 * @Date:Create in 13:04 2017/6/26
 */
public interface SQLContext {

    BuildResult print();

    <T> T result();

    /**
     * 获取插入后，自增的主键操作
     * @param returnKeys
     * @param <T>
     * @return
     */
    <T> T result(Boolean returnKeys);
}
