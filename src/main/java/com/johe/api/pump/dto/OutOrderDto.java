package com.johe.api.pump.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="出库单（含物料列表）-数据传输对象")
@Data
public class OutOrderDto {
	
	@ApiModelProperty(value="出库类型：01借用出库、02领料出库、03加工出库",required=true,allowableValues="01,02,03") private String out_type;
//	@ApiModelProperty(value="编号") private String sn_no;
	@ApiModelProperty(value="领料地点",required=true) private String pick_address;
	@ApiModelProperty(value="发货仓库",required=true) private long delivery_storage;
	@ApiModelProperty(value="发货日期",example="例如：2018-01-01",required=true) private String delivery_date;
	@ApiModelProperty(value="制单人ID",required=true) private long make_person;
	@ApiModelProperty(value="领料人",required=true) private String pick_person;
	@ApiModelProperty(value="发货人",required=true) private String delivery_person;
//	@ApiModelProperty(value="批次号") private String batch_no;
	@ApiModelProperty(value="特性（物料列表中的最高特性）",required=true) private String feature;
	@ApiModelProperty(value="入库单对应的物料列表",required=true) private List<OutOrderItemDto> item_list;
}
