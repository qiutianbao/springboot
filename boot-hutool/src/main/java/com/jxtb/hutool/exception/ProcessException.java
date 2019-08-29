package com.jxtb.hutool.exception;

/**
 * Created by jxtb on 2019/8/1.
 */
public class ProcessException extends RuntimeException{

    public String errorCode;

    public ProcessException(){

    }

    public ProcessException(Throwable throwable){
        super(throwable);
    }

    public ProcessException(String message){
        super(message);
    }

    public ProcessException(String errCode,String message){
        super(message);
        this.errorCode = errCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
