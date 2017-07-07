package com.lucky.db.executor;

import com.lucky.db.executor.context.UpdateClause;
import com.lucky.db.executor.mapper.EntityInfo;
import com.lucky.db.executor.mapper.Mapper;
import com.lucky.db.executor.result.BasicResult;
import com.lucky.db.executor.result.BuildResult;
import com.lucky.db.sqlbuilder.DbBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 17:51 2017/6/26
 */
public class UpdateClauseProvider implements UpdateClause {


    private ConnectionManager connectionManager;

    //代表需要更新的字段信息
    private List<String> updateColumns = new ArrayList<>();
    private Object object;

    public UpdateClauseProvider(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    public UpdateClauseProvider(ConnectionManager connectionManager, Object obj) {
        this.connectionManager = connectionManager;
        this.object = obj;
    }

    public UpdateClauseProvider(ConnectionManager connectionManager, Object obj, String... columns) {
        this.connectionManager = connectionManager;
        this.updateColumns = Arrays.asList(columns);
        this.object = obj;
    }

    @Override
    public BuildResult print() {
        Objects.requireNonNull(object);
        EntityInfo entityInfo = Mapper.getEntityInfo(object.getClass());
        if (updateColumns.size() <= 0) {
            //为空的话，就是用的实体里面所有的字段信息
            return new DbBuilder().updateBuilder(entityInfo, object, Arrays.asList(entityInfo.getUpdateColumns()));
        }

        //否则就是用的是更新后的信息
        return new DbBuilder().updateBuilder(entityInfo, object, updateColumns);
    }


    @Override
    public BasicResult result() {
        BuildResult buildResult = print();
        return DbUtil.executeUpdate(this.connectionManager, buildResult.getSql(), buildResult.getArgs());

    }
}
