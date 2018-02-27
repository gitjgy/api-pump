package com.johe.api.pump.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="出库单-实体对象")
@Entity
@Table(name="pump_out_stock_pick_order")
public class OutOrderEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @Column(name="osp_id",insertable=false,updatable=false) @ApiModelProperty(value="出库单ID") private Long ospid;
	@Setter @Getter @ApiModelProperty(value="单号",required=true) private String osp_sn;
	@Setter @Getter @ApiModelProperty(value="批次号") private String osp_batch_no;
	@Setter @Getter @ApiModelProperty(value="制单日期") private String osp_date;
	@Setter @Getter @ApiModelProperty(value="领料地点") private String osp_pick_address;
	@Setter @Getter @ApiModelProperty(value="发货仓库") private long osp_delivery_stock;
	@Setter @Getter @ApiModelProperty(value="出库类型：01借用出库、02领料出库、03加工出库") private String osp_type;
	@Setter @Getter @ApiModelProperty(value="制单人") private long osp_make_person;
	@Setter @Getter @ApiModelProperty(value="审核人") private String osp_audit_person;
	@Setter @Getter @ApiModelProperty(value="审核日期") private String osp_audit_date;
	@Setter @Getter @ApiModelProperty(value="发货日期") private String osp_delivery_date;
	@Setter @Getter @ApiModelProperty(value="状态：01待审核、02主管通过、03主管驳回、04上级通过、05上级驳回、06库管通过、07库管驳回") private String osp_status;
	@Setter @Getter @ApiModelProperty(value="驳回原因") private String osp_reason;
	@Setter @Getter @ApiModelProperty(value="领料人") private String osp_pick_person;
	@Setter @Getter @ApiModelProperty(value="发货人") private String osp_delivery_person;
	@Setter @Getter @ApiModelProperty(value="物料特性") private String mt_feature;
	
	@Setter @Getter 
	@OneToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="osp_delivery_stock",insertable=false,updatable=false) 
	private StorageEntity storageEntity;
}
