package com.lucky.db.executor.context;

import com.lucky.db.executor.result.BasicResult;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 13:44 2017/6/26
 */
public interface DeleteClause extends SQLContext{
    BasicResult result();
}
