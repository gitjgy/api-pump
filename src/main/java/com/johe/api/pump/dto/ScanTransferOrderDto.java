package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="调拨单（含物料列表）-数据传输对象")
@Data
public class ScanTransferOrderDto {	
	
	@ApiModelProperty(value="物流公司（限64字符）") private String logistics_co;
	@ApiModelProperty(value="物流单号（限64字符）") private String logistics_order_sn;
	@ApiModelProperty(value="泵站名称（领料地点）（限64字符）",required=true) private String pump_name;
	@ApiModelProperty(value="对方单据号（限64字符）") private String other_order_sn;
//	@ApiModelProperty(value="调拨单编号") private String order_sn;
	@ApiModelProperty(value="调出地址（限64字符）",required=true) private String out_address;
	@ApiModelProperty(value="调拨部门名称（限64字符）",required=true) private String dept;
	@ApiModelProperty(value="调拨领导（限32字符）",required=true) private String leader;
	@ApiModelProperty(value="调拨人（限32字符）",required=true) private String transfer_person;
	@ApiModelProperty(value="领用人（限32字符）",required=true) private String use_person;
	@ApiModelProperty(value="制单人ID",required=true) private long make_person;
//	@ApiModelProperty(value="制单日期",required=true) private String date;
	@ApiModelProperty(value="门店验收人（限32字符）") private String acceptor;
	@ApiModelProperty(value="收货日期（限10字符）") private String receipt_date;


	@ApiModelProperty(value="商品编码（限6字符）",required=true) private String product_code;
	 @ApiModelProperty(value="商品名称（小类ID）",required=true) private long small_id;
	 @ApiModelProperty(value="规格型号（限32字符）",required=true) private String spec_model;
	 @ApiModelProperty(value="条形码（限18字符）") private String barcode;
	 @ApiModelProperty(value="物料类别（大类ID）",required=true) private long big_id;
	 @ApiModelProperty(value="商品品牌（限64字符）") private String brand;
	 @ApiModelProperty(value="单价") private double price=0.00;
	 @ApiModelProperty(value="计量单位（限16字符）",required=true) private String measure_unit;
//	 @ApiModelProperty(value="金额") private double tranitem_amount=0.00;
	 @ApiModelProperty(value="数量") private double item_qty=0.00;
	 @ApiModelProperty(value="调入仓库ID",required=true) private long in_stock_id;
	 @ApiModelProperty(value="调入仓位区",required=true) private long in_bin_area;
	 @ApiModelProperty(value="调入仓位架",required=true) private long in_bin_rack;
	 @ApiModelProperty(value="调入仓位位",required=true) private long in_bin_pos;
	 @ApiModelProperty(value="调入仓位代码（限5字符）",required=true) private String in_bin_code;
	 @ApiModelProperty(value="调出仓库ID",required=true) private long out_stock;
	 @ApiModelProperty(value="调出仓位区",required=true) private long out_bin_area;
	 @ApiModelProperty(value="调出仓位架",required=true) private long out_bin_rack;
	 @ApiModelProperty(value="调出仓位位",required=true) private long out_bin_pos;
	 @ApiModelProperty(value="调出仓位代码（限5字符）",required=true) private String out_bin_code;
	 @ApiModelProperty(value="物料特性：01普通、02市采、03重要、04关键",required=true) private String mt_feature;
//	 @ApiModelProperty(value="备注") private String remark;
}
