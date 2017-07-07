package com.lucky.db.executor;

import com.lucky.db.executor.context.*;

import java.util.List;

/**
 * @Author:chaoqiang.zhou
 * @Description:仿照mybatis，详情见mybatis中得executor核心执行器接口
 * @Date:Create in 10:46 2017/6/26
 */
public interface Executor {


    InsertClause insert(Object obj);

    //insert(table).columns().values().result()
    InsertContext insert(String table);

    InsertClause insert(List<Object> objects);


    //删除语句操作,delete table where......
    DeleteContext delete(String table);

    DeleteClause delete(Object obj);


    UpdateContext update(String table);

    UpdateClause update(Object obj);

    UpdateClause update(Object obj, String... columns);


    //查询操作
    SelectContext select(String column);

    SelectContext select(String... columns);

    SelectClause select(Class<?> clazz);


    //事务操作的方法


    //原生sql查询操作
    ExecuteClause execute(String sql, Object... args);


}
