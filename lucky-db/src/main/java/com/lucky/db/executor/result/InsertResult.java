package com.lucky.db.executor.result;

import java.util.List;

/**
 * @Author:chaoqiang.zhou
 * @Description:参照mybatis
 * @Date:Create in 20:23 2017/6/27
 */
public class InsertResult extends BasicResult{
    //插入后获取自增的主键操作
    private List<Object> keys;

    public InsertResult(int affectedRows, List<Object> keys) {
        super(affectedRows);
        this.keys = keys;
    }

    /**
     * 获取自动生成的自增主键列表
     * @return
     */
    public List<Object> getKeys() {
        return this.keys;
    }
}
