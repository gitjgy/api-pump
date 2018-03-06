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

@ApiModel(value="单子编号-实体对象")
@Entity
@Table(name="t_sys_seq_number")
public class SeqNumberEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @ApiModelProperty(value="主键ID") @Column(name="seq_id",insertable=false,updatable=false) private long seqid;
	@Setter @Getter @ApiModelProperty(value="前缀") @Column(name="biz_type",insertable=false,updatable=false)  private String btype;
	@Setter @Getter @ApiModelProperty(value="年月日时分秒") private String ymdhms;
	@Setter @Getter @ApiModelProperty(value="当前值")  private String cur_value;
	@Setter @Getter @ApiModelProperty(value="当前日期")  private String cur_date;
	@Setter @Getter @ApiModelProperty(value="说明")  private String remarks;
}
