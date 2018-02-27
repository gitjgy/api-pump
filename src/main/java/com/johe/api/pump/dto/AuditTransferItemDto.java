package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="调拨单栏目-数据传输对象")
@Data
public class AuditTransferItemDto {
	
	@ApiModelProperty(value="栏目ID") private Long item_id;
	 @ApiModelProperty(value="调拨单ID") private Long tran_id;
	 @ApiModelProperty(value="商品编码",required=true) private String product_code;
	 @ApiModelProperty(value="条形码") private String barcode;
	 @ApiModelProperty(value="实调数量") private double act_qty_tran=0.00;
	 @ApiModelProperty(value="调入仓库ID",required=true) private long tranitem_in_stock;
	 @ApiModelProperty(value="调入仓位代码",required=true) private String tranitem_in_bin_code;
	 @ApiModelProperty(value="调出仓库ID",required=true) private long tranitem_out_stock;
	 @ApiModelProperty(value="调出仓位代码",required=true) private String tranitem_out_bin_code;
}
