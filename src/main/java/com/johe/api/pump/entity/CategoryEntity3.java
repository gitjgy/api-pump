package com.johe.api.pump.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value="物料小类-实体对象")
@Data
public class CategoryEntity3 implements Serializable {

	private static final long serialVersionUID = 5586881338575643766L;
	private long id;
	private long parentid;
	private String code;
	private String name;
	private String model;
	private String mea_unit_name;
	
}
