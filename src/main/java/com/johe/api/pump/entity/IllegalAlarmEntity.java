package com.johe.api.pump.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="非法出库预警-实体对象")
@Entity
@Table(name="pump_illegal_alarm")
public class IllegalAlarmEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @ApiModelProperty(value="非法预警ID") private long alarm_id;
	@Setter @Getter @ApiModelProperty(value="条形码") private String barcode;
	@Setter @Getter @ApiModelProperty(value="请求ID") private String req_id;
	@Setter @Getter @ApiModelProperty(value="请求时间") private String req_time;
	@Setter @Getter @ApiModelProperty(value="操作人") private String opt_person;
	@Setter @Getter @ApiModelProperty(value="设备编号") private String eqp_sn;
	@Setter @Getter @ApiModelProperty(value="开门时间") private String open_time;
	@Setter @Getter @ApiModelProperty(value="关门时间") private String close_time;
	@Setter @Getter @ApiModelProperty(value="操作时间") private String opt_time;
	@Setter @Getter @ApiModelProperty(value="创建时间") private String cre_time;

}
