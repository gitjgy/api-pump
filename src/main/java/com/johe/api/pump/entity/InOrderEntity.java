package com.johe.api.pump.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="入库单/采购单-实体对象")
@Entity
@Table(name="pump_stockin_order")
public class InOrderEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @Column(name="sin_id",insertable=false,updatable=false) @ApiModelProperty(value="入库单ID") private Long sinid;
	@Setter @Getter @ApiModelProperty(value="单号",required=true) private String sin_sn;
	@Setter @Getter @ApiModelProperty(value="批次号") private String sin_batch_no;
	@Setter @Getter @ApiModelProperty(value="制单日期") private String sin_datetime;
	@Setter @Getter @ApiModelProperty(value="开票方式") private String sin_open_invoice;
	@Setter @Getter @ApiModelProperty(value="部门") private String sin_dept;
	@Setter @Getter @ApiModelProperty(value="摘要") private String sin_summary;
	@Setter @Getter @ApiModelProperty(value="类型：01采购申请、02采购入库、03归还入库、04其他入库、05加工入库",required=true) private String sin_type;
	@Setter @Getter @ApiModelProperty(value="制单机构") private String sin_make_institution;
	@Setter @Getter @ApiModelProperty(value="制单人ID") private long sin_make_person;
	@Setter @Getter @ApiModelProperty(value="审核人") private String sin_audit_person;
	@Setter @Getter @ApiModelProperty(value="状态：01待审核、02主管通过、03主管驳回、04上级通过、05上级驳回、06库管通过、07库管驳回、08审核完成、10库管待审",required=true) private String sin_status;
	@Setter @Getter @ApiModelProperty(value="驳回原因") private String sin_reject_reason;
	@Setter @Getter @ApiModelProperty(value="审核日期") private String sin_audit_date;
	@Setter @Getter @ApiModelProperty(value="记账人") private String sin_tally_person;
	@Setter @Getter @ApiModelProperty(value="验收人") private String sin_acceptor;
	@Setter @Getter @ApiModelProperty(value="保管人") private String sin_keeper;
	@Setter @Getter @ApiModelProperty(value="业务员") private String sin_salesman;
	@Setter @Getter @ApiModelProperty(value="负责人") private String sin_leader;
	@Setter @Getter @ApiModelProperty(value="归还人") private String return_person;
	@Setter @Getter @ApiModelProperty(value="物料特性") private String mt_feature;
	@Setter @Getter @ApiModelProperty(value="收货仓库ID") private long sin_receive_stock;
	@Setter @Getter @ApiModelProperty(value="供应商ID") private long sin_supply;
	
}
