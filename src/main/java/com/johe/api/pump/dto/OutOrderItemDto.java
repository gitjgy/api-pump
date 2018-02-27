package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="出库单栏目-数据传输对象")
@Data
public class OutOrderItemDto {
//	@ApiModelProperty(value="出库单ID") private long osp_id;
	@ApiModelProperty(value="商品编码：大类code+小类code",example="例如：AJ1010",required=true) private String product_code;
	@ApiModelProperty(value="商品名称（小类ID）",required=true) private long product_name;
	@ApiModelProperty(value="物料类别（大类ID）",required=true) private long category_name;
	@ApiModelProperty(value="规格型号") private String spec_model;
	@ApiModelProperty(value="条形码",required=true) private String barcode;
	@ApiModelProperty(value="商品品牌") private String brand;
	@ApiModelProperty(value="商品（物料）特性：01普通、02市采、03重要、04关键",required=true,allowableValues="01,02,03,04") private String feature;
	@ApiModelProperty(value="单价",required=true) private double price;
	@ApiModelProperty(value="应出数量",required=true) private double item_qty;
	@ApiModelProperty(value="计量单位",required=true) private String measure_unit;
	@ApiModelProperty(value="发货仓库ID",required=true) private long storage;
	@ApiModelProperty(value="仓位-区",required=true) private long sbin_area;
	@ApiModelProperty(value="仓位-架",required=true) private long sbin_rack;
	@ApiModelProperty(value="仓位-位",required=true) private long sbin_pos;
	@ApiModelProperty(value="仓位代码",required=true) private String stockbin_code;
}
