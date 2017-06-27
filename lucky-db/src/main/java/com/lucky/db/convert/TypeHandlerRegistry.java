/*
 *    Copyright 2009-2013 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.lucky.db.convert;


import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型处理器注册机
 */
public final class TypeHandlerRegistry {
    //枚举型map
    public final static Map<Type, TypeHandler<?>> TYPE_HANDLER_MAP = new HashMap<Type, TypeHandler<?>>();

    static {
        //构造函数里注册系统内置的类型处理器
        //以下是为多个类型注册到同一个handler
        register(Boolean.class, new BooleanTypeHandler());
        register(boolean.class, new BooleanTypeHandler());

        register(Byte.class, new ByteTypeHandler());
        register(byte.class, new ByteTypeHandler());

        register(Short.class, new ShortTypeHandler());
        register(short.class, new ShortTypeHandler());

        register(Integer.class, new IntegerTypeHandler());
        register(int.class, new IntegerTypeHandler());

        register(Long.class, new LongTypeHandler());
        register(long.class, new LongTypeHandler());

        register(Float.class, new FloatTypeHandler());
        register(float.class, new FloatTypeHandler());

        register(Double.class, new DoubleTypeHandler());
        register(double.class, new DoubleTypeHandler());
        register(String.class, new StringTypeHandler());
        register(BigInteger.class, new BigIntegerTypeHandler());
        register(BigDecimal.class, new BigDecimalTypeHandler());
        register(Byte[].class, new ByteObjectArrayTypeHandler());
        register(byte[].class, new ByteArrayTypeHandler());
        register(Date.class, new DateTypeHandler());
        register(java.sql.Date.class, new SqlDateTypeHandler());
        register(Character.class, new CharacterTypeHandler());
        register(char.class, new CharacterTypeHandler());
    }

    public static <T> void register(Class<?> javaType, TypeHandler<T> typeHandler) {
        TYPE_HANDLER_MAP.put(javaType, typeHandler);

    }


}
