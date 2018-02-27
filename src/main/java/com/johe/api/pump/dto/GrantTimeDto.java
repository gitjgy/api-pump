package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="【授时】数据传输对象")
public class GrantTimeDto {
	
	@Setter @Getter @ApiModelProperty(value="库房ID",required=true) private String requestid;
	@Setter @Getter @ApiModelProperty(value="请求密钥",required=true) private String requestauth;
	@Setter @Getter @ApiModelProperty(value="请求时间") private String requesttime;
	@Setter @Getter @ApiModelProperty(value="设备编号") private String equipment;
	@Setter @Getter @ApiModelProperty(value="时间格式："
			+ "01(yyyy-MM-dd HH:mm:ss)"
			+ "、02(yyyy年MM月dd日 HH时mm分ss秒)"
			+ "、03(yyyy-MM-dd HH:mm:ss.SSSS)"
			+ "、04(yy/MM/dd HH:mm)"
			+ "、05(yyyy年MM月dd日 HH时mm分ss秒 E)"
			+ "、06(yyyy年MM月dd日 HH时mm分ss秒SSSS毫秒)"
			+ "、07(HH时mm分ss秒SSSS毫秒)",
			allowableValues="01,02,03,04,05,06、07") private String pattern;
}
