package com.johe.api.pump.entity.result;

import lombok.Getter;
import lombok.Setter;

public enum ResultStatus {
	OK(0,"OK"),
	
	UN_SUCCESS_CREATE(-20000,"创建不成功"),
	
	UNKNOWN_EXCEPTION(-10000,"服务内部，发生未知异常"),
	
	LOGIN_FAILED(-10000,"验证失败，请检查账号密码~"),
	OLD_PWD_WRONG(-10010,"修改密码失败，原密码不正确~"),
	PWD_TOO_LONG(-10011,"修改密码失败，新密码过长（<=6）~"),
	COLUMN_NULL_EXCEPTION(-10012,"操作数据库出错"),
	
	AUTHENTICATION_FAILED(-10001,"登录不成功，认证失败，请检查账号密码~"),
	LOGIN_PARAM_FIELD_UNKNOW(-10002,"登录不成功，未知参数字段~"),
	
	ACCOUNT_IS_EXISTED(-20001,"注册失败，此账号已存在，请更换账号~"),
	CREATE_FAILED(-20002,"创建入库单不成功，发生未知异常"),
	
	ARGUMENT_TYPE_MISMATCH(-30001,"参数类型不匹配"),
	ARGUMENT_VALUE_ILLEGAL(-30002,"参数值非法"),//illegal
	
	TOKEN_EXPIRED_ERROR(250250,"客户端TOKEN过期啦~，重新获取吧~"),
	TOKEN_FORMAT_ERROR(-40001,"TOKEN格式有误，请检查~"),
	TOKEN_OTHER_ERROR(-40002,"校验TOKEN时，发生未知错误~"),
	
	INTERNAL_SERVER_ERROR(-50000,"内部服务处理请求时，出错啦~"),
	
	
	//==================== 外部系统对接 （非法出库）===================
	ILLEGAL_OUT_SUCCESS(0,"illegal outbound"),
	ILLEGAL_OUT_REQ_ERROR(1,"错误的请求方法"),
	ILLEGAL_OUT_REQ_EMPTY(2,"请求的字符串为空"),
	ILLEGAL_OUT_REQ_JSON_EXP(3,"JSON字符串不合法"),
	ILLEGAL_OUT_REQ_AUTH_ERR(4,"密钥验证不通过"),
	ILLEGAL_OUT_REQ_EQU_ERR(5,"错误的设备ID"),
	ILLEGAL_OUT_REQ_NUM_UNAUTH(6,"号码未授权"),
	ILLEGAL_OUT_REQ_SQL_EXP(7,"数据库操作异常"),	
	
	
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
