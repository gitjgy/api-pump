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

@ApiModel(value="物料类别-实体对象")
@Entity
@Table(name="pump_material_category")
public class CategoryEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @ApiModelProperty(value="类别ID") @Column(name="category_id") private long id;
	@Setter @Getter @ApiModelProperty(value="父类别ID") @Column(name="parent_category_id") private long parentid;
	@Setter @Getter @ApiModelProperty(value="类别代码") @Column(name="category_code") private String code;
	@Setter @Getter @ApiModelProperty(value="类别名称") @Column(name="category_name") private String name;
	@Setter @Getter @ApiModelProperty(value="规格型号") @Column(name="spec_model") private String model;
	@Setter @Getter @ApiModelProperty(value="排序索引") @Column(name="sort_index") private int sort;
	@Setter @Getter @ApiModelProperty(value="状态") private String status;
	@Setter @Getter @ApiModelProperty(value="计量单位CODE") private String mea_unit_code;
	@Setter @Getter @ApiModelProperty(value="最低存量") private double mt_min_quantity;
	@Setter @Getter @ApiModelProperty(value="最高存量") private double mt_max_quantity;
}
