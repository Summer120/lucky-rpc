package com.lucky.db.executor;

import com.lucky.db.convert.TypeHandler;
import com.lucky.db.convert.TypeHandlerRegistry;
import lucky.util.log.Logger;
import lucky.util.log.LoggerFactory;

import java.sql.*;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 20:18 2017/6/27
 */
public class DbUtil {
    private static Logger logger = LoggerFactory.getLogger(DbUtil.class);

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
            TypeHandler typeHandler = TypeHandlerRegistry.TYPE_HANDLER_MAP.get(args[i].getClass());
            if (typeHandler == null) {
                throw new SQLException("LuckyDb could not find a TypeHandler instance for " + args[i].getClass());
            } else {
                typeHandler.setParameter(ps, i + 1, args[i], null);
            }
        }
    }
}
