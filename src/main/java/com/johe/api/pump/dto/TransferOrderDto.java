package com.johe.api.pump.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="调拨单（含物料列表）-数据传输对象")
@Data
public class TransferOrderDto {	
	
	@ApiModelProperty(value="调拨单ID") private Long tran_id;
	@ApiModelProperty(value="物流公司") private String tran_logistics_co;
	@ApiModelProperty(value="物流单号") private String tran_logistics_order_sn;
	@ApiModelProperty(value="泵站名称",required=true) private String tran_pump_name;
	@ApiModelProperty(value="对方单据号") private String tran_other_order_sn;
	@ApiModelProperty(value="调拨单编号") private String tran_order_sn;
	@ApiModelProperty(value="调出仓库",required=true) private long tran_out_stock;
	@ApiModelProperty(value="调出地址",required=true) private String tran_out_address;
	@ApiModelProperty(value="调拨部门",required=true) private String tran_dept;
	@ApiModelProperty(value="调拨领导",required=true) private String tran_leader;
	@ApiModelProperty(value="调拨人",required=true) private String tran_transfer_person;
	@ApiModelProperty(value="领用人",required=true) private String tran_use_person;
	@ApiModelProperty(value="审核人") private String tran_audit_person;
	@ApiModelProperty(value="制单人ID",required=true) private long tran_make_person;
	@ApiModelProperty(value="制单日期",required=true) private String tran_date;
	@ApiModelProperty(value="门店验收人") private String tran_acceptor;
	@ApiModelProperty(value="收货日期") private String tran_receipt_date;
	@ApiModelProperty(value="物料特性",required=true) private String mt_feature;
	@ApiModelProperty(value="调拨单对应的物料列表",required=true) private List<TransferItemDto> item_list;
}
