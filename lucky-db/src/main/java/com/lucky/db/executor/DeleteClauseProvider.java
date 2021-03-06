package com.lucky.db.executor;

import com.lucky.db.executor.context.DeleteClause;
import com.lucky.db.executor.mapper.EntityInfo;
import com.lucky.db.executor.mapper.Mapper;
import com.lucky.db.executor.result.BasicResult;
import com.lucky.db.executor.result.BuildResult;
import com.lucky.db.sqlbuilder.DbBuilder;

import java.util.Objects;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 17:15 2017/6/26
 */
public class DeleteClauseProvider implements DeleteClause {


    public ConnectionManager connectionManager;
    private Object obj;

    public DeleteClauseProvider(ConnectionManager connectionManager, Object obj) {
        this.connectionManager = connectionManager;
        this.obj = obj;
    }

    @Override
    public BuildResult print() {
        //参数校验
        Objects.requireNonNull(obj);
        //获取mapper实体操作
        EntityInfo entityInfo = Mapper.getEntityInfo(obj.getClass());
        return new DbBuilder().deleteBuilder(entityInfo, obj);
    }

    @Override
    public BasicResult result() {
        BuildResult buildResult = print();
        return DbUtil.executeUpdate(this.connectionManager, buildResult.getSql(), buildResult.getArgs());
    }


}
