package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="盘点记录-数据传输对象")
public class CheckRecordDto {
//	@Setter @Getter @ApiModelProperty(value="盘点时间") private String check_time;
//	@Setter @Getter @ApiModelProperty(value="物料代码（小类CODE）") private String mt_code;
	@Setter @Getter @ApiModelProperty(value="物料全名（大类名称+'-'+小类名称）") private String mt_fullname;
	@Setter @Getter @ApiModelProperty(value="条形码（限18字符）",example="例：007001001A01010001") private String bar_code;
//	@Setter @Getter @ApiModelProperty(value="物料编码（限16字符）",example="例：007001001A0101") private String mt_encode;
//	@Setter @Getter @ApiModelProperty(value="大类代码（限3字符）") private String big_code;
//	@Setter @Getter @ApiModelProperty(value="大类名称（限32字符）") private String big_name;
//	@Setter @Getter @ApiModelProperty(value="小类代码（限7字符）") private String small_code;
//	@Setter @Getter @ApiModelProperty(value="小类名称（限32字符）") private String small_name;
	
	@Setter @Getter @ApiModelProperty(value="小类ID）") private long small_id;
	@Setter @Getter @ApiModelProperty(value="大类ID") private long big_id;	
	
	
	@Setter @Getter @ApiModelProperty(value="仓位代码（限7字符）") private String sbin_code;
	@Setter @Getter @ApiModelProperty(value="仓库名称（限32字符）") private String storage_name;
	@Setter @Getter @ApiModelProperty(value="仓库代码（限3字符）") private String storage_code;
	@Setter @Getter @ApiModelProperty(value="盘点人姓名（限32字符）") private String person_name;
	@Setter @Getter @ApiModelProperty(value="盘点人ID（限20字符）") private long person_id;
	@Setter @Getter @ApiModelProperty(value="计量单位（限32字符）") private String mea_unit;
	@Setter @Getter @ApiModelProperty(value="期初") private double init_qty;
	@Setter @Getter @ApiModelProperty(value="入库") private double in_stock_qty;
	@Setter @Getter @ApiModelProperty(value="出库") private double out_stock_qty;
	@Setter @Getter @ApiModelProperty(value="库存") private double stock_qty;
	@Setter @Getter @ApiModelProperty(value="实盘") private double check_qty;
	@Setter @Getter @ApiModelProperty(value="盈亏") private String pl_qty;
	@Setter @Getter @ApiModelProperty(value="类型:01手动盘点、02扫描盘点",allowableValues="01,02") private String check_type;
	@Setter @Getter @ApiModelProperty(value="备注（限128字符）") private String remark;
}
