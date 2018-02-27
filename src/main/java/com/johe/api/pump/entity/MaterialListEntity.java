package com.johe.api.pump.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="物料-实体对象")
@Entity
@Table(name="pump_material")
public class MaterialListEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @ApiModelProperty(value="物料ID") private long mt_id;
	@Setter @Getter @ApiModelProperty(value="条形码") private String mt_barcode;
	@Setter @Getter @ApiModelProperty(value="物料名称") private String mt_name;//小类名称
	@Setter @Getter @ApiModelProperty(value="物料全名") private String mt_fullname;//大类名称
	@Setter @Getter @ApiModelProperty(value="计量单位") private String mt_measure_unit;
	@Setter @Getter @ApiModelProperty(value="物料代码") private String mt_code;
	@Setter @Getter @ApiModelProperty(value="物料特性") private String mt_feature;
//	@Setter @Getter @ApiModelProperty(value="仓库ID") private long stg_id;
	@Setter @Getter @ApiModelProperty(value="大类ID") private long mt_category_big;
	@Setter @Getter @ApiModelProperty(value="小类ID") private long mt_category_small;
//	@Setter @Getter @ApiModelProperty(value="仓位位ID") private long sbin_pos_id;
	
}
