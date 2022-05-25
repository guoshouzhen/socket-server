package top.guoshouzhen.socketserver.utils;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description
 * @date 2022/3/26 0:11
 */
public class IntegerUtil {
    /**
     * 将一个int值转为字节数组存储（大端方式存储）
     *
     * @param num int值
     * @return byte[]
     * @author guoshouzhen
     * @date 2022/3/26 0:19
     */
    public static byte[] toByteAry(int num) {
        byte[] bytes = new byte[4];

        bytes[0] = (byte) ((num >>> 24) & 0xff);
        bytes[1] = (byte) ((num >>> 16) & 0xff);
        bytes[2] = (byte) ((num >>> 8) & 0xff);
        bytes[3] = (byte) (num & 0xff);
        return bytes;
    }

    /**
     * 将一个字节数组转为int值（大端方式）
     *
     * @param bytes 字节数组
     * @return int
     * @author guoshouzhen
     * @date 2022/3/26 0:24
     */
    public static int toInt(byte[] bytes) {
        return (bytes[0] & 0xFF) << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF << 8) | (bytes[3] & 0xFF);
    }
}
