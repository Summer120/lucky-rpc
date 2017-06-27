package com.lucky.db.exception;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 21:03 2017/6/27
 */
public class DataSourceException extends PersistenceException{
    private static final long serialVersionUID = -5251396250407091334L;

    public DataSourceException() {
        super();
    }

    public DataSourceException(String message) {
        super(message);
    }

    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceException(Throwable cause) {
        super(cause);
    }
}
