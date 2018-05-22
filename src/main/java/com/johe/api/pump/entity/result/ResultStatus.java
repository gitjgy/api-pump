package com.johe.api.pump.entity.result;

import lombok.Getter;
import lombok.Setter;

public enum ResultStatus {
	OK(0,"OK"),
	
	UN_SUCCESS_CREATE(-20000,"创建不成功"),
	
	UNKNOWN_EXCEPTION(-10000,"Error-10000:the unknow exception."),
	JPA_SYSTEM_EXCEPTION(-10011,"Error--10011:jpa system exception"),//JpaSystemException
	
	LOGIN_FAILED(-10000,"验证失败，请检查账号密码~"),
	OLD_PWD_WRONG(-10010,"修改密码失败，原密码不正确~"),
	PWD_TOO_LONG(-10011,"修改密码失败，新密码过长（<=6）~"),
	COLUMN_NULL_EXCEPTION(-10012,"操作数据库出错"),
	
	AUTHENTICATION_FAILED(-10001,"登录不成功，认证失败，请检查账号密码~"),
	LOGIN_PARAM_FIELD_UNKNOW(-10002,"登录不成功，未知参数字段~"),
	
	ACCOUNT_IS_EXISTED(-20001,"注册失败，此账号已存在，请更换账号~"),
	CREATE_FAILED(-20002,"创建入库单不成功，发生未知异常"),
	
	ARGUMENT_TYPE_MISMATCH(-30001,"参数类型不匹配"),
	ARGUMENT_VALUE_ILLEGAL(-30002,"Error-30002:the input argument is illegal,please check it."),//illegal
	
	TOKEN_EXPIRED_ERROR(250250,"客户端TOKEN过期啦~，重新获取吧~"),
	TOKEN_FORMAT_ERROR(-40001,"TOKEN格式有误，请检查~"),
	TOKEN_OTHER_ERROR(-40002,"校验TOKEN时，发生未知错误~"),
	
	INTERNAL_SERVER_ERROR(-50000,"内部服务处理请求时，出错啦~"),
	
	
	//==================== 外部系统对接 （非法出库）===================
	ILLEGAL_OUT_SUCCESS(0,"OK"),
	ILLEGAL_OUT_REQ_ERROR(1,"Error-1:the request method is wrong."),
	ILLEGAL_OUT_REQ_EMPTY(2,"Error-2:the request is empty."),
	ILLEGAL_OUT_REQ_JSON_EXP(3,"Error-3:the json object is illegal."),
	ILLEGAL_OUT_REQ_AUTH_ERR(4,"Error-4:the request auth is wrong."),
	ILLEGAL_OUT_REQ_EQU_ERR(5,"Error-5:the equip ID is wrong."),
	ILLEGAL_OUT_REQ_NUM_UNAUTH(6,"Error-6:the number is wrong."),
	ILLEGAL_OUT_REQ_SQL_EXP(7,"Error-7:the server error."),	
	
	ILLEGAL_OUT_REQ_OK(0,"All barcodes are OK."),	
	ILLEGAL_OUT_BARCODE_ILLEGAL(9001,"At least one of those barcodes is illegal outbound."),	
	
	
	AUDIT_FAILED(-50001,"审核不成功，发生未知异常");
	
	
	@Getter
	@Setter
	private int code;
	@Getter
	@Setter
	private String message;
	
	ResultStatus(int code,String message){
		this.code = code;
		this.message = message;
	}
}
