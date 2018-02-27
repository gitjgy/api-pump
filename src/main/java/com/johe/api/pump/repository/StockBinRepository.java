package com.johe.api.pump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.johe.api.pump.entity.StockBinEntity;

public interface StockBinRepository extends JpaRepository<StockBinEntity, Long>,
								JpaSpecificationExecutor<StockBinEntity> {
	
	@Query(value="SELECT sbin_id,parent_sbin_id,stg_id,sbin_name,sbin_code,sbin_fullname,sbin_type "
			+ "FROM pump_stock_bin WHERE sbin_code=?1 AND sbin_type=?2",nativeQuery=true)
	StockBinEntity getByCodeAndType(String code,String type);
}
