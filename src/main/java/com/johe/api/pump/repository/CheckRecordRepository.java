package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.entity.CheckRecordEntity;

public interface CheckRecordRepository extends JpaRepository<CheckRecordEntity, Long>,
									JpaSpecificationExecutor<CheckRecordEntity> {
	
	/*@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value="UPDATE pump_stockin_order SET sin_audit_person = ?1,sin_audit_date=?2,sin_reject_reason=?3,sin_status=?4 WHERE sin_id=?5",nativeQuery=true)//SQL
	public void audit(long auditPerson, String auditDate,String rejectReason,String status,long sinId);*/
	
	


}
