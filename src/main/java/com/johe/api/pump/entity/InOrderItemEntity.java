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

@ApiModel(value="入库单栏目-实体对象")
@Entity
@Table(name="pump_stockin_order_item")
public class InOrderItemEntity {
	@Id
	@GeneratedValue
	@Setter @Getter @Column(name="siitem_id",insertable=false,updatable=false) @ApiModelProperty(value="入库单栏目ID") private long itemid;
	@Setter @Getter @Column(name="sin_id") @ApiModelProperty(value="入库单ID") private long sinid;
	@Setter @Getter @Column(name="siitem_product_code") @ApiModelProperty(value="商品编码") private String product_code;
	@Setter @Getter @Column(name="siitem_product_name") @ApiModelProperty(value="商品名称（小类ID）") private long product_name;
	@Setter @Getter @Column(name="siitem_spec_model") @ApiModelProperty(value="规格型号") private String spec_model;
	@Setter @Getter @Column(name="siitem_barcode") @ApiModelProperty(value="条形码") private String barcode;
	@Setter @Getter @Column(name="siitem_category") @ApiModelProperty(value="物料类别（大类ID）") private long category;
	@Setter @Getter @Column(name="siitem_brand") @ApiModelProperty(value="商品品牌") private String brand;
	@Setter @Getter @Column(name="siitem_supply") @ApiModelProperty(value="供应商ID") private long supply;
	@Setter @Getter @Column(name="siitem_price") @ApiModelProperty(value="单价") private double price=0.00;
	@Setter @Getter @Column(name="siitem_measure_unit") @ApiModelProperty(value="计量单位") private String measure_unit;
	@Setter @Getter @Column(name="siitem_amount") @ApiModelProperty(value="金额") private double amount=0.00;
	@Setter @Getter @Column(name="siitem_quantity_recv") @ApiModelProperty(value="应入数量") private double item_qty=0.00;
	@Setter @Getter @Column(name="siitem_actual_quantity_recv") @ApiModelProperty(value="实入数量") private double act_qty_recv=0.00;
	@Setter @Getter @Column(name="siitem_open_invoice") @ApiModelProperty(value="开票方式") private String open_invoice;
	@Setter @Getter @Column(name="siitem_receipt_stock") @ApiModelProperty(value="收货（料）仓库ID") private long receipt_stock;
	@Setter @Getter @Column(name="siitem_stockbin_area") @ApiModelProperty(value="仓位区ID") private long stock_bin_area;
	@Setter @Getter @Column(name="siitem_stockbin_rack") @ApiModelProperty(value="仓位架ID") private long stock_bin_rack;
	@Setter @Getter @Column(name="siitem_stockbin_pos") @ApiModelProperty(value="仓位位ID") private long stock_bin_pos;
	@Setter @Getter @Column(name="siitem_stockbin_code") @ApiModelProperty(value="仓位代码") private String stock_bin_code;
	@Setter @Getter @Column(name="mt_feature") @ApiModelProperty(value="物料特性：01普通、02市采、03重要、04关键") private String mt_feature;
	@Setter @Getter @Column(name="siitem_remark") @ApiModelProperty(value="备注") private String remark;
	
	@Setter @Getter @Column(name="siitem_assist_attr") @ApiModelProperty(value="辅助属性") private String assist_attr;
	@Setter @Getter @Column(name="siitem_unit_cost") @ApiModelProperty(value="单位成本") private double unit_cost=0.00;
	@Setter @Getter @Column(name="siitem_buy_date") @ApiModelProperty(value="采购日期") private String buy_date;
	@Setter @Getter @Column(name="siitem_receive_date") @ApiModelProperty(value="收料日期") private String recv_date;
	@Setter @Getter @Column(name="siitem_expiration_date") @ApiModelProperty(value="保质期") private String expir_date;
	@Setter @Getter @Column(name="siitem_valid_until") @ApiModelProperty(value="有效期至") private String valid_unti;
	@Setter @Getter @Column(name="siitem_receipt_total") @ApiModelProperty(value="收料总仓") private String receipt_total;
	

	@Setter @Getter 
	@OneToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="siitem_receipt_stock",insertable=false,updatable=false) 
	private StorageEntity storage;
	
	
	@Setter @Getter 
	@OneToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="siitem_category",insertable=false,updatable=false) 
	private CategoryEntity bigCategoryEntity;
	
	@Setter @Getter 
	@OneToOne(fetch=FetchType.EAGER) 
	@JoinColumn(name="siitem_product_name",insertable=false,updatable=false) 
	private CategoryEntity smallCategoryEntity;
	
//	@Setter @Getter 
//	@OneToOne() 
//	@JoinColumn(name="sin_id",insertable=false,updatable=false) 
//	private InOrderEntity stockInOrder;
	
//	@Setter @Getter 
//	@OneToOne(fetch=FetchType.EAGER) 
//	@JoinColumn(name="siitem_stockbin_area",insertable=false,updatable=false) 
//	private StockBinEntity areaEntity;
//	
//	@Setter @Getter 
//	@OneToOne(fetch=FetchType.EAGER) 
//	@JoinColumn(name="siitem_stockbin_rack",insertable=false,updatable=false) 
//	private StockBinEntity rackEntity;
//	
//	@Setter @Getter 
//	@OneToOne(fetch=FetchType.EAGER) 
//	@JoinColumn(name="siitem_stockbin_pos",insertable=false,updatable=false) 
//	private StockBinEntity posEntity;
}
