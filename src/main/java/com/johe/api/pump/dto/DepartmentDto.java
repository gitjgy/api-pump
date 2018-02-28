package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="部门列表-数据传输对象")
@Data
public class DepartmentDto {
	@ApiModelProperty(value="部门ID）") private long dept_id;
	@ApiModelProperty(value="部门代码") private String dept_code;
	@ApiModelProperty(value="部门名称") private String dept_name;
}
