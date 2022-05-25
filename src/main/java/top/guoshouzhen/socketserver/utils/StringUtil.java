package top.guoshouzhen.socketserver.utils;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description
 * @date 2022/3/27 14:45
 */
public class StringUtil {
    /**
     * 分隔符26
     */
    public static final String SPLIT_26 = String.valueOf((char)26);

    /**
     * 分隔符16
     */
    public static final String SPLIT_16 = String.valueOf((char)16);

    /**
     * 判断目标字符串是否为空（null, "", " "）
     * @author guoshouzhen
     * @date 2021/11/21 15:57
     * @param str 待判断字符
     * @return java.lang.Boolean
     */
    public static boolean isNullOrWhiteSpace(String str){
        return str == null || "".equals(str.trim());
    }
}
