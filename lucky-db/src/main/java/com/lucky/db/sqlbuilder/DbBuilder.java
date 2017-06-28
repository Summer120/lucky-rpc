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
        sb.append(" values ");
        //单个obj insert into tablename('field','field') values()
        //多个实体 insert into tableName('field','field') value(),value()

        objects.forEach(data -> {
            sb.append("(").append(buildArgs(entityInfo.getInsertColumns().length)).append("),");
            fieldObj.addAll(Arrays.asList(entityInfo.getInsertValues(data)));
        });
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
