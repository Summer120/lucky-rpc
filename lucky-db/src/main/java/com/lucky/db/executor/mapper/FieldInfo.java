package com.lucky.db.executor.mapper;

/**
 * @Author:chaoqiang.zhou
 * @Description:字段信息操作
 * @Date:Create in 13:23 2017/6/27
 */
public class FieldInfo {

    //字段的set方法索引信息
    private int getIndex;
    private int setIndex;
    boolean key;
    boolean auto;
    //返回值类型信息
    Class<?> type;

    public FieldInfo(int getIndex, int setIndex, Class<?> type) {
        this.getIndex = getIndex;
        this.setIndex = setIndex;
        this.type = type;
    }
}
