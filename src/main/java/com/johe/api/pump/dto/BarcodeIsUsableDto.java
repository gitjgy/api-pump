package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="根据入库类型、输入的条码，判断此条码是否可用")
@Data
public class BarcodeIsUsableDto {
	
	@ApiModelProperty(value="入库类型：02采购入库、03归还入库、04其他入库、05加工入库",required=true,allowableValues="02,03,04,05") private String in_type;
	@ApiModelProperty(value="条形码(18位)",required=true) private String bar_code;
	@ApiModelProperty(value="条码是否可用") private boolean flg_usabled=false;
	@ApiModelProperty(value="不可用原因") private String unusable_cause;
}
