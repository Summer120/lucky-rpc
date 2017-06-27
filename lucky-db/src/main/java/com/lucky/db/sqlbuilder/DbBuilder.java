package com.lucky.db.sqlbuilder;

import com.lucky.db.executor.mapper.EntityInfo;
import com.lucky.db.executor.result.BuildResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 20:32 2017/6/27
 */
public class DbBuilder implements Builder {


    @Override
    public BuildResult insertBuilder(EntityInfo entityInfo, List<Object> objects) {
        List<Object> fieldObj = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO").append(entityInfo.getTableName())
                .append("(");
        for (String columns : entityInfo.getInsertColumns()) {
            sb.append(columns).append(",");
        }
        //去除最后一个逗号
        sb.deleteCharAt(sb.length() - 1);
        //单个obj insert into tablename('field','field') values()
        //多个实体 insert into tableName('field','field') value(),value()
        if (objects.size() > 1) {
            objects.forEach(data -> {
                sb.append("value(").append(buildArgs(entityInfo.getInsertColumns().length)).append("),");
                fieldObj.addAll(Arrays.asList(entityInfo.getInsertValues(data)));
            });
        } else {
            //单个实体插入操作
            sb.append("values(").append(buildArgs(entityInfo.getInsertColumns().length)).append("),");
            fieldObj.addAll(Arrays.asList(entityInfo.getInsertValues(objects.get(0))));
        }
        sb.deleteCharAt(sb.length() - 1);
        return new BuildResult(fieldObj, sb.toString());
    }


    private String buildArgs(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("?,");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
