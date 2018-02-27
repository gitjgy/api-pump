package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="【非法出库】响应物料对象")
public class IllegalMaterialDto {

	@Setter @Getter @ApiModelProperty(value="物料条形码（限18位字符）",example="例：AJ0102001A01020001",required=true) private String barcode;
}
