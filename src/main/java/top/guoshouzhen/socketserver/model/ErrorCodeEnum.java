package top.guoshouzhen.socketserver.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 错误枚举
 * @date 2021/12/28 19:31
 */
public enum ErrorCodeEnum {

    /**
     * 通用错误
     */
    A0000("A0000", "系统忙，请稍后再试"),
    /**
     * 接口参数错误信息
     */
    A0001("A0001", "请求参数错误，请稍后重试"),

    Z9999("", "");

    /**
     * 错误码
     */
    private final String errCode;
    /**
     * 错误消息
     */
    private final String errMsg;

    ErrorCodeEnum(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public static ErrorCodeEnum getErrorEnumByCode(String errCode) {
        return Arrays.stream(ErrorCodeEnum.values())
                .filter(x -> Objects.equals(errCode, x.getErrCode()))
                .findFirst().orElse(ErrorCodeEnum.A0000);
    }
}
