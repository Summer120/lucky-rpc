package com.lucky.db.executor;

import com.lucky.db.executor.context.SelectClause;
import com.lucky.db.executor.mapper.EntityInfo;
import com.lucky.db.executor.mapper.Mapper;
import com.lucky.db.executor.result.BuildResult;
import com.lucky.db.executor.result.SelectResult;
import com.lucky.db.sqlbuilder.DbBuilder;
import com.lucky.db.sqlbuilder.SQL;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 10:50 2017/6/27
 */
public class SelectClauseProvider implements SelectClause {


    public Class<?> clazz;
    public ConnectionManager connectionManager;
    private SQL sqlBuilder = new SQL();
    private List<Object> args = new ArrayList<>();

    public SelectClauseProvider(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public SelectClauseProvider(Class<?> clazz, ConnectionManager connectionManager) {
        this.clazz = clazz;

        this.connectionManager = connectionManager;
    }

    @Override
    public BuildResult print() {
        EntityInfo entityInfo = Mapper.getEntityInfo(clazz);
        return new DbBuilder().selectBuilder(entityInfo, sqlBuilder, args);
    }

    @Override
    public SelectResult result() {
        return this.result(LockMode.NONE);
    }

    @Override
    public SelectClauseProvider JOIN(String join) {
        this.sqlBuilder.JOIN(join);
        return this;
    }

    @Override
    public SelectClauseProvider INNER_JOIN(String join) {
        this.sqlBuilder.INNER_JOIN(join);
        return this;
    }

    @Override
    public SelectClauseProvider LEFT_OUTER_JOIN(String join) {
        this.sqlBuilder.LEFT_OUTER_JOIN(join);
        return this;
    }

    @Override
    public SelectClauseProvider RIGHT_OUTER_JOIN(String join) {
        this.sqlBuilder.RIGHT_OUTER_JOIN(join);
        return this;

    }

    @Override
    public SelectClauseProvider OUTER_JOIN(String join) {
        this.sqlBuilder.OUTER_JOIN(join);
        return this;
    }

    //只有where的时候才会有参数信息
    @Override
    public SelectClauseProvider WHERE(String conditions, ConditionType type, Object value) {

        this.sqlBuilder.WHERE(conditions + "" + type.value + " ?");
        this.args.add(value);
        return this;
    }

    @Override
    public SelectClauseProvider OR() {
        this.sqlBuilder.OR();
        return this;
    }

    @Override
    public SelectClauseProvider AND() {
        this.sqlBuilder.AND();
        return this;
    }

    @Override
    public SelectClauseProvider GROUP_BY(String... columns) {
        for (String column : columns) {
            this.sqlBuilder.GROUP_BY(column);
        }
        return this;
    }

    @Override
    public SelectClauseProvider HAVING(String conditions) {
        this.sqlBuilder.HAVING(conditions);
        return this;
    }

    @Override
    public SelectClauseProvider ORDER_BY(String columns, Sorter sorter) {
        this.sqlBuilder.ORDER_BY(columns + sorter.name());
        return this;
    }

    @Override
    public SelectClauseProvider ORDER_BY(String columns) {
        this.sqlBuilder.ORDER_BY(columns);
        return this;
    }

    @Override
    public SelectClauseProvider LIMIT(String... columns) {
        this.sqlBuilder.LIMIT(columns);
        return this;
    }

    @Override
    public SelectResult result(LockMode lockMode) {
        BuildResult buildResult = print();
        if (lockMode == LockMode.SHARED) {
            buildResult.setSql(buildResult.getSql() + " lock in share mode");
        } else if (lockMode == LockMode.EXCLUSIVE) {
            buildResult.setSql(buildResult.getSql() + " for update");
        }
        return DbUtil.executeQuery(this.connectionManager, buildResult.getSql(), buildResult.getArgs());
    }
}
