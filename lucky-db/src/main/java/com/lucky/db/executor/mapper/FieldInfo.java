package com.lucky.db.executor.mapper;

import com.lucky.db.convert.TypeHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:chaoqiang.zhou
 * @Description:字段信息操作
 * @Date:Create in 13:23 2017/6/27
 */
@Getter
@Setter
public class FieldInfo {

    //字段的set方法索引信息
    private int getIndex;
    private int setIndex;
    boolean key;
    boolean auto;
//    //初始化的时候，直接缓存操作
//    TypeHandler<?> typeHandler;
    //返回值类型信息
    Class<?> type;

    public FieldInfo(int getIndex, int setIndex, Class<?> type) {
        this.getIndex = getIndex;
        this.setIndex = setIndex;
        this.type = type;
//        this.typeHandler = handler;
    }
}
