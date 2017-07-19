package com.lucky.db.shard;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 14:18 2017/7/19
 */
public interface Rule {
    Object getMatchKey(Object key);
}
