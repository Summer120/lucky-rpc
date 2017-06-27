package com.lucky.db.executor.mapper;

import com.lucky.db.exception.ConfigException;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:chaoqiang.zhou
 * @Description:实体映射关系
 * @Date:Create in 13:22 2017/6/27
 */
public class Mapper {


    //用来缓存实体信息，也就是类似于object的操作信息
    private static Map<String, EntityInfo> entityCache = new HashMap<>();

    public static EntityInfo getEntityInfo(Class<?> clazz) {
        String key = clazz.getTypeName();
        EntityInfo info = entityCache.get(key);
        if (info == null) {
            info = new EntityInfo(clazz);
            if (info.fields.size() <= 0) {
                throw new ConfigException("clazz is Not a valid entity");
            }
        }
        return info;
    }
}
