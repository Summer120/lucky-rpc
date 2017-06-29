package com.lucky.db.executor;

import com.lucky.db.executor.context.InsertClause;
import com.lucky.db.executor.mapper.EntityInfo;
import com.lucky.db.executor.mapper.Mapper;
import com.lucky.db.executor.result.BuildResult;
import com.lucky.db.executor.result.InsertResult;
import com.lucky.db.sqlbuilder.DbBuilder;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author:chaoqiang.zhou
 * @Description:插入语句操作，dataSource可以后期抽离出来，提升一下，有一个专门管理jdbc容器的manager
 * @Date:Create in 13:40 2017/6/26
 */
public class InsertClauseProvider implements InsertClause {

    private List<Object> objects = new ArrayList<>();
    public DataSource dataSource;

    public InsertClauseProvider(DataSource dataSource, List<Object> objs) {
        this.dataSource = dataSource;
        this.objects = objs;
    }

    public InsertClauseProvider(DataSource dataSource, Object obj) {
        this.dataSource = dataSource;
        this.objects.add(obj);
    }

    @Override
    public BuildResult print() {
        //非null的校验
        Objects.requireNonNull(objects);
        //获取entity实体
        //entity包含了每个table的的信息：插入字段、更新字段、删除字段信息，懒加载
        EntityInfo entityInfo = Mapper.getEntityInfo(objects.get(0).getClass());
        return new DbBuilder().insertBuilder(entityInfo, objects);
    }

    @Override
    public InsertResult result(Boolean returnKeys) {
        BuildResult buildResult = print();
        return DbUtil.executeUpdate(dataSource, buildResult.getSql(), buildResult.getArgs(), returnKeys);
    }


    //具体的业务逻辑实现操作,单行用values，多行用value比较快
    //单个obj insert into tablename('field','field') values()
    //多个实体 insert into tableName('field','field') value(),value()
    @Override
    public InsertResult result() {
        return this.result(false);
    }


}
