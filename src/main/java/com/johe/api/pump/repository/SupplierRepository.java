package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.johe.api.pump.entity.SupplierEntity;

public interface SupplierRepository extends JpaRepository<SupplierEntity, Long>,
								JpaSpecificationExecutor<SupplierEntity> {
	


	
}
