package com.johe.api.pump.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="入库单（含物料列表）-数据传输对象")
@Data
public class InOrderDto {
	
	@ApiModelProperty(value="入库类型：02采购入库、03归还入库、04其他入库、05加工入库",required=true,allowableValues="02,03,04,05") private String in_type;
//	@ApiModelProperty(value="编号") private String sn_no;
	@ApiModelProperty(value="供应商ID") private long supply;
	@ApiModelProperty(value="部门") private String dept;
	@ApiModelProperty(value="制单人ID",required=true) private long make_person;
	@ApiModelProperty(value="制单机构",required=true) private String make_inst;
	@ApiModelProperty(value="摘要") private String summary;
//	@ApiModelProperty(value="开票方式") private String open_invoice;
//	@ApiModelProperty(value="批次号") private String batch_no;
	@ApiModelProperty(value="收货仓库ID",required=true) private long sin_receive_stock;
	@ApiModelProperty(value="特性（物料列表中的最高特性）",required=true) private String feature;
	
	@ApiModelProperty(value="入库单对应的物料列表",required=true) private List<InOrderItemDto> item_list;
	
	@ApiModelProperty(value="记账人（限32字符）") private String tally_person;
	@ApiModelProperty(value="验收人（限32字符）") private String acceptor;
	@ApiModelProperty(value="保管人（限32字符）") private String keeper;
	@ApiModelProperty(value="业务员（采购员）（限32字符）") private String salesman;
	@ApiModelProperty(value="负责人（限32字符）") private String leader;
}
