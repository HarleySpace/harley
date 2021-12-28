package com.rzdata.ps.exception;

public class DataNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private Integer errCode = 1000;
    private Object detail;

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public DataNotFoundException(String message) {
        this(1000, message);
    }

    public DataNotFoundException(String errorName, String message) {
        super(message);
    }

    public DataNotFoundException(Integer errorCode, String message) {
        super(message);
        this.errCode = errorCode;
    }

    public DataNotFoundException(Integer errorCode, String message, Object detail) {
        super(message);
        this.errCode = errorCode;
        this.detail = detail;
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotFoundException(Integer errCode, String message, Throwable cause) {
        super(message, cause);
        this.errCode = errCode;
    }

    public Integer getErrCode() {
        return this.errCode;
    }

    public Object getDetail() {
        return this.detail;
    }

}
