package backend.common;

/**
 * @author charles
 * @date 2023/3/3 23:02
 */
public enum ResultCode {
    SUCCESS(200, "success"),
    FAILED(500, "fail"),
    VALIDATE_FAILED(404, "invalid args");
    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
