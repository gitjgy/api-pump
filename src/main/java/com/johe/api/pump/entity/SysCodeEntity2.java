package com.johe.api.pump.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="计量单位-实体对象")
@Entity
@Table(name="t_sys_code")
public class SysCodeEntity2 {
	@Id
	@GeneratedValue
	@Setter @Getter @ApiModelProperty(value="计量单位ID") private Long code_id;
	@Setter @Getter @ApiModelProperty(value="计量单位代码") private String code;
	@Setter @Getter @ApiModelProperty(value="计量单位名称") private String code_name;
	@Setter @Getter @ApiModelProperty(value="资源类型") @Column(name="FIELD") private String src_type;
	
//	@Setter @Getter 
//	@OneToOne(mappedBy="meaUnitEntity",fetch=FetchType.EAGER,cascade= {CascadeType.ALL})
//	private CategoryEntity2 categoryEntity = new CategoryEntity2();
}
