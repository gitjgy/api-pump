package com.johe.api.pump.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="出库单栏目-实体对象")
@Entity
@Table(name="pump_out_stock_pick_order_item")
public class OutOrderItemEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @Column(name="ospitem_id",insertable=false,updatable=false) @ApiModelProperty(value="出库单栏目ID") private long itemid;
	@Setter @Getter @Column(name="osp_id") @ApiModelProperty(value="出库单ID") private long ospid;
	@Setter @Getter @ApiModelProperty(value="商品编码（小类CODE）") private String ospitem_product_code;
	@Setter @Getter @ApiModelProperty(value="商品名称（小类ID）") private long ospitem_product_name;
	@Setter @Getter @ApiModelProperty(value="规格型号") private String ospitem_spec_model;
	@Setter @Getter @ApiModelProperty(value="条形码") private String ospitem_barcode;
	@Setter @Getter @ApiModelProperty(value="物料类别（大类ID）") private long ospitem_category;
	@Setter @Getter @ApiModelProperty(value="商品品牌") private String ospitem_brand;
	@Setter @Getter @ApiModelProperty(value="辅助属性") private String ospitem_assist_attr;
	@Setter @Getter @ApiModelProperty(value="单价") private double ospitem_price;
	@Setter @Getter @ApiModelProperty(value="计量单位") private String ospitem_measure_unit;
	@Setter @Getter @ApiModelProperty(value="金额") private double ospitem_amount=0.00;
	@Setter @Getter @ApiModelProperty(value="应出数量") private double item_qty=0.00;
	@Setter @Getter @ApiModelProperty(value="实出数量") private double ospitem_quantity=0.00;
	@Setter @Getter @ApiModelProperty(value="发货仓库") private long ospitem_delivery_stock;
	@Setter @Getter @ApiModelProperty(value="仓位区ID") private long ospitem_out_bin_area;
	@Setter @Getter @ApiModelProperty(value="仓位架ID") private long ospitem_out_bin_rack;
	@Setter @Getter @ApiModelProperty(value="仓位位ID") private long ospitem_out_bin_pos;
	@Setter @Getter @ApiModelProperty(value="仓位代码") private String ospitem_out_bin_code;
	@Setter @Getter @ApiModelProperty(value="备注") private String ospitem_remark;
	@Setter @Getter @ApiModelProperty(value="物料特性：01普通、02市采、03重要、04关键") private String mt_feature;

	
	@Setter @Getter 
	@OneToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="ospitem_delivery_stock",insertable=false,updatable=false) 
	private StorageEntity storage;
	
	
	@Setter @Getter 
	@OneToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="ospitem_category",insertable=false,updatable=false) 
	private CategoryEntity bigCategoryEntity;
	
	@Setter @Getter 
	@OneToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="ospitem_product_name",insertable=false,updatable=false) 
	private CategoryEntity smallCategoryEntity;
	
	
	
	//
//	@Setter @Getter 
//	@OneToOne() 
//	@JoinColumn(name="osp_id",insertable=false,updatable=false) 
//	private OutOrderEntity stockInOrder;
	
	/*@Setter @Getter 
	@OneToOne() 
	@JoinColumn(name="ospitem_out_bin_area",insertable=false,updatable=false) 
	private StockBinEntity stockbin_area;
	
	@Setter @Getter 
	@OneToOne() 
	@JoinColumn(name="ospitem_out_bin_rack",insertable=false,updatable=false) 
	private StockBinEntity stockbin_rack;
	
	@Setter @Getter 
	@OneToOne() 
	@JoinColumn(name="ospitem_out_bin_pos",insertable=false,updatable=false) 
	private StockBinEntity stockbin_pos*/;
}
