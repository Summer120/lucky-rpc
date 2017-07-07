package com.lucky.db.executor.context;

import com.lucky.db.executor.result.BasicResult;
import com.lucky.db.executor.result.InsertResult;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 12:49 2017/6/26
 */
public interface InsertClause extends SQLContext {
    /**
     * 获取插入后，自增的主键操作
     *
     * @param returnKeys
     * @return
     */
    InsertResult result(Boolean returnKeys);
    InsertResult result();
}
