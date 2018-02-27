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

@ApiModel(value="供应商-实体对象")
@Entity
@Table(name="pump_supplier")
public class SupplierEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @ApiModelProperty(value="供应商ID") private Long sup_id;
	@Setter @Getter @ApiModelProperty(value="供应商名称") private String sup_name;
//	@Setter @Getter @ApiModelProperty(value="供应商简称") private String sup_short;
//	@Setter @Getter @ApiModelProperty(value="供应商代码") private String sup_code;
}
