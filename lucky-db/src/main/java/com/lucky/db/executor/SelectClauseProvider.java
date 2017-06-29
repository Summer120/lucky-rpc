package com.lucky.db.executor;

import com.lucky.db.executor.context.SelectClause;
import com.lucky.db.executor.result.BuildResult;

import javax.sql.DataSource;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 10:50 2017/6/27
 */
public class SelectClauseProvider implements SelectClause {


    public Class<?> clazz;
    public DataSource dataSource;

    public SelectClauseProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SelectClauseProvider(Class<?> clazz, DataSource dataSource) {
        this.clazz = clazz;
        this.dataSource = dataSource;
    }

    @Override
    public BuildResult print() {
        return null;
    }

    @Override
    public <T> T result() {
        return null;
    }

    @Override
    public <T> T result(Boolean returnKeys) {
        return null;
    }
}
