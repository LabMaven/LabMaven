package com.tide.ailab.common.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tide.ailab.common.log.Logger;
import com.tide.ailab.common.model.JsonResult;
import com.tide.ailab.common.model.JsonResultType;

/**
 * 调度系统异常处理器，统一捕获系统抛出的异常，并返回前端
 * 
 * @author User
 */
@RestControllerAdvice
public class DispatchExceptionHandler {

	@Autowired
	MessageSource source;

	@ExceptionHandler({ DispatchUnauthException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public String resolveUnauthException(DispatchUnauthException e, HttpServletRequest request) {
		e.setMessage(source.getMessage(String.valueOf(e.getExceptionCode()), null, request.getLocale()));
		Logger.e(e);
		JsonResult result = new JsonResult();
		result.setType(JsonResultType.ERROR);
		result.setMessage(e.getMessage());
		return result.toJSON();
	}

	@ExceptionHandler({ Exception.class })
	@ResponseStatus(HttpStatus.OK)
	public String resolveException(Exception e, HttpServletRequest request) {
		// 日志中记录原始的异常信息，而后经过包装返回前端
		DispatchException ex = null;
		if (e instanceof DispatchException) {
			ex = (DispatchException) e;

		} else {
			ex = new DispatchException(e);
		}
		ex.setMessage(source.getMessage(String.valueOf(ex.getExceptionCode()), null, request.getLocale()));
		Logger.e(ex);
		JsonResult result = new JsonResult();
		result.setType(JsonResultType.ERROR);
		result.setMessage(ex.getMessage());
		return result.toJSON();
	}
}
