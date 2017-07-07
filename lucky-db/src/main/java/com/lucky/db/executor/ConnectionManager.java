package com.lucky.db.executor;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Author:chaoqiang.zhou
 * @Description:管理连接操作,抽象类，通过子类来获取connect操作，来控制事务操作
 * @Date:Create in 16:39 2017/7/4
 */
public interface ConnectionManager {

    Connection getConnection();
}
