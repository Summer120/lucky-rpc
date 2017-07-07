package com.lucky.db.executor;

import com.lucky.db.convert.TypeHandler;
import com.lucky.db.convert.TypeHandlerRegistry;
import com.lucky.db.debug.DebugLogger;
import com.lucky.db.exception.DataSourceException;
import com.lucky.db.executor.result.BasicResult;
import com.lucky.db.executor.result.InsertResult;
import com.lucky.db.executor.result.SelectResult;
import lucky.util.log.Logger;
import lucky.util.log.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 20:18 2017/6/27
 */
public class DbUtil {
    private static Logger logger = LoggerFactory.getLogger(DbUtil.class);


    //执行insert、update、delete的sql语句
    public static InsertResult executeUpdate(ConnectionManager manager, String sql, List<Object> args, boolean returnKeys) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            int option = returnKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
            connection = manager.getConnection();
            statement = connection.prepareStatement(sql, option);
            //在这里做javatype---》to---》jdbctype类型转换操作
            setParameters(statement, args.toArray());
            int rows = statement.executeUpdate();
            List<Object> keys = null;
            if (rows > 0 && returnKeys) {
                keys = new ArrayList<>(rows);
                rs = statement.getGeneratedKeys();
                while (rs.next()) {
                    keys.add(rs.getObject(1));
                }
            }
            return new InsertResult(rows, keys);
        } catch (Exception e) {
            DebugLogger.debugLogger(sql, args, e);
            //打印debug的日志信息//对外统一异常
            throw new DataSourceException(e);
        } finally {
            DbUtil.release(rs);
            DbUtil.release(statement);
            DbUtil.release(connection);
        }

    }


    //执行update、delete的sql语句
    public static BasicResult executeUpdate(ConnectionManager manager, String sql, List<Object> args) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection = manager.getConnection();
            statement = connection.prepareStatement(sql);
            //在这里做javatype---》to---》jdbctype类型转换操作
            setParameters(statement, args.toArray());
            int rows = statement.executeUpdate();
            return new BasicResult(rows);
        } catch (Exception e) {
            DebugLogger.debugLogger(sql, args, e);
            //打印debug的日志信息//对外统一异常
            throw new DataSourceException(e);
        } finally {
            DbUtil.release(rs);
            DbUtil.release(statement);
            DbUtil.release(connection);
        }

    }


    /**
     * 执行查询的语句的信息
     *
     * @param sql
     * @param args
     * @return
     */
    public static SelectResult executeQuery(ConnectionManager manager, String sql, List<Object> args) {
        PreparedStatement statement = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = manager.getConnection();
            statement = connection.prepareStatement(sql);
            //在这里做javatype---》to---》jdbctype类型转换操作
            setParameters(statement, args.toArray());
            rs = statement.executeQuery();
            return new SelectResult(rs, statement, connection);
        } catch (Exception e) {
            DebugLogger.debugLogger(sql, args, e);
            //打印debug的日志信息//对外统一异常
            throw new DataSourceException(e);
        } finally {
            //todo:查询的操作，不可以在这里关闭链接，否则，数据取不出来
//            DbUtil.release(rs);
//            DbUtil.release(statement);
//            DbUtil.release(connection);
        }
    }

    public static void release(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                logger.error("close rs failed,error{}", e);
            }
        }

    }

    public static void release(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                logger.error("close statement failed,error{}", e);
            }
        }
    }

    public static void release(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                logger.error("close connection failed,error{}", e);
            }
        }
    }

    public static void setParameters(PreparedStatement ps, Object... args) throws SQLException {
        for (int i = 0, n = args.length; i < n; i++) {
            //巧妙的利用TypeHandler来设置参数
            //需要判断是否为null
            if (args[i] == null) {
                ps.setObject(i + 1, args[i]);
                continue;
            }
            TypeHandler typeHandler = TypeHandlerRegistry.TYPE_HANDLER_MAP.get(args[i].getClass());
            if (typeHandler == null) {
                throw new SQLException("LuckyDb could not find a TypeHandler instance for " + args[i].getClass());
            } else {
                typeHandler.setParameter(ps, i + 1, args[i], null);
            }
        }
    }


    public static Object getObject(ResultSet resultSet, int columnLabel, Object object) throws SQLException {
        TypeHandler typeHandler = TypeHandlerRegistry.TYPE_HANDLER_MAP.get(object.getClass());
        if (typeHandler == null) {
            throw new SQLException("LuckyDb could not find a TypeHandler instance for " + object.getClass().getClass());
        } else {
            return typeHandler.getResult(resultSet, columnLabel);
        }
    }
}
