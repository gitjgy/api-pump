package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="扫码出库单（含物料）-数据传输对象")
@Data
public class ScanOutOrderDto {
	
	@ApiModelProperty(value="出库类型：01借用出库、02领料出库、03加工出库",required=true,allowableValues="01,02,03") private String out_type;
//	@ApiModelProperty(value="编号") private String sn_no;
	@ApiModelProperty(value="领料地点（限64字符）",required=true) private String pick_address;
	@ApiModelProperty(value="发货仓库ID",required=true) private long storage_id;
	@ApiModelProperty(value="发货日期（限10字符）",example="例如：2018-01-01",required=true) private String delivery_date;
	@ApiModelProperty(value="制单人",required=true) private long make_person;
	@ApiModelProperty(value="领料人（限32字符）",required=true) private String pick_person;
	@ApiModelProperty(value="发货人（限32字符）",required=true) private String delivery_person;
//	@ApiModelProperty(value="批次号") private String batch_no;
	@ApiModelProperty(value="商品编码：大类code+小类code（限6字符）",example="例如：001010",required=true) private String product_code;
	@ApiModelProperty(value="商品名称（小类ID）",required=true) private long small_id;
	@ApiModelProperty(value="物料类别（大类ID）",required=true) private long big_id;
	@ApiModelProperty(value="规格型号") private String spec_model;
	@ApiModelProperty(value="条形码（限18字符）",example="例如：001002001A01010001",required=true) private String barcode;
	@ApiModelProperty(value="商品品牌（限64字符）") private String brand;
	@ApiModelProperty(value="物料特性：01普通、02市采、03重要、04关键",required=true,allowableValues="01,02,03,04") private String feature;
	@ApiModelProperty(value="单价",required=true) private double price=0.00;
	@ApiModelProperty(value="辅助属性（限32字符）") private String assist_attr;
	@ApiModelProperty(value="出库数量",required=true) private double item_qty=0.00;
	@ApiModelProperty(value="计量单位（限16字符）",required=true) private String measure_unit;
	@ApiModelProperty(value="仓位-区",required=true) private long sbin_area;
	@ApiModelProperty(value="仓位-架",required=true) private long sbin_rack;
	@ApiModelProperty(value="仓位-位",required=true) private long sbin_pos;
	@ApiModelProperty(value="仓位代码（限5字符）") private String stockbin_code;
}
