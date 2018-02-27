package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long>,
									JpaSpecificationExecutor<MessageEntity> {
	
	MessageEntity findByMsgid(long msg_id);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value="UPDATE pump_message p SET p.audit_post='00',p.finish_time=?1,p.send_time=?1,p.audit_records=p.audit_records+?2 "
			+ "WHERE p.relate_id=?3 AND p.biz_type=?4",nativeQuery=true)
	public void updateAuditMsg(String finishTime, String auditRecords, long orderId, String biz_type);
}
