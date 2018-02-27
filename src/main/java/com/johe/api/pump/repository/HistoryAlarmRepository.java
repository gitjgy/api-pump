package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.johe.api.pump.entity.HistoryAlarmEntity;

public interface HistoryAlarmRepository extends JpaRepository<HistoryAlarmEntity, Long>,
								JpaSpecificationExecutor<HistoryAlarmEntity> {
	
	
}
