package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="库管或采购员-数据传输对象")
@Data
public class UserDto {
	
	@ApiModelProperty(value="用户ID") private long user_id;
	@ApiModelProperty(value="用户名称") private String user_name;
	@ApiModelProperty(value="角色名称") private String role_name;
}
