package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="调拨单栏目-数据传输对象")
@Data
public class TransferItemDto {
	 @ApiModelProperty(value="商品编码",required=true) private String tranitem_product_code;
	 @ApiModelProperty(value="商品名称（小类ID）",required=true) private long tranitem_product_name;
	 @ApiModelProperty(value="规格型号",required=true) private String tranitem_spec_model;
	 @ApiModelProperty(value="条形码") private String tranitem_barcode;
	 @ApiModelProperty(value="物料类别（大类ID）",required=true) private long tranitem_category;
	 @ApiModelProperty(value="商品品牌") private String tranitem_brand;
	 @ApiModelProperty(value="单价") private double tranitem_price=0.00;
	 @ApiModelProperty(value="计量单位",required=true) private String tranitem_measure_unit;
	 @ApiModelProperty(value="金额") private double tranitem_amount=0.00;
	 @ApiModelProperty(value="数量") private double tranitem_quantity=0.00;
	 @ApiModelProperty(value="调入仓库ID",required=true) private long tranitem_in_stock;
	 @ApiModelProperty(value="调入仓位区",required=true) private long tranitem_in_bin_area;
	 @ApiModelProperty(value="调入仓位架",required=true) private long tranitem_in_bin_rack;
	 @ApiModelProperty(value="调入仓位位",required=true) private long tranitem_in_bin_pos;
	 @ApiModelProperty(value="调入仓位代码",required=true) private String tranitem_in_bin_code;
	 @ApiModelProperty(value="调出仓库ID",required=true) private long tranitem_out_stock;
	 @ApiModelProperty(value="调出仓位区",required=true) private long tranitem_out_bin_area;
	 @ApiModelProperty(value="调出仓位架",required=true) private long tranitem_out_bin_rack;
	 @ApiModelProperty(value="调出仓位位",required=true) private long tranitem_out_bin_pos;
	 @ApiModelProperty(value="调出仓位代码",required=true) private String tranitem_out_bin_code;
	 @ApiModelProperty(value="备注") private String tranitem_remark;
	 @ApiModelProperty(value="物料特性：01普通、02市采、03重要、04关键",required=true) private String mt_feature;
}
