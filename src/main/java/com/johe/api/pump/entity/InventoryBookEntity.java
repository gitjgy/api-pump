package com.johe.api.pump.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="库存台账-实体对象")
@Entity
@Table(name="pump_inventory_book")
public class InventoryBookEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @ApiModelProperty(value="台账ID") private long ib_id;
	@Setter @Getter @ApiModelProperty(value="物料ID") private long mt_id;
	@Setter @Getter @ApiModelProperty(value="台账类型") private String ib_type;
	@Setter @Getter @ApiModelProperty(value="入库单ID 或 出库单ID 或 调拨单ID") private long order_id;
	@Setter @Getter @ApiModelProperty(value="栏目ID") private long item_id;
	@Setter @Getter @ApiModelProperty(value="出入库类型") private String in_out_type;
	@Setter @Getter @ApiModelProperty(value="出入库日期") private String cre_date;
	@Setter @Getter @ApiModelProperty(value="期初结存") private double last_qty;
	@Setter @Getter @ApiModelProperty(value="出入库数量") private double in_out_qty;
	@Setter @Getter @ApiModelProperty(value="当期结存") private double cur_qty;

}
