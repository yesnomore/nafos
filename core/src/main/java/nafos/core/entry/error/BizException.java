package nafos.core.entry.error;

/**
 * @Author 黄新宇
 * @Date 2018/10/27 下午3:35
 * @Description TODO
 **/
public class BizException extends RuntimeException {

    private Integer code;
    private String message;

    public static BizException LOGIN_SESSION_TIME_OUT = new BizException(600,"登录失效，请重新登录");

    public BizException() {
    }

    public BizException(String errmsg) {
        super(errmsg);
        this.code = 500;
        this.message = errmsg;
    }


    public BizException(int code, String errmsg) {
        super(errmsg);
        this.code = code;
        this.message = errmsg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "\"error\":" + code +
                ", \"message\":\"" + message + "\"" + "}";
    }
}

