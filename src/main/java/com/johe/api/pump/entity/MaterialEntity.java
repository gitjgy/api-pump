package com.johe.api.pump.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="物料-实体对象")
@Entity
@Table(name="pump_material")
public class MaterialEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @Column(name="mt_id") @ApiModelProperty(value="物料ID") private Long materialid;
	@Setter @Getter @ApiModelProperty(value="物料代码") private String mt_code;
	@Setter @Getter @ApiModelProperty(value="物料属性") private String mt_attr;
	@Setter @Getter @Column(name="mt_barcode") @ApiModelProperty(value="物料条码（条形码）") private String barcode;
	@Setter @Getter @ApiModelProperty(value="物料名称") private String mt_name;
	@Setter @Getter @ApiModelProperty(value="物料全名") private String mt_fullname;
	@Setter @Getter @ApiModelProperty(value="物料特性：01普通、02市采、03重要、04关键") private String mt_feature;
	@Setter @Getter @ApiModelProperty(value="计量单位") private String mt_measure_unit;
	@Setter @Getter @ApiModelProperty(value="在库余量（物料仓位数量）") private long mt_in_remain_quantity;
	@Setter @Getter @ApiModelProperty(value="在库总量（物料在库总量）") private long mt_in_total_quantity;
	@Setter @Getter @ApiModelProperty(value="最低存量") private long mt_min_quantity;
	@Setter @Getter @ApiModelProperty(value="最高存量") private long mt_max_quantity;
	@Setter @Getter @ApiModelProperty(value="物料状态：01在库、02借出、03使用、04报废") private String mt_status;
	@Setter @Getter @ApiModelProperty(value="库存状态：01正常、02短缺、03超储") private String mt_stock_status;
	@Setter @Getter @ApiModelProperty(value="大类ID") private long mt_category_big;
	@Setter @Getter @ApiModelProperty(value="小类ID") private long mt_category_small;
	@Setter @Getter @ApiModelProperty(value="仓库ID") private long stg_id;
	@Setter @Getter @ApiModelProperty(value="仓位区ID") private long sbin_area_id;
	@Setter @Getter @ApiModelProperty(value="仓位架ID") private long sbin_rack_id;
	@Setter @Getter @ApiModelProperty(value="仓位位ID") private long sbin_pos_id;
	@Setter @Getter @ApiModelProperty(value="供应商ID") private long sup_id;
	@Setter @Getter @Column(name="mt_assist_attr") @ApiModelProperty(value="物料单价") private String price;
	
	@Setter @Getter 
	@ManyToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="sup_id",insertable=false,updatable=false) 
	private SupplierEntity supplier;
	
	@Setter @Getter 
	@ManyToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="stg_id",insertable=false,updatable=false) 
	private StorageEntity storage;
	
	@Setter @Getter 
	@ManyToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="sbin_area_id",insertable=false,updatable=false) 
	private StockBinEntity stockbin_area;
	
	@Setter @Getter 
	@ManyToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="sbin_rack_id",insertable=false,updatable=false) 
	private StockBinEntity stockbin_rack;
	
	@Setter @Getter 
	@ManyToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="sbin_pos_id",insertable=false,updatable=false) 
	private StockBinEntity stockbin_pos;
	
	@Setter @Getter 
	@ManyToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="mt_category_big",insertable=false,updatable=false) 
	private CategoryEntity category_big;
	
	@Setter @Getter 
	@ManyToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="mt_category_small",insertable=false,updatable=false) 
	private CategoryEntity category_small;
}
