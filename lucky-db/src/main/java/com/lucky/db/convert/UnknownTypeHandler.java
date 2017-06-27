package com.lucky.db.convert;


import org.apache.ibatis.io.Resources;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:chaoqiang.zhou
 * @Description:未定义得参数类型
 * @Date:Create in 18:57 2017/6/27
 */
public class UnknownTypeHandler extends BaseTypeHandler<Object> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object array = rs.getObject(columnName);
        return array == null ? null : array;
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object array = rs.getObject(columnIndex);
        return array == null ? null : array;
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object array = cs.getArray(columnIndex);
        return array == null ? null : array;
    }
}
