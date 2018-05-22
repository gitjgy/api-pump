package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.entity.InOrderEntity;
import com.johe.api.pump.entity.InOrderItemEntity;

public interface InOrderRepository extends JpaRepository<InOrderEntity, Long>,
									JpaSpecificationExecutor<InOrderEntity> {
	
//	@Modifying(clearAutomatically = true)
//	@Transactional
//	@Query(value="UPDATE pump_stockin_order SET sin_audit_person = ?1,sin_audit_date=?2,sin_reject_reason=?3,sin_status=?4 WHERE sin_id=?5",nativeQuery=true)//SQL
//	public void audit(long auditPerson, String auditDate,String rejectReason,String status,long sinId);
	
//	@Modifying(clearAutomatically = true)
//	@Transactional
//	@Query("UPDATE InOrderEntity SET stg_address = ?1 WHERE stg_id=?2")//JPQL
//	public void audit(String addr, long id);

	// 盘点（入库总数量）
	@Query(value="SELECT COUNT(*) FROM pump_stockin_order p LEFT JOIN pump_stockin_order_item it ON p.sin_id=it.sin_id WHERE p.sin_type IN ('02','03','04','05') AND p.sin_status='08' AND it.siitem_barcode=?1",nativeQuery=true)
	long getInQty(String barCode);			
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value="UPDATE pump_stockin_order p SET p.sin_status=?1,p.sin_audit_person=?2,p.sin_reject_reason=?3,p.sin_audit_date=?4 WHERE p.sin_id=?5",nativeQuery=true)
	public void auditOrder(String sin_status,String audit_person,String reject_reason,String audit_date,long sinId);
	
	// 获取条码对应的入库记录
	@Query(value="SELECT COUNT(*) FROM pump_stockin_order p LEFT JOIN pump_stockin_order_item it ON p.sin_id=it.sin_id WHERE p.sin_type IN ('02','03','04','05') AND p.sin_status in('01','02','04','06','08','10') AND it.siitem_barcode=?1",nativeQuery=true)
	long getCountForBarcode(String barCode);		
}
