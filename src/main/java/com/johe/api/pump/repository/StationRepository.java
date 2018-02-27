package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.johe.api.pump.entity.StationEntity;

public interface StationRepository extends JpaRepository<StationEntity, Long>,
								JpaSpecificationExecutor<StationEntity> {
	


	
}
