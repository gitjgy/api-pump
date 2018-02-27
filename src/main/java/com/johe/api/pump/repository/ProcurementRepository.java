package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.johe.api.pump.entity.InOrderEntity;

public interface ProcurementRepository extends JpaRepository<InOrderEntity, Long>,
									JpaSpecificationExecutor<InOrderEntity> {

}
