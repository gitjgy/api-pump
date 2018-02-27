package com.johe.api.pump.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="历史预警-实体对象")
@Entity
@Table(name="pump_history_alarm")
public class HistoryAlarmEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @ApiModelProperty(value="历史ID") private Long his_id;
	@Setter @Getter @ApiModelProperty(value="物料条形码") private String material_barcode;
	@Setter @Getter @ApiModelProperty(value="物料名称") private String material_name;
	@Setter @Getter @ApiModelProperty(value="物料大类") private String big_name;
	@Setter @Getter @ApiModelProperty(value="物料小类") private String small_name;
	@Setter @Getter @ApiModelProperty(value="仓库名称") private String storage_name;
	@Setter @Getter @ApiModelProperty(value="仓位名称") private String sbin_name;
	@Setter @Getter @ApiModelProperty(value="仓位CODE") private String sbin_code;
	@Setter @Getter @ApiModelProperty(value="预警时间") private String alarm_time;
	@Setter @Getter @ApiModelProperty(value="预警存量") private double alarm_qty;
	@Setter @Getter @ApiModelProperty(value="预警前存量") private double alarm_qty_before;
	@Setter @Getter @ApiModelProperty(value="最低存量") private double min_qty;
	@Setter @Getter @ApiModelProperty(value="最高存量") private double max_qty;
	@Setter @Getter @ApiModelProperty(value="状态：短缺、超储") private String status;
	@Setter @Getter @ApiModelProperty(value="计量单位") private String mea_unit_name;
	
}
