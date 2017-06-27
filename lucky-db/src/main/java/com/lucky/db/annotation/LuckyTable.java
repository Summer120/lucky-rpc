package com.lucky.db.annotation;

import java.lang.annotation.*;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 13:28 2017/6/27
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LuckyTable {
    /**
     * 表分片字段
     * @return
     */
    String shardKeys() default "";
}
