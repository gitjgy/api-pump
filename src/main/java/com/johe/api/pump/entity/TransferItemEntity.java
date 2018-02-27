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

@ApiModel(value="调拨单栏目-实体对象")
@Entity
@Table(name="pump_transfer_order_item")
public class TransferItemEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @Column(name="tranitem_id",insertable=false,updatable=false) @ApiModelProperty(value="调拨单栏目ID") private long itemid;
	@Setter @Getter @Column(name="tran_id") @ApiModelProperty(value="调拨单ID") private long tranid;
	@Setter @Getter @ApiModelProperty(value="商品编码") private String tranitem_product_code;
	@Setter @Getter @ApiModelProperty(value="商品名称（小类ID）") private long tranitem_product_name;
	@Setter @Getter @ApiModelProperty(value="规格型号") private String tranitem_spec_model;
	@Setter @Getter @ApiModelProperty(value="条形码") private String tranitem_barcode;
	@Setter @Getter @ApiModelProperty(value="物料类别（大类ID）") private long tranitem_category;
	@Setter @Getter @ApiModelProperty(value="商品品牌") private String tranitem_brand;
	@Setter @Getter @ApiModelProperty(value="单价") private double tranitem_price;
	@Setter @Getter @ApiModelProperty(value="计量单位") private String tranitem_measure_unit;
	@Setter @Getter @ApiModelProperty(value="金额") private double tranitem_amount;
	@Setter @Getter @ApiModelProperty(value="应调数量") private double item_qty;
	@Setter @Getter @ApiModelProperty(value="实调数量") private double tranitem_quantity;
	@Setter @Getter @ApiModelProperty(value="调入仓库ID") private long tranitem_in_stock;
	@Setter @Getter @ApiModelProperty(value="调入仓位区") private long tranitem_in_bin_area;
	@Setter @Getter @ApiModelProperty(value="调入仓位架") private long tranitem_in_bin_rack;
	@Setter @Getter @ApiModelProperty(value="调入仓位位") private long tranitem_in_bin_pos;
	@Setter @Getter @ApiModelProperty(value="调入仓位代码") private String tranitem_in_bin_code;
	@Setter @Getter @ApiModelProperty(value="调出仓库ID") private long tranitem_out_stock;
	@Setter @Getter @ApiModelProperty(value="调出仓位区") private long tranitem_out_bin_area;
	@Setter @Getter @ApiModelProperty(value="调出仓位架") private long tranitem_out_bin_rack;
	@Setter @Getter @ApiModelProperty(value="调出仓位位") private long tranitem_out_bin_pos;
	@Setter @Getter @ApiModelProperty(value="调出仓位代码") private String tranitem_out_bin_code;
	@Setter @Getter @ApiModelProperty(value="备注") private String tranitem_remark;
	@Setter @Getter @ApiModelProperty(value="物料特性：01普通、02市采、03重要、04关键") private String mt_feature;

	/*@Setter @Getter 
	@ManyToOne(targetEntity=TransferOrderEntity.class) 
	@JoinColumn(name="tran_id",insertable=false,updatable=false) 
	private TransferOrderEntity transferOrder;
	
//	@JoinColumn(name="tranitem_out_bin_area",referencedColumnName="sbin_id",table="pump_stock_bin") 
	@Setter @Getter 
	@ManyToOne(targetEntity=StockBinEntity.class) 
	@JoinColumn(name="tranitem_out_bin_area",insertable=false,updatable=false) 
	private StockBinEntity in_stockbin_area;*/
	
	@Setter @Getter 
	@OneToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="tranitem_in_stock",insertable=false,updatable=false) 
	private StorageEntity inStorage;
	
	@Setter @Getter 
	@OneToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="tranitem_out_stock",insertable=false,updatable=false) 
	private StorageEntity outStorage;
	
	
	@Setter @Getter 
	@OneToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="tranitem_category",insertable=false,updatable=false) 
	private CategoryEntity bigCategoryEntity;
	
	@Setter @Getter 
	@OneToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="tranitem_product_name",insertable=false,updatable=false) 
	private CategoryEntity smallCategoryEntity;
	
}
