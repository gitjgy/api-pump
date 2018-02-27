package com.johe.api.pump.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="审核记录-实体对象")
@Entity
@Table(name="pump_audit_records")
public class AuditRecordEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @ApiModelProperty(value="审核记录ID") private Long audit_id;
	@Setter @Getter @ApiModelProperty(value="审核时间") private String audit_time;
	@Setter @Getter @ApiModelProperty(value="审核人") private long audit_person;
	@Setter @Getter @ApiModelProperty(value="审核节点：01待审核、02主管通过、03主管驳回、04上级通过、05上级驳回、06库管通过、07库管驳回") private String audit_node;
	@Setter @Getter @ApiModelProperty(value="审核意见") private String audit_suggestion;
	@Setter @Getter @ApiModelProperty(value="业务类型：01采购申请、02采购入库、03归还入库、04加工入库、05其它入库、06调拨单、07领料出库") private String biz_type;
	@Setter @Getter @ApiModelProperty(value="关联ID") private long relate_id;
	
//	@Setter @Getter @ApiModelProperty(value="仓库物料") 
//	@OneToMany(mappedBy="storage",fetch=FetchType.LAZY)
//	private MaterialEntity material;
	
}
