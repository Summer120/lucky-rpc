package com.lucky.db.executor.context;

import com.lucky.db.executor.ConditionType;
import com.lucky.db.executor.LockMode;
import com.lucky.db.executor.Sorter;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 18:22 2017/6/26
 */
public interface SelectClause extends SQLContext {
    public SelectClause JOIN(String join);

    public SelectClause INNER_JOIN(String join);

    public SelectClause LEFT_OUTER_JOIN(String join);

    public SelectClause RIGHT_OUTER_JOIN(String join);

    public SelectClause OUTER_JOIN(String join);

    public SelectClause WHERE(String conditions, ConditionType type, Object value);

    public SelectClause OR();

    public SelectClause AND();

    public SelectClause GROUP_BY(String... columns);

    public SelectClause HAVING(String conditions);

    public SelectClause ORDER_BY(String columns);

    public SelectClause ORDER_BY(String columns, Sorter sorter);

    //todo:扩展分页的查询语句操作
    public SelectClause LIMIT(String... columns);

    //锁模式操作
    <T> T result(LockMode lockMode);
}
