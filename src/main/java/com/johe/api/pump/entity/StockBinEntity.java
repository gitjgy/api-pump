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

@ApiModel(value="仓位-实体对象")
@Entity
@Table(name="pump_stock_bin")
public class StockBinEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @ApiModelProperty(value="仓位ID")  private long sbin_id;
	@Setter @Getter @ApiModelProperty(value="父ID")  private long parent_sbin_id;
	@Setter @Getter @ApiModelProperty(value="所属仓库ID")  private long stg_id;
	@Setter @Getter @ApiModelProperty(value="名称") private String sbin_name;
	@Setter @Getter @ApiModelProperty(value="代码") private String sbin_code;
	@Setter @Getter @ApiModelProperty(value="全名") private String sbin_fullname;
	@Setter @Getter @ApiModelProperty(value="类型：01区、02架、03位")private String sbin_type;
}
