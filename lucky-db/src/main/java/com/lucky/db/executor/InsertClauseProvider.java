package com.lucky.db.executor;

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

/**
 * @Author:chaoqiang.zhou
 * @Description:插入语句操作
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
        EntityInfo entityInfo = Mapper.getEntityInfo(objects.get(0).getClass());
        return new DbBuilder().insertBuilder(entityInfo, objects);
    }

    @Override
    public InsertResult result(Boolean returnKeys) {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            BuildResult buildResult = print();
            int option = returnKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
            statement = connection.prepareStatement(buildResult.getSql(), option);
            List<Object> args = buildResult.getArgs();
            if (args != null) {
                //在这里做javatype---》to---》jdbctype类型转换操作
                DbUtil.setParameters(statement, args);
            }
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
