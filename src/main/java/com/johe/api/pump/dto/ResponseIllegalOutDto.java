package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="【非法出库】响应对象")
public class ResponseIllegalOutDto {
	
	@Setter @Getter @ApiModelProperty(value="库房ID") private String responseid;
	@Setter @Getter @ApiModelProperty(value="回应时间") private String responsetime;
	@Setter @Getter @ApiModelProperty(value="错误码： 0：成功，1：错误的请求方法，2：请求的字符串为空，3：JSON字符串不合法，4：密钥验证不通过，5：错误的设备ID，6：号码未授权，7：数据库操作异常") private String error;
	@Setter @Getter @ApiModelProperty(value="返回数据") private Object data;
	
}
