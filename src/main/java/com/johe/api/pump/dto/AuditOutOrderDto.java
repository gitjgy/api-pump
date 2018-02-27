package com.johe.api.pump.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="审核出库单（含物料列表）-数据传输对象")
@Data
public class AuditOutOrderDto {
	
	@ApiModelProperty(value="出库类型：01借用出库、02领料出库、03加工出库",required=true,allowableValues="01,02,03") private String out_type;
	@ApiModelProperty(value="出库单ID",required=true) private long order_id;
	@ApiModelProperty(value="发货仓库ID",required=true) private long delivery_st_id;
	@ApiModelProperty(value="审核状态",required=true) private String audit_status;
	@ApiModelProperty(value="审核人ID",required=true) private long audit_person_id;
	@ApiModelProperty(value="审核人名称",required=true) private String audit_person_name;
	@ApiModelProperty(value="审核意见",required=true) private String reject_reason;
	
	@ApiModelProperty(value="出库单对应的物料列表",required=true) private List<AuditOutOrderItemDto> item_list;
}
