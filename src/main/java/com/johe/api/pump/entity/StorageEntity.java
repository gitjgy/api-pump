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

@ApiModel(value="仓库-实体对象")
@Entity
@Table(name="pump_storage")
public class StorageEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @ApiModelProperty(value="仓库ID") private long stg_id;
	@Setter @Getter @ApiModelProperty(value="仓库名称") private String stg_name;
	@Setter @Getter @ApiModelProperty(value="仓库代码") private String stg_code;
	
//	@Setter @Getter @ApiModelProperty(value="仓库物料") 
//	@OneToMany(mappedBy="storage",fetch=FetchType.LAZY)
//	private MaterialEntity material;
	
}
