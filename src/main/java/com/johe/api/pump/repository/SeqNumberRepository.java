package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.johe.api.pump.entity.SeqNumberEntity;

public interface SeqNumberRepository extends JpaRepository<SeqNumberEntity, Long>,
								JpaSpecificationExecutor<SeqNumberEntity> {
	

	SeqNumberEntity findByBtypeAndYmdhms(String bType,String ymdhms);
	
}
