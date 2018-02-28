package com.johe.api.pump.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="系统用户-实体对象")
@Entity
@Table(name="pump_department")
public class DepartmentEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter @Getter @ApiModelProperty(value="部门ID") private long dept_id;
	@Setter @Getter @ApiModelProperty(value="部门名称") private String dept_name;
	@Setter @Getter @ApiModelProperty(value="部门代码") private String dept_code;
	
//	@OneToMany(cascade= {CascadeType.ALL},mappedBy="dept")
//	@Setter @Getter @ApiModelProperty(value="部门下用户") 
//	private Set<SysUserEntity> sysUser = new HashSet<SysUserEntity>();
}
