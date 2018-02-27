package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.entity.TransferOrderEntity;

public interface TransferOrderRepository extends JpaRepository<TransferOrderEntity, Long>,
									JpaSpecificationExecutor<TransferOrderEntity> {
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value="UPDATE pump_transfer_order SET tran_status= ?1,tran_audit_person=?2,tran_reject_reason=?3,tran_audit_date=?4 WHERE tran_id=?5",nativeQuery=true)//SQL
	public void auditOrder(String status,String audit_person,String reject_reason,String audit_date,long tranId);
}
