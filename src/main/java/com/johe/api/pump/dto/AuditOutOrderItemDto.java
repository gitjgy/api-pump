package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="审核出库单栏目-数据传输对象")
@Data
public class AuditOutOrderItemDto {
	@ApiModelProperty(value="出库单ID",required=true) private long osp_id;
	@ApiModelProperty(value="商品编码",required=true) private String product_code;
	@ApiModelProperty(value="条形码",required=true) private String barcode;
	@ApiModelProperty(value="实出数量",required=true) private double act_qty_out=0.00;
	@ApiModelProperty(value="发货仓库ID",required=true) private long storage;
	@ApiModelProperty(value="仓位代码",required=true) private String stockbin_code;
}
