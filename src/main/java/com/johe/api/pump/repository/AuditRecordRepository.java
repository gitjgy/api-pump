package com.johe.api.pump.repository;

import java.text.SimpleDateFormat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.entity.AuditRecordEntity;

public interface AuditRecordRepository extends JpaRepository<AuditRecordEntity, Long>,
								JpaSpecificationExecutor<AuditRecordEntity> {
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value="INSERT INTO pump_audit_records(audit_person,audit_suggestion,biz_type,audit_node,audit_time,relate_id) VALUES(?1,?2,?3,?4,?5,?6)",nativeQuery=true)//SQL
	void create(long auditUserId,String suggestion,String bizType,String auditNode,String auditTime,long relateId);
}
