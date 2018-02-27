package com.johe.api.pump.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="调拨单-实体对象")
@Entity
@Table(name="pump_transfer_order")
public class TransferOrderEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @Column(name="tran_id",insertable=false,updatable=false) @ApiModelProperty(value="调拨单ID") private Long tranid;
	@Setter @Getter @ApiModelProperty(value="物流公司") private String tran_logistics_co;
	@Setter @Getter @ApiModelProperty(value="物流单号") private String tran_logistics_order_sn;
	@Setter @Getter @ApiModelProperty(value="泵站名称",required=true) private String tran_pump_name;
	@Setter @Getter @ApiModelProperty(value="对方单据号") private String tran_other_order_sn;
	@Setter @Getter @ApiModelProperty(value="调拨单编号") private String tran_order_sn;
	@Setter @Getter @ApiModelProperty(value="调出仓库",required=true) private long tran_out_stock;
	@Setter @Getter @ApiModelProperty(value="调出地址",required=true) private String tran_out_address;
	@Setter @Getter @ApiModelProperty(value="调拨部门",required=true) private String tran_dept;
	@Setter @Getter @ApiModelProperty(value="调拨领导",required=true) private String tran_leader;
	@Setter @Getter @ApiModelProperty(value="调拨人",required=true) private String tran_transfer_person;
	@Setter @Getter @ApiModelProperty(value="领用人",required=true) private String tran_use_person;
	@Setter @Getter @ApiModelProperty(value="审核人") private String tran_audit_person;
	@Setter @Getter @ApiModelProperty(value="审核日期") private String tran_audit_date;
	@Setter @Getter @ApiModelProperty(value="状态：01待审核、02主管通过、03主管驳回、04上级通过、05上级驳回、06库管通过、07库管驳回") private String tran_status;
	@Setter @Getter @ApiModelProperty(value="驳回原因") private String tran_reject_reason;
	@Setter @Getter @ApiModelProperty(value="制单人ID",required=true) private long tran_make_person;
	@Setter @Getter @ApiModelProperty(value="制单日期",required=true) private String tran_date;
	@Setter @Getter @ApiModelProperty(value="门店验收人") private String tran_acceptor;
	@Setter @Getter @ApiModelProperty(value="收货日期") private String tran_receipt_date;
	@Setter @Getter @ApiModelProperty(value="物料特性") private String mt_feature;
}
