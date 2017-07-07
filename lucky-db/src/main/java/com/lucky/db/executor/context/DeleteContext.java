package com.lucky.db.executor.context;

import com.lucky.db.executor.ConditionType;
import com.lucky.db.executor.result.BasicResult;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 13:43 2017/6/26
 */
public interface DeleteContext  extends SQLContext{

    DeleteContext where(String field, ConditionType type, Object value);

    DeleteContext or();

    DeleteContext and();
    BasicResult result();
}
