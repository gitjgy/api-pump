package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.entity.SysCodeEntity;

public interface SysCodeRepository extends JpaRepository<SysCodeEntity, Long>,
								JpaSpecificationExecutor<SysCodeEntity> {
	

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value="SELECT nextval('seq_order_no') from dual",nativeQuery=true)
	int getCurrentValue();
	
}
