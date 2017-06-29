package com.lucky.db.sqlbuilder;

import com.lucky.db.executor.mapper.EntityInfo;
import com.lucky.db.executor.result.BuildResult;

import java.util.List;

/**
 * @Author:chaoqiang.zhou
 * @Description:根据对象拼接sql语句操作
 * @Date:Create in 20:32 2017/6/27
 */
public interface Builder {

    BuildResult insertBuilder(EntityInfo entityInfo, List<Object> objects);


    BuildResult updateBuilder(EntityInfo entityInfo, Object object, List<String> updateColumns);

    BuildResult deleteBuilder(EntityInfo entityInfo, Object obj);
}
