package com.lucky.db.executor;

import com.lucky.db.datasource.DataSourceFactory;
import com.lucky.db.executor.transaction.DataBaseImpl;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author:chaoqiang.zhou
 * @Description:Executor的工厂类
 * @Date:Create in 9:51 2017/6/27
 */
public class DataBaseFactory {

    public static final Map<String, DataBase> executorCache = new ConcurrentHashMap<>();

    /**
     * 返回最底层的executor对象信息
     *
     * @param dbName
     * @return
     */
    public static DataBase open(String dbName) {
        DataBase executor = executorCache.get(dbName);
        if (executor == null) {
            DataSource dataSource = DataSourceFactory.get(dbName);
            executor = new DataBaseImpl(dataSource);
            executorCache.putIfAbsent(dbName, executor);
        }
        return executor;
    }


    /**
     * @param dbName:dbname的信息
     * @param key
     * @return
     */
    public Executor openReadShard(String dbName, Object key) {

        return null;
    }


    public Executor openWriteShard(String dbName, String key) {

        return null;
    }
}
