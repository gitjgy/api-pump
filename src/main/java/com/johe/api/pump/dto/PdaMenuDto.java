package com.johe.api.pump.dto;

import java.util.List;

import com.johe.api.pump.entity.DepartmentEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="菜单-数据传输对象")
@Data
public class PdaMenuDto {
	
	@ApiModelProperty(value="上级菜单ID") private long parent_id;
	@ApiModelProperty(value="菜单ID") private long menu_id;
	@ApiModelProperty(value="菜单名称") private String meun_name;
}
