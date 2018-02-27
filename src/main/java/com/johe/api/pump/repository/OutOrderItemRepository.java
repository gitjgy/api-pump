package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.entity.OutOrderItemEntity;

public interface OutOrderItemRepository
		extends JpaRepository<OutOrderItemEntity, Long>, JpaSpecificationExecutor<OutOrderItemEntity> {

	// 详情
	OutOrderItemEntity findByOspidAndItemid(long ospid, long itemid);

	// 出库审核（出库单ID、条形码）
	@Query(value = "SELECT oi FROM OutOrderItemEntity oi WHERE oi.ospid=?1 AND oi.ospitem_barcode=?2")
	OutOrderItemEntity getByOspidAndBarcode(long ospid, String barcode);

	
	@Query(value = "SELECT count(*) FROM pump_out_stock_pick_order_item WHERE ospitem_product_code=?1 AND ospitem_barcode=?2 AND ospitem_delivery_stock=?3 AND ospitem_out_bin_code=?4", nativeQuery = true)
	public long getItemByCategoryAndStgAndSbin(String productCode, String barCode, String outStgId, String outBinCode);
	
	// 上传非法出库数据时，校验物料条码是否为非法出库；记录数>0，则认为此物料（条码）非法出库
	// 库管审核完成后，3个小时后出库的物料，就认为是非法出库，则预警提示
	@Query(value = "SELECT COUNT(*) FROM pump_out_stock_pick_order p "
			+ "LEFT JOIN pump_out_stock_pick_order_item it ON it.osp_id=p.osp_id "
			+ "WHERE p.osp_status='08' AND it.ospitem_barcode=?1 "
			+ "AND DATE_ADD(p.osp_audit_date, INTERVAL 3 HOUR) < ?2", nativeQuery = true)
	public long getCountByBarCodeAndAuditTime(String barCode, String optTime);
	
	// 在已完成的出库中，查无记录，也认定为非法出库
	@Query(value = "SELECT COUNT(*) FROM pump_out_stock_pick_order p "
			+ "LEFT JOIN pump_out_stock_pick_order_item it ON it.osp_id=p.osp_id "
			+ "WHERE p.osp_status='08' AND it.ospitem_barcode=?1 ", nativeQuery = true)
	public long getCountByBarCode(String barCode);

	// 出库单栏目审核（更新 实出数量、条形码）
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE pump_out_stock_pick_order_item p SET p.ospitem_barcode=?1,p.ospitem_quantity=?2 WHERE p.osp_id=?3 AND p.ospitem_product_code=?4 AND p.ospitem_delivery_stock=?5 AND p.ospitem_out_bin_code=?6", nativeQuery = true)
	public void auditOrderItem(String barcode, double act_qty, long sinId, String pCode, long deliveryStId,
			String binCode);
}
