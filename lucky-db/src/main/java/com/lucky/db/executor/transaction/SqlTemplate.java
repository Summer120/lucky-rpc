package com.lucky.db.executor.transaction;

import com.lucky.db.executor.*;
import com.lucky.db.executor.context.*;
import lucky.util.log.Logger;
import lucky.util.log.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 17:28 2017/7/4
 */
public abstract class SqlTemplate implements ConnectionManager, Executor {

    private static Logger logger = LoggerFactory.getLogger(SqlTemplate.class);
    public DataSource dataSource;

    public SqlTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //插入语句信息
    @Override
    public InsertClause insert(Object obj) {
        return new InsertClauseProvider(this, obj);
    }

    @Override
    public InsertContext insert(String table) {
        return new InsertProvider(table, this);
    }

    @Override
    public InsertClause insert(List<Object> objs) {
        return new InsertClauseProvider(this, objs);
    }


    //删除语句信息
    @Override
    public DeleteContext delete(String table) {

        return new DeleteProvider(table, this);
    }

    @Override
    public DeleteClauseProvider delete(Object obj) {
        return new DeleteClauseProvider(this, obj);
    }


    //更新语句操作
    @Override
    public UpdateContext update(String table) {
        return new UpdateProvider(table, this);
    }

    @Override
    public UpdateClause update(Object obj) {

        return new UpdateClauseProvider(this, obj);
    }

    @Override
    public UpdateClause update(Object obj, String... columns) {

        return new UpdateClauseProvider(this, obj, columns);
    }


    //查询语句操作,这块封装的不是太好，有点太死了
    @Override
    public SelectContext select(String column) {
        return new SelectProvider(column, this);
    }

    @Override
    public SelectContext select(String... columns) {
        return new SelectProvider(this, columns);
    }


    //内部还可以调用SelectContext，就是为了字段名信息
    @Override
    public SelectClause select(Class<?> clazz) {
        return new SelectClauseProvider(clazz, this);
    }


    //事务操作的方法


    @Override
    public ExecuteClause execute(String sql, Object... args) {
        return null;
    }

}
