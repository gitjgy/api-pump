package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="借用记录-数据传输对象")
public class PickRecordDto {
	@Setter @Getter @ApiModelProperty(value="领料地点") private String pick_addr;
	@Setter @Getter @ApiModelProperty(value="借用人") private String pick_person;
	@Setter @Getter @ApiModelProperty(value="借用时间") private String pick_time;
}
