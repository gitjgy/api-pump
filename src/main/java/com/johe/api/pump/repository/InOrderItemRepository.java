package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.entity.InOrderItemEntity;

public interface InOrderItemRepository
		extends JpaRepository<InOrderItemEntity, Long>, JpaSpecificationExecutor<InOrderItemEntity> {

	InOrderItemEntity findBySinidAndItemid(long sinid, long itemid);

	// 入库审核（入库单ID、条形码）
	InOrderItemEntity findBySinidAndBarcode(long sinid, String barcode);

	// @Query(value="SELECT
	// it.siitem_stockbin_area,it.siitem_stockbin_rack,it.siitem_stockbin_pos FROM
	// pump_stockin_order_item it",nativeQuery=true)
	// List<InOrderItemDetailDto> getItemList();

	// 入库单栏目审核（更新 实入数量、条形码）
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE pump_stockin_order_item p SET p.siitem_actual_quantity_recv=?2 WHERE p.sin_id=?3 AND p.siitem_product_code=?4 AND p.siitem_receipt_stock=?5 AND p.siitem_stockbin_code=?6 AND p.siitem_barcode=?1", nativeQuery = true)
	public void auditOrderItem(String barcode, double act_qty, long sinId, String pCode, long recvStId, String binCode);
}
