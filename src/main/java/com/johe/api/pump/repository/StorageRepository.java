package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.johe.api.pump.entity.StorageEntity;


public interface StorageRepository extends JpaRepository<StorageEntity, Long>,
								JpaSpecificationExecutor<StorageEntity> {

//	SysUserEntity findByAccount(String account);
	
	@Query(value="SELECT stg_id,stg_code,stg_name FROM pump_storage WHERE stg_code=?1 ",nativeQuery=true)
	StorageEntity getByStgCode(String stgCode); 
}
