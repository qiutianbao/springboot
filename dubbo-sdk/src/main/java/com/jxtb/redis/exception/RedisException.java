package com.jxtb.redis.exception;

/**
 * Created by jxtb on 2019/6/12.
 */
public class RedisException extends RuntimeException{
    public static final long servialVersionUID = 1L;
    public String errorCode;

    public RedisException() {
    }
    public RedisException(Throwable cause) {
        super(cause);
    }
    public RedisException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public RedisException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
