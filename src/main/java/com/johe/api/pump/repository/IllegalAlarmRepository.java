package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.entity.IllegalAlarmEntity;

public interface IllegalAlarmRepository extends JpaRepository<IllegalAlarmEntity, Long>,
									JpaSpecificationExecutor<IllegalAlarmEntity> {
	
	/*@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value="INSERT INTO pump_illegal_out(src_id,stg_code,stg_name,cg_big_code,cg_big_name,cg_small_code,cg_small_name,"
			+ "sbin_code,sbin_area_code,sbin_rack_code,sbin_pos_code,bar_code,out_qty,mea_unit,open_time,close_time,"
			+ "opt_time,opt_person,relate_code,is_alarm) "
			+ "VALUES(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18,?19,?20,?21)",nativeQuery=true)//SQL
	IllegalAlarmEntity insertIllegal(String src_id,String stg_code,String stg_name,String cg_big_code,String cg_big_name,String cg_small_code,
			String cg_small_name,String sbin_code,String sbin_area_code,String sbin_rack_code,String sbin_pos_code,
			String bar_code,String out_qty,String mea_unit,String open_time,String close_time,String opt_time,
			String opt_person,String relate_code,String is_alarm);*/
}
