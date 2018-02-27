package com.johe.api.pump.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="系统用户-实体对象")
@Entity
@Table(name="t_sys_user")
public class SysUserEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter @Getter @ApiModelProperty(value="用户ID") private long user_id;
	@Setter @Getter @ApiModelProperty(value="登录账号") private String account;
	@Setter @Getter @ApiModelProperty(hidden=true) private String pwd;
	@Setter @Getter @ApiModelProperty(value="用户名称") private String user_name;
	@Setter @Getter @ApiModelProperty(value="更新时间") private String upt_time;
	@Setter @Getter @ApiModelProperty(value="单位ID") private String org_id;
	@Setter @Getter @ApiModelProperty(value="部门ID") private String cert_no;
	@Setter @Getter @Column(name="worker_no") @ApiModelProperty(value="职位") private String wkno;
	
	@Setter @Getter 
	@ManyToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="CERT_NO",insertable=false,updatable=false) 
    @ApiModelProperty(value="所属部门")
	private DepartmentEntity dept;
	
}
