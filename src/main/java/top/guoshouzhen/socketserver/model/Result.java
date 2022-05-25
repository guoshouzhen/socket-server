package top.guoshouzhen.socketserver.model;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description
 * @date 2022/3/27 23:41
 */
public class Result {
    private int result;
    private String code;
    private String msg;
    private Object data;

    private Result() {
    }

    public <T> Result(int result, String code, String msg, T data) {
        this.result = result;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result success() {
        return success(null);
    }

    public static <T> Result success(T data) {
        return new Result(1, "200", "操作成功", data);
    }

    public static Result fail() {
        return fail("500", "操作失败");
    }

    public static Result fail(String code, String msg) {
        return fail(code, msg, null);
    }

    public static Result fail(ErrorCodeEnum errorCodeEnum) {
        return fail(errorCodeEnum.getErrCode(), errorCodeEnum.getErrMsg());
    }

    public static <T> Result fail(String code, String msg, T data) {
        return new Result(0, code, msg, data);
    }

    public int getResult() {
        return result;
    }

    public Object getData() {
        return data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
