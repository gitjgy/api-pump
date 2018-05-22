package com.johe.api.pump.dto;

import java.util.List;

import com.johe.api.pump.entity.InOrderItemEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="审核入库单（含物料列表）-数据传输对象")
@Data
public class AuditInOrderDto {
	
	@ApiModelProperty(value="入库类型：02采购入库、03归还入库、04其他入库、05加工入库",required=true,allowableValues="02,03,04,05") private String in_type;
	@ApiModelProperty(value="入库单ID",required=true) private long order_id;
	@ApiModelProperty(value="收货仓库ID",required=true) private long recv_st_id;
	@ApiModelProperty(value="审核状态",required=true) private String audit_status;
	@ApiModelProperty(value="审核人ID",required=true) private long audit_person_id;
	@ApiModelProperty(value="审核人名称",required=true) private String audit_person_name;
	@ApiModelProperty(value="审核意见",required=true) private String reject_reason;
//	@ApiModelProperty(value="入库单对应的物料列表",required=true) private List<AuditInOrderItemDto> item_list;
	@ApiModelProperty(value="入库单对应的物料列表",required=true) private List<InOrderItemEntity> item_list;
	
}
