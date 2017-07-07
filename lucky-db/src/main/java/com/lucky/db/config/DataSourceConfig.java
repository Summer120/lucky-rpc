package com.lucky.db.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author:chaoqiang.zhou
 * @Description:数据源信息配置
 * @Date:Create in 17:46 2017/6/24
 */
@Getter
@Setter
public class DataSourceConfig {
    private String name;
    private String driver;
    //默认是DBCP数据源连接池
    private DataSourceType dataSourceType = DataSourceType.DBCP;
    private DataSourceParams dataSourceParams = new DataSourceParams();

    @Getter
    @Setter
    public static class DataSourceParams {
        private String userName;
        private String password;
        private String url;
        //增加默认值操作
        private int MaxIdleConns = 1;
        private int MaxOpenConns = 100;
    }


}
