package com.lucky.db.shard;

/**
 * @Author:chaoqiang.zhou
 * @Description:hash路由规则
 * @Date:Create in 14:18 2017/7/19
 */
public class HashRule implements Rule {
    private int count;

    public HashRule(int count) {
        this.count = count;
    }

    @Override
    public Object getMatchKey(Object key) {
        int hash = key.hashCode();
        return Math.abs(hash % count);
    }
}
