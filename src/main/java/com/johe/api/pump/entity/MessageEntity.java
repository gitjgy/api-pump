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

@ApiModel(value="消息管理-实体对象")
@Entity
@Table(name="pump_message")
public class MessageEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @Column(name="msg_id") @ApiModelProperty(value="消息ID") private Long msgid;
	@Setter @Getter @ApiModelProperty(value="消息内容") private String msg_content;
	@Setter @Getter @ApiModelProperty(value="审核职位") private String audit_post;
	@Setter @Getter @ApiModelProperty(value="发送时间") private String send_time;
	@Setter @Getter @ApiModelProperty(value="业务类型：01采购申请、02加工入库、03其他入库（采购入库、归还入库、其他入库）、04出库（借用出库、领料出库、加工出库）、05调拨") private String biz_type;
	@Setter @Getter @ApiModelProperty(value="审核记录") private String audit_records;
	@Setter @Getter @ApiModelProperty(value="创建人ID") private long msg_creator;
	@Setter @Getter @ApiModelProperty(value="创建时间") private String cre_time;
	@Setter @Getter @ApiModelProperty(value="完成时间") private String finish_time;
	@Setter @Getter @ApiModelProperty(value="关联ID") private long relate_id;
}
