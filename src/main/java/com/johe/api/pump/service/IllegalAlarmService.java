package com.johe.api.pump.service;

import com.johe.api.pump.dto.IllegalOutDto;
import com.johe.api.pump.dto.ResponseIllegalOutDto;
import com.johe.api.pump.entity.IllegalAlarmEntity;

public interface IllegalAlarmService {
	
	IllegalAlarmEntity create(String src_id,String stg_code,String cg_big_code,String cg_small_code,
			String sbin_code,String sbin_area_code,String sbin_rack_code,String sbin_pos_code,
			String bar_code,int out_qty,String mea_unit,String open_time,String close_time,String opt_time,
			String opt_person,String relate_code);
	
	IllegalAlarmEntity create(IllegalOutDto ioDto);
	
	ResponseIllegalOutDto checkIsIllegal(IllegalOutDto ioDto);
}
