package nafos.core.entry;

/**
 * @Author 黄新宇
 * @Date 2018/10/27 下午3:35
 * @Description TODO
 **/
public class BusinessException extends RuntimeException{

    private Integer code;
    private String message;
    public BusinessException() {
    }
    public BusinessException(String errmsg) {
        super(errmsg);
        this.code=2;
        this.message=errmsg;
    }


    public BusinessException(int code , String errmsg) {
        super(errmsg);
        this.code=code;
        this.message=errmsg;
    }


    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code +
                ", \"message\":\"" + message + "\"" +"}";
    }
}

