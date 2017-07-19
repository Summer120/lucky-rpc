package com.lucky.db.executor.result;

import com.lucky.db.exception.DataSourceException;
import com.lucky.db.executor.DbUtil;
import com.lucky.db.executor.mapper.EntityInfo;
import com.lucky.db.executor.mapper.Mapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:chaoqiang.zhou
 * @Description:查询返回的结果集信息 在JDK7中只要实现了AutoCloseable或Closeable接口的类或接口，都可以使用try-with-resource来实现异常处理和资源关闭
 * @Date:Create in 9:24 2017/6/27
 */
public class SelectResult implements AutoCloseable {


    //通过resultset来操作下面的信息
    private ResultSet resultset;
    private Statement statement;
    private Connection connection;

    public SelectResult(ResultSet set) {
        this.resultset = set;
    }

    public SelectResult(ResultSet resultset, Statement statement, Connection connection) {
        this.resultset = resultset;
        this.statement = statement;
        this.connection = connection;
    }

    //通过result来进行返回值即可
    //返回某个类操作
    public <T> T one(Class<T> clazz) {
        try {
            if (resultset.next()) {
                EntityInfo entityInfo = Mapper.getEntityInfo(clazz);
                T obj = clazz.newInstance();
                ResultSetMetaData metaData = resultset.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    Object value = resultset.getObject(i);
                    if (value != null) {
                        Object entity = DbUtil.getObject(resultset, i, value);
                        entityInfo.setValue(obj, metaData.getColumnLabel(i), entity);
                    }
                }

                return obj;
            }

            return null;
        } catch (Exception e) {
            throw new DataSourceException(e);
        } finally {
            this.close();
        }

    }


    //返回集合操作
    public <T> List<T> all(Class<T> clazz) {
        try {
            List<T> list = new ArrayList<>();
            ResultSetMetaData metaData = resultset.getMetaData();
            EntityInfo info = Mapper.getEntityInfo(clazz);
            while (resultset.next()) {
                T obj = clazz.newInstance();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    //jdbctype---to--javatype操作
                    Object value = resultset.getObject(i);
                    info.setValue(obj, metaData.getColumnLabel(i), value);
                }
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            throw new DataSourceException(e);
        } finally {
            this.close();
        }
    }


    //读取记录得第一行操作
    public Map<String, Object> one() {

        try {
            if (resultset.next()) {
                Map<String, Object> objMap = new HashMap<>();
                ResultSetMetaData metaData = resultset.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    objMap.put(metaData.getColumnLabel(i), resultset.getObject(i));
                }
                return objMap;
            }
            return null;
        } catch (SQLException e) {
            throw new DataSourceException(e);
        } finally {
            this.close();
        }
    }

    @Override
    public void close() {

        //自动关闭连接操作
        DbUtil.release(resultset);
        DbUtil.release(statement);
        DbUtil.release(connection);
        resultset = null;
        statement = null;
        connection = null;
    }
}
