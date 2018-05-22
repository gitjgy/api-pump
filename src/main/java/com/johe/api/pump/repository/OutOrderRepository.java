package com.johe.api.pump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.entity.OutOrderEntity;

public interface OutOrderRepository extends JpaRepository<OutOrderEntity, Long>,
									JpaSpecificationExecutor<OutOrderEntity> {
	
	@Query(value="SELECT COUNT(*) FROM pump_out_stock_pick_order p LEFT JOIN pump_out_stock_pick_order_item it ON p.osp_id=it.osp_id WHERE p.osp_status='08' AND it.ospitem_barcode=?1",nativeQuery=true)
	long getOutQty(String barCode);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value="UPDATE pump_out_stock_pick_order p SET p.osp_status=?1,p.osp_audit_person=?2,p.osp_reason=?3,p.osp_audit_date=?4 WHERE p.osp_id=?5",nativeQuery=true)
	public void auditOrder(String status,String audit_person,String reject_reason,String audit_date,long osp_id);
	
	// 物料借用人
	@Query(value="SELECT " + 
			"  o.osp_pick_address," + 
			"  o.osp_pick_person,  " + 
			"  o.osp_date" + 
			" FROM" + 
			"  pump_out_stock_pick_order o " + 
			"  INNER JOIN pump_out_stock_pick_order_item i " + 
			"    ON i.osp_id = o.osp_id " + 
			" WHERE o.osp_type = '01' " + // 01借用
			"  AND o.osp_status = '08' " + //08完成
			"  AND i.ospitem_barcode =?1",nativeQuery=true)
	List<Object> getPickInfo(String barCode);
}
