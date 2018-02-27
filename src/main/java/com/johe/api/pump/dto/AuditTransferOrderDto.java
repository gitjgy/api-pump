package com.johe.api.pump.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="审核调拨单（含物料列表）-数据传输对象")
@Data
public class AuditTransferOrderDto {	
	
	@ApiModelProperty(value="调拨单ID") private Long tran_id;
	@ApiModelProperty(value="调出仓库ID",required=true) private long tran_out_stock;
	@ApiModelProperty(value="审核状态",required=true) private String audit_status;
	@ApiModelProperty(value="审核人ID",required=true) private long audit_person_id;
	@ApiModelProperty(value="审核人名称",required=true) private String audit_person_name;
	@ApiModelProperty(value="审核意见",required=true) private String reject_reason;
	
	@ApiModelProperty(value="调拨单对应的物料列表",required=true) private List<AuditTransferItemDto> item_list;
}
