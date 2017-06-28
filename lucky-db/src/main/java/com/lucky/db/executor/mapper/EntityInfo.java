package com.lucky.db.executor.mapper;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.lucky.db.annotation.LuckyTable;
import lombok.Getter;
import lucky.util.lang.FieldsUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.beans.Transient;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author:chaoqiang.zhou
 * @Description:实体 methodaccess:参考https://unmi.cc/java-reflectasm-bytecode-usage/
 * @Date:Create in 13:24 2017/6/27
 */
public class EntityInfo {

    @Getter
    private MethodAccess method;
    @Getter
    String tableName;
    //缓存字段信息,有效的字段，有get和set方法
    public Map<String, FieldInfo> fields = new HashMap<>();
    // 删除,更新条件字段(带 Id 注解的)
    String[] idColumns;
    // 插入字段(不带 GeneratedValue 注解的)
    String[] insertColumns;
    // 更新字段(不带 Id 注解的)
    String[] updateColumns;
    //表分片的字段信息
    String shardKeys;

    public EntityInfo(Class<?> clazz) {
        method = MethodAccess.get(clazz);
        Table tableAnno = clazz.getAnnotation(Table.class);
        if (tableAnno == null || StringUtils.isEmpty(tableAnno.name())) {
            this.tableName = clazz.getSimpleName();
        }

        this.tableName = tableAnno.name();
        //分片的字段信息
        this.shardKeys = clazz.getAnnotation(LuckyTable.class).shardKeys();
        initField(clazz);
    }


    //用来缓存字段的信息
    private void initField(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String> tempIdColumns = new ArrayList<>();
        for (Field f : fields) {
            FieldInfo fi = getFieldInfo(f);
            if (fi == null) {
                continue;
            }

            String name = this.getFieldName(f);
            Id idAnno = f.getAnnotation(Id.class);
            if (idAnno != null) {
                fi.key = true;
                tempIdColumns.add(name);
            }
            GeneratedValue generatedAnno = f.getAnnotation(GeneratedValue.class);
            if (generatedAnno != null) {
                fi.auto = true;
            }
            this.fields.put(name, fi);
        }
        if (!tempIdColumns.isEmpty()) {
            this.idColumns = tempIdColumns.toArray(new String[0]);
        }
        if (clazz.getSuperclass() != Object.class) {
            initField(clazz.getSuperclass());
        }
    }


    private FieldInfo getFieldInfo(Field f) {
        //该字段代表排除信息
        Transient transientAnno = f.getAnnotation(Transient.class);
        if (transientAnno != null) {
            return null;
        }

        int getIndex = this.getMethodIndex(FieldsUtil.getGetterName(f));
        //代表没有get和set的方法信息
        if (getIndex == -1) {
            return null;
        }

        int setIndex = this.getMethodIndex(FieldsUtil.getSetterName(f));
        if (setIndex == -1) {
            return null;
        }

//        TypeHandler<?> typeHandler = TypeHandlerRegistry.TYPE_HANDLER_MAP.get(f.getType());
//        //如果未找到的话，就不转了
//        if (typeHandler == null) {
//            typeHandler = new UnknownTypeHandler();
//        }

        return new FieldInfo(getIndex, setIndex, f.getType());
    }


    //获取get和set的索引值，方面后面调用，进行缓存操作
    private int getMethodIndex(String name) {
        String[] names = method.getMethodNames();
        for (int i = 0; i < names.length; i++) {
            if (name.equals(names[i])) {
                return i;
            }
        }

        return -1;
    }


    //需要考虑column的标签配置
    private String getFieldName(Field f) {

        Column columnAn = f.getAnnotation(Column.class);
        if (columnAn == null || StringUtils.isEmpty(columnAn.name())) {
            return f.getName();
        }
        return columnAn.name();
    }


    //获取各个字段的信息
    public Object[] getInsertValues(Object obj) {
        Object[] result = new Object[this.getInsertColumns().length];
        //循环遍历字段集合
        for (int i = 0; i < this.getInsertColumns().length; i++) {
            //反射调用gett方法
            result[i] = getValue(obj, this.getInsertColumns()[i]);
        }
        return result;
    }


    public Object getValue(Object obj, String field) {
        FieldInfo fieldInfo = this.fields.get(field);
        if (fieldInfo == null) return null;

        //在拼接的时候做转换
        return method.invoke(obj, fieldInfo.getGetIndex());

    }

    //需要过滤掉带generated自动生成的标识
    public String[] getInsertColumns() {
        //懒加载模式
        if (this.insertColumns == null) {
            List<String> columns = new ArrayList<>(fields.size());
            Iterator<Map.Entry<String, FieldInfo>> iter = fields.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, FieldInfo> fi = iter.next();
                //排除是自动生成的标签
                if (!fi.getValue().auto) {
                    columns.add(fi.getKey());
                }

            }
            this.insertColumns = columns.toArray(new String[0]);
        }
        return insertColumns;
    }


    public String[] getUpdateColumns() {
        if (this.updateColumns == null) {
            List<String> columns = new ArrayList<>(fields.size());
            Iterator<Map.Entry<String, FieldInfo>> iter = fields.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, FieldInfo> fi = iter.next();
                if (!fi.getValue().key) {
                    columns.add(fi.getKey());
                }
            }
            this.updateColumns = columns.toArray(new String[0]);
        }
        return this.updateColumns;
    }

    public String[] getIdColumns() {
        return this.idColumns;
    }
}
