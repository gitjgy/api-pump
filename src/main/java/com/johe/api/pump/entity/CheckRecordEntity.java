package com.johe.api.pump.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="盘点记录-实体对象")
@Entity
@Table(name="pump_check_records")
public class CheckRecordEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter @Getter @ApiModelProperty(value="盘点记录ID")  @Column(name="check_id") private long checkid;
	@Setter @Getter @ApiModelProperty(value="盘点时间") private String check_time;
	@Setter @Getter @ApiModelProperty(value="物料代码") private String mt_code;
	@Setter @Getter @ApiModelProperty(value="物料全名") private String mt_fullname;
	@Setter @Getter @ApiModelProperty(value="条形码") private String bar_code;
	@Setter @Getter @ApiModelProperty(value="物料编码") private String mt_encode;
	@Setter @Getter @ApiModelProperty(value="大类代码") private String big_code;
	@Setter @Getter @ApiModelProperty(value="大类名称") private String big_name;
	@Setter @Getter @ApiModelProperty(value="小类代码") private String small_code;
	@Setter @Getter @ApiModelProperty(value="小类名称") private String small_name;
	@Setter @Getter @ApiModelProperty(value="仓位代码") private String sbin_code;
	@Setter @Getter @ApiModelProperty(value="仓库名称") private String storage_name;
	@Setter @Getter @ApiModelProperty(value="仓库代码") private String storage_code;
	@Setter @Getter @ApiModelProperty(value="盘点人姓名") private String person_name;
	@Setter @Getter @ApiModelProperty(value="盘点人ID") private long person_id;
	@Setter @Getter @ApiModelProperty(value="计量单位") private String mea_unit;
	@Setter @Getter @ApiModelProperty(value="期初") private double init_qty;
	@Setter @Getter @ApiModelProperty(value="入库") private double in_stock_qty;
	@Setter @Getter @ApiModelProperty(value="出库") private double out_stock_qty;
	@Setter @Getter @ApiModelProperty(value="库存") private double stock_qty;
	@Setter @Getter @ApiModelProperty(value="实盘") private double check_qty;
	@Setter @Getter @ApiModelProperty(value="盈亏") private String pl_qty;
	@Setter @Getter @ApiModelProperty(value="类型:01终端手动、02终端扫描、03电脑手动、04电脑扫描") private String check_type;
	@Setter @Getter @ApiModelProperty(value="备注") private String remark;
	
}
