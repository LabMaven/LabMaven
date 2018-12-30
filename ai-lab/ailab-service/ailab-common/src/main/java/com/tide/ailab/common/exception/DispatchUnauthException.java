package com.tide.ailab.common.exception;

/**
 * 调度系统token失效异常定义类
 * @author User
 */
public class DispatchUnauthException extends DispatchException {

    private static final long serialVersionUID = 4106039127727892708L;

    public DispatchUnauthException() {
        super();
    }

    public DispatchUnauthException(int exceptionCode) {
        super(exceptionCode);
    }

}
