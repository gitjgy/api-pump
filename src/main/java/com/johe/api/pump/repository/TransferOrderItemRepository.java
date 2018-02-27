package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.entity.OutOrderItemEntity;
import com.johe.api.pump.entity.TransferItemEntity;

public interface TransferOrderItemRepository extends JpaRepository<TransferItemEntity, Long>,
									JpaSpecificationExecutor<TransferItemEntity> {
	// 详情	
	TransferItemEntity findByTranidAndItemid(long tranid,long itemid);
	
	// 调拨审核（调拨单ID、条形码）
	@Query(value="SELECT ti FROM TransferItemEntity ti WHERE ti.tranid=?1 AND ti.tranitem_barcode=?2")
	TransferItemEntity getByTranidAndBarcode(long tranid, String barcode);
	
	// 调拨单栏目审核（更新 实出数量、条形码）
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE pump_transfer_order_item p SET p.tranitem_barcode=?1,p.tranitem_quantity=?2 "
			+ "WHERE p.tran_id=?3 AND p.tranitem_product_code=?4 AND p.tranitem_out_stock=?5 "
			+ "AND p.tranitem_out_bin_code=?6 AND p.tranitem_in_stock=?7 AND p.tranitem_in_bin_code=?8", nativeQuery = true)
	public void auditOrderItem(String barcode, double act_qty, long sinId, String pCode,
			long out_stock, String out_bin_code, long in_stock,String in_bin_code);
}
