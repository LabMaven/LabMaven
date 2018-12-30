package com.tide.ailab.common.exception;

import com.tide.ailab.common.util.StringUtil;

/**
 * 调度系统统一异常定义类，统一规划系统异常
 * @author User
 */
public class DispatchException extends RuntimeException {

    private static final long serialVersionUID = -6034324836011469058L;

    private int exceptionCode;

    private String message;

    public DispatchException() {
        super();
        this.exceptionCode = DispatchExceptionCode.UNKNOW_ERROR;
    }

    public DispatchException(String message) {
        super(message);
        this.exceptionCode = DispatchExceptionCode.UNKNOW_ERROR;
    }

    public DispatchException(int exceptionCode) {
        super();
        this.exceptionCode = exceptionCode;
    }

    public DispatchException(int exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public DispatchException(Throwable cause) {
        super(cause);
        this.exceptionCode = DispatchExceptionCode.UNKNOW_ERROR;
    }

    public DispatchException(int exceptionCode, Throwable cause) {
        super(cause);
        this.exceptionCode = exceptionCode;
    }

    public int getExceptionCode() {
        return exceptionCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        if (StringUtil.isNullOrEmpty(message)) {
            return super.getMessage();
        } else {
            return message;
        }
    }

}
