package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="扫码入库单（含物料信息）-数据传输对象")
@Data
public class ScanInOrderDto {
	
	@ApiModelProperty(value="入库类型：02采购入库、03归还入库、04其他入库、05加工入库",required=true,allowableValues="02,03,04,05") private String in_type;
//	@ApiModelProperty(value="编号") private String sn_no;
	@ApiModelProperty(value="部门（限32字符）") private String dept;
	@ApiModelProperty(value="制单人ID",required=true) private long make_person;
	@ApiModelProperty(value="制单机构（限64字符）",required=true) private String make_inst;
	@ApiModelProperty(value="摘要（限64字符）") private String summary;
//	@ApiModelProperty(value="开票方式") private String open_invoice;
//	@ApiModelProperty(value="批次号") private String batch_no;
	@ApiModelProperty(value="记账人（限32字符）") private String tally_person;
	@ApiModelProperty(value="验收人（限32字符）") private String acceptor;
	@ApiModelProperty(value="保管人（限32字符）") private String keeper;
	@ApiModelProperty(value="业务员（限32字符）") private String salesman;
	@ApiModelProperty(value="负责人（限32字符）") private String leader;
	
//	@ApiModelProperty(value="入库单ID",required=true) private long sin_id;
	@ApiModelProperty(value="商品编码：大类code+小类code （限32字符）",example="例如：001010",required=true) private String product_code;
	@ApiModelProperty(value="小类ID",required=true) private long small_id;
//	@ApiModelProperty(value="小类名称",required=true) private String small_name;
	@ApiModelProperty(value="大类ID",required=true) private long big_id;
//	@ApiModelProperty(value="大类名称",required=true) private String big_name;
	@ApiModelProperty(value="规格型号（限32字符）") private String spec_model;
	@ApiModelProperty(value="条形码（限18字符）",example="编码规则：大类代码+小类代码+仓库代码+仓位区+仓位架+仓位位+四位编号。例如：001002001A01010001",required=true) private String barcode;
	@ApiModelProperty(value="供应商ID",required=true) private long supply_id;
	@ApiModelProperty(value="品牌（限64字符）") private String brand;
	@ApiModelProperty(value="物料特性：01普通、02市采、03重要、04关键",required=true,allowableValues="01,02,03,04") private String feature;
	@ApiModelProperty(value="单价",required=true) private double price=0.00;
//	@ApiModelProperty(value="金额",required=true) private double amount=0.00;
	@ApiModelProperty(value="实入数量",required=true) private double act_qty_recv=0.00;
	@ApiModelProperty(value="计量单位名称（限16字符）",required=true) private String measure_unit;
	@ApiModelProperty(value="收货仓库ID",required=true) private long storage_id;
	@ApiModelProperty(value="仓位-区ID",required=true) private long sbin_area;
	@ApiModelProperty(value="仓位-架ID",required=true) private long sbin_rack;
	@ApiModelProperty(value="仓位-位ID",required=true) private long sbin_pos;
	@ApiModelProperty(value="仓位代码(区CODE+架CODE+位CODE)（限5字符）") private String stockbin_code;
	
	@ApiModelProperty(value="辅助属性（限128字符）") private String assist_attr;
	@ApiModelProperty(value="应入数量",required=true) private double item_qty=0.00;
	@ApiModelProperty(value="单位成本",required=true) private double unit_cost=0.00;
//	@ApiModelProperty(value="开票方式") private String open_invoice;
	@ApiModelProperty(value="采购日期（限10字符）",example="例如：2018-01-02",required=true) private String buy_date;
	@ApiModelProperty(value="收料日期（限10字符）",example="例如：2018-01-02",required=true) private String recv_date;
	@ApiModelProperty(value="保质期（限16字符）",example="例如：2个月") private String expired;
	@ApiModelProperty(value="有效期",example="例如：2018-01-02",required=true) private String valid_until;
//	@ApiModelProperty(value="收料总仓") private String receipt_total;
//	@ApiModelProperty(value="备注") private String remark;
}
