package com.johe.api.pump.exception;

import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ExpiredJwtException.class)
	@ResponseBody
	public ResultEntity<String> expiredJwtExceptionHandler(ExpiredJwtException exception) throws Exception{
		return new ResultEntity<String>(ResultStatus.TOKEN_EXPIRED_ERROR.getCode(),
				ResultStatus.TOKEN_EXPIRED_ERROR.getMessage(), 
				null);
	}
	
	
	@ExceptionHandler(JpaSystemException.class)
	@ResponseBody
	public ResultEntity<String> expiredJwtExceptionHandler(Exception exception) throws Exception{
		return new ResultEntity<String>(ResultStatus.UNKNOWN_EXCEPTION.getCode(),
				ResultStatus.UNKNOWN_EXCEPTION.getMessage(), 
				exception.getMessage());
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseBody
	public ResultEntity<String> expiredMethodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException exception) throws Exception{
		return new ResultEntity<String>(ResultStatus.ARGUMENT_TYPE_MISMATCH.getCode(),
				ResultStatus.ARGUMENT_TYPE_MISMATCH.getMessage(), 
				exception.getMessage());
	}
	

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResultEntity<String> expiredUnknownExceptionHandler(Exception exception) throws Exception{
		return new ResultEntity<String>(ResultStatus.UNKNOWN_EXCEPTION.getCode(),
				ResultStatus.UNKNOWN_EXCEPTION.getMessage(), 
				exception.getMessage());
	}
}
