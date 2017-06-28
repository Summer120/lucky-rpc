package com.lucky.db.executor;

import com.lucky.db.debug.DebugLogger;
import com.lucky.db.exception.DataSourceException;
import com.lucky.db.executor.context.InsertClause;
import com.lucky.db.executor.mapper.EntityInfo;
import com.lucky.db.executor.mapper.Mapper;
import com.lucky.db.executor.result.BuildResult;
import com.lucky.db.executor.result.InsertResult;
import com.lucky.db.sqlbuilder.DbBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet rs = null;
        BuildResult buildResult = null;

        try {
            connection = dataSource.getConnection();
            buildResult = print();
            int option = returnKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
            statement = connection.prepareStatement(buildResult.getSql(), option);
            List<Object> args = buildResult.getArgs();
            //在这里做javatype---》to---》jdbctype类型转换操作
            DbUtil.setParameters(statement, args);
            int rows = statement.executeUpdate();
            List<Object> keys = null;
            if (rows > 0 && returnKeys) {
                keys = new ArrayList<>(rows);
                rs = statement.getGeneratedKeys();
                while (rs.next()) {
                    keys.add(rs.getObject(1));
                }
            }

            return new InsertResult(rows, keys);
        } catch (Exception e) {
            //打印debug的日志信息
            DebugLogger.debugLogger(buildResult, e);
            //对外统一异常
            throw new DataSourceException(e);

        } finally {
            DbUtil.release(rs);
            DbUtil.release(statement);
            DbUtil.release(connection);
        }
    }


    //具体的业务逻辑实现操作,单行用values，多行用value比较快
    //单个obj insert into tablename('field','field') values()
    //多个实体 insert into tableName('field','field') value(),value()
    @Override
    public InsertResult result() {
        return this.result(false);
    }


}
