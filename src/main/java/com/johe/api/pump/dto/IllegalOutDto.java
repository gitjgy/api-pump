package com.johe.api.pump.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="【非法出库】数据传输对象")
public class IllegalOutDto {
	
	@Setter @Getter @ApiModelProperty(value="库房ID",required=true) private String requestid;
	@Setter @Getter @ApiModelProperty(value="请求密钥",required=true) private String requestauth;
	@Setter @Getter @ApiModelProperty(value="请求时间") private String requesttime;
//	@Setter @Getter @ApiModelProperty(value="数据长度") private String datalenth;
	@Setter @Getter @ApiModelProperty(value="操作人") private String optperson;
	@Setter @Getter @ApiModelProperty(value="设备编号") private String equipment;
//	@Setter @Getter @ApiModelProperty(value="物料数量（数字）") private long materialnum;
	@Setter @Getter @ApiModelProperty(value="开门时间") private String opentime;
	@Setter @Getter @ApiModelProperty(value="关门时间") private String closetime;
	@Setter @Getter @ApiModelProperty(value="操作时间（限19位字符）",example="例如：2018-01-09 19:29:59",required=true) private String opttime;
	
	@Setter @Getter @ApiModelProperty(value="非法物料列表（物料对象）",required=true) private List<IllegalMaterialDto> material;

}
