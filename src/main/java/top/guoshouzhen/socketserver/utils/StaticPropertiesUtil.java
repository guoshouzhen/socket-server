package top.guoshouzhen.socketserver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description
 * @date 2022/5/21 18:39
 */
public class StaticPropertiesUtil {
    private static final Logger LOGGER;
    private static final Properties PROP;
    //加载配置文件
    static{
        LOGGER = LoggerFactory.getLogger(StaticPropertiesUtil.class);
        PROP = new Properties();
        try{
            PROP.load(PROP.getClass().getResourceAsStream("/application.properties"));
        }catch (Exception ex){
            LOGGER.error("加载配置文件时发生异常，异常信息：", ex);
        }
    }

    /**
     * 根据key获取配置文件value
     * @author shouzhen.guo
     * @date 2022/5/21 19:05
     * @param key key
     * @return java.lang.String
     */
    public static String getValue(String key){
        return PROP.getProperty(key);
    }
}
