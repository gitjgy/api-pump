package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="物料盘点数据-数据传输对象")
public class MaterialCheckDto {
	@Setter @Getter @ApiModelProperty(value="物料编码（限18字符）",example="例：007001001A01010001") private String mt_encode;
	@Setter @Getter @ApiModelProperty(value="品名（大类名称+小类名称）") private String mt_fullname;
//	@Setter @Getter @ApiModelProperty(value="条形码",example="例：007001001A.01.01") private String bar_code;
	@Setter @Getter @ApiModelProperty(value="大类代码") private String big_code;
	@Setter @Getter @ApiModelProperty(value="大类名称") private String big_name;
	@Setter @Getter @ApiModelProperty(value="小类代码") private String small_code;
	@Setter @Getter @ApiModelProperty(value="小类名称") private String small_name;
	@Setter @Getter @ApiModelProperty(value="仓位代码") private String sbin_code;
	@Setter @Getter @ApiModelProperty(value="仓位-区代码") private String area_code;
//	@Setter @Getter @ApiModelProperty(value="仓位-区名称") private String area_name;
	@Setter @Getter @ApiModelProperty(value="仓位-架代码") private String rack_code;
//	@Setter @Getter @ApiModelProperty(value="仓位-架名称") private String rack_name;
	@Setter @Getter @ApiModelProperty(value="仓位-位代码") private String pos_code;
//	@Setter @Getter @ApiModelProperty(value="仓位-位名称") private String pos_name;
	@Setter @Getter @ApiModelProperty(value="仓库名称") private String storage_name;
	@Setter @Getter @ApiModelProperty(value="仓库代码") private String storage_code;
	@Setter @Getter @ApiModelProperty(value="盘点人姓名") private String person_name;
	
	@Setter @Getter @ApiModelProperty(value="小类ID") private long small_id;
	@Setter @Getter @ApiModelProperty(value="大类ID") private long big_id;
	
	
	
	@Setter @Getter @ApiModelProperty(value="计量单位") private String mea_unit;
	@Setter @Getter @ApiModelProperty(value="期初") private long init_qty;
	@Setter @Getter @ApiModelProperty(value="入库") private long in_stock_qty;
	@Setter @Getter @ApiModelProperty(value="出库") private long out_stock_qty;
	@Setter @Getter @ApiModelProperty(value="库存") private long stock_qty;
	@Setter @Getter @ApiModelProperty(value="实盘") private long check_qty;
	@Setter @Getter @ApiModelProperty(value="盈亏") private String pl_qty;
}
