package com.johe.api.pump.dto;

import java.util.List;

import com.johe.api.pump.entity.DepartmentEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="登录用户-数据传输对象")
@Data
public class SysUserDto {
	
	@ApiModelProperty(value="用户ID") private long user_id;
	@ApiModelProperty(value="登录账号") private String account;
	@ApiModelProperty(value="用户名称") private String user_name;
	@ApiModelProperty(value="单位ID") private String org_id;
	@ApiModelProperty(value="部门ID") private String cert_no;
	@ApiModelProperty(value="职位：01主管领导、02上级领导、03库管员、04普通用户") private String wkno;
	
    @ApiModelProperty(value="所属部门")
	private DepartmentEntity dept;
    
    @ApiModelProperty(value="菜单列表") private List<PdaMenuDto> menu_list;
    
}
