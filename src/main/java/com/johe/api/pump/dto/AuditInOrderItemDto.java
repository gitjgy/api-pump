package com.johe.api.pump.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="审核入库单栏目-数据传输对象")
@Data
public class AuditInOrderItemDto {

	@ApiModelProperty(value="入库单ID",required=true) private long sin_id;
	@ApiModelProperty(value="商品编码：大类code+小类code",example="例如：AJ0203",required=true) private String product_code;
	@ApiModelProperty(value="条形码",example="例如：001002001A01010001",required=true) private String barcode;//更新字段
	@ApiModelProperty(value="实入数量",required=true) private double act_qty_recv=0.00;// 更新字段
	@ApiModelProperty(value="收料仓库ID",required=true) private long recv_st_id;
	@ApiModelProperty(value="仓位代码(区CODE+架CODE+位CODE)",required=true) private String stockbin_code;
	
	@ApiModelProperty(value="仓区ID",required=true) private long area_id;
	@ApiModelProperty(value="仓架ID",required=true) private long rack_id;
	@ApiModelProperty(value="仓位ID",required=true) private long pos_id;
	@ApiModelProperty(value="大类ID",required=true) private long big_id;
	@ApiModelProperty(value="小类ID",required=true) private long small_id;
	@ApiModelProperty(value="供应商ID") private long supply_id;
	@ApiModelProperty(value="计量单位",required=true) private String mea_unit;
	@ApiModelProperty(value="物料特性",required=true) private String mt_feature;
	@ApiModelProperty(value="物料名称",required=true) private String mt_name;
	@ApiModelProperty(value="物料全名",required=true) private String mt_fullname;
	@ApiModelProperty(value="最低存量",required=true) private double min_qty=0.00;
	@ApiModelProperty(value="最高存量",required=true) private double max_qty=0.00;
	
}
