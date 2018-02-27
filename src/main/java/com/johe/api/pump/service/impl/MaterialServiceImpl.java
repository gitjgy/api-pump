package com.johe.api.pump.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.entity.HistoryAlarmEntity;
import com.johe.api.pump.entity.MaterialEntity;
import com.johe.api.pump.repository.HistoryAlarmRepository;
import com.johe.api.pump.repository.MaterialRepository;
import com.johe.api.pump.service.MaterialService;

@Service
public class MaterialServiceImpl implements MaterialService {
	@Autowired
	MaterialRepository mtReps;
	
	@Autowired
	HistoryAlarmRepository haReps;
	
	@Transactional
	@Override
	public void addHistoryAlarm(String bar_code, double qty)  throws Exception{
		MaterialEntity mt = mtReps.findByBarcode(bar_code);
		if(mt != null) {
			String status = "";
			// 短缺（在库余量 < 最低存量）
			if(mt.getMt_in_remain_quantity() < mt.getMt_min_quantity()) {
				status = "短缺";
			}
			
			// 超储（在库余量 > 最高存量）
			if(mt.getMt_in_remain_quantity() > mt.getMt_max_quantity()) {
				status = "超储";
			}
			
			// 添加预警（历史）
			if(!status.equals("")) {
//				01采购申请、02采购入库、03归还入库、04其他入库、05其他入库，06借用出库、07领料出库、08加工出库 09 调拨
				HistoryAlarmEntity ha = new HistoryAlarmEntity();
				ha.setAlarm_qty(mt.getMt_in_remain_quantity());
				ha.setAlarm_qty_before(mt.getMt_in_remain_quantity()- qty);
				ha.setAlarm_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				ha.setBig_name(mt.getCategory_big().getName());
				ha.setMaterial_barcode(bar_code);
				ha.setMaterial_name(mt.getMt_fullname());
				ha.setMax_qty(mt.getMt_max_quantity());
				ha.setMin_qty(mt.getMt_min_quantity());
				ha.setMea_unit_name(mt.getMt_measure_unit());
				ha.setSbin_code(mt.getStockbin_area().getSbin_code()+mt.getStockbin_rack().getSbin_code()+mt.getStockbin_pos().getSbin_code());
				ha.setSbin_name(mt.getStockbin_area().getSbin_name()+mt.getStockbin_rack().getSbin_name()+mt.getStockbin_pos().getSbin_name());
				ha.setStatus(status);
				haReps.save(ha);
			}
			
		}
	}

}
