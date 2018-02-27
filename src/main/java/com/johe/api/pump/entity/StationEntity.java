package com.johe.api.pump.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="领料地点（泵站）-实体对象")
@Entity
@Table(name="pump_station")
public class StationEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @ApiModelProperty(value="泵站ID")  private long pump_id;
	@Setter @Getter @ApiModelProperty(value="泵站代码")  private String pump_code;
	@Setter @Getter @ApiModelProperty(value="泵站全名")  private String pump_name;
	@Setter @Getter @ApiModelProperty(value="泵站简称")  private String pump_short_name;
	@Setter @Getter @ApiModelProperty(value="状态")  private String pump_status;
}
