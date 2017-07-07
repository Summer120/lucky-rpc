package com.lucky.db.sqlbuilder;

import com.lucky.db.executor.mapper.EntityInfo;
import com.lucky.db.executor.result.BuildResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author:chaoqiang.zhou
 * @Description:Stringbuilder,后期看会不会出现多线程的问题
 * @Date:Create in 20:32 2017/6/27
 */
public class DbBuilder implements Builder {


    @Override
    public BuildResult insertBuilder(EntityInfo entityInfo, List<Object> objects) {
        List<Object> fieldObj = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO ").append(entityInfo.getTableName())
                .append("(");
        for (String columns : entityInfo.getInsertColumns()) {
            sb.append(columns).append(",");
        }
        //去除最后一个逗号
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") values ");
        //单个obj insert into tablename('field','field') values()
        //多个实体 insert into tableName('field','field') value(),value()

        objects.forEach(data -> {
            sb.append("(").append(buildArgs(entityInfo.getInsertColumns().length)).append("),");
            fieldObj.addAll(Arrays.asList(entityInfo.getInsertValues(data)));
        });
        sb.deleteCharAt(sb.length() - 1);
        return new BuildResult(fieldObj, sb.toString());
    }


    /**
     * 更新语句的拼接build
     *
     * @param entityInfo：table的字段信息
     * @param object：要拼接的实体信息
     * @return
     */
    @Override
    public BuildResult updateBuilder(EntityInfo entityInfo, Object object, List<String> updateColumns) {
        Objects.requireNonNull(entityInfo.getIdColumns());
        StringBuffer sb = new StringBuffer();
        List<Object> args = new ArrayList<>();
        //update tablename set field=?,field=? where ID=?,AND IDS=?
        sb.append("UPDATE ").append(entityInfo.getTableName()).append(" SET ");
        updateColumns.forEach(column -> {
            sb.append(column).append(" =?,");
        });
        //add update fields
        args.addAll(Arrays.asList(entityInfo.getUpdateValues(object, updateColumns)));
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" WHERE ");

        //带id的字段信息
        for (int i = 0; i < entityInfo.getIdColumns().length; i++) {
            sb.append(entityInfo.getIdColumns()[i]).append("=?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        //add where object
        args.addAll(Arrays.asList(entityInfo.getUpdateValues(object, Arrays.asList(entityInfo.getIdColumns()))));
        return new BuildResult(args, sb.toString());
    }


    /***
     * delete builder 操作信息
     * @param entityInfo
     * @param obj,通过实体的@id字段来删除操作，强硬删除
     * @return
     */
    @Override
    public BuildResult deleteBuilder(EntityInfo entityInfo, Object obj) {
        StringBuffer sb = new StringBuffer();
        List<Object> args = new ArrayList<>();
        sb.append("DELETE FROM ").append(entityInfo.getTableName()).append(" WHERE ");
        //带id的字段信息
        for (int i = 0; i < entityInfo.getIdColumns().length; i++) {
            sb.append(entityInfo.getIdColumns()[i]).append("=?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        args.addAll(Arrays.asList(entityInfo.getUpdateValues(obj, Arrays.asList(entityInfo.getIdColumns()))));
        return new BuildResult(args, sb.toString());
    }


    /**
     * 拼接clazz的sql语句操作
     *
     * @param entityInfo：包含的字段信息
     * @param whereSql：条件信息
     * @param args：参数信息
     * @return
     */
    @Override
    public BuildResult selectBuilder(EntityInfo entityInfo, SQL whereSql, List<Object> args) {
        StringBuffer sb = new StringBuffer();
        //add select field information
        for (String key : entityInfo.fields.keySet()) {
            whereSql.SELECT(key);
        }
        whereSql.FROM(entityInfo.getTableName());
        return new BuildResult(args, sb.toString());
    }


    private String buildArgs(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("?,");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
