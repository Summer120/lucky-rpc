package com.lucky.db.debug;

import com.lucky.db.executor.result.BuildResult;
import lucky.util.log.Logger;
import lucky.util.log.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:chaoqiang.zhou
 * @Description:用来记录sql信息，debug级别的日志信息
 * @Date:Create in 16:56 2017/6/27
 */
public class DebugLogger {

    //    到时候需要单据配置该logger的名称
    public static final String LOGGER_NAME = "cmc.lucky.db.logger";
    public static final Logger logger = LoggerFactory.getLogger(LOGGER_NAME);


    public static List<String> argss = new ArrayList<>();

    public static void debugLogger(BuildResult result) {
        logger.debug("debug sql {},args{}", result.getSql(), result.getArgs());
    }
}
