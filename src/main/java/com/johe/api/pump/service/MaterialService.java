package com.johe.api.pump.service;

public interface MaterialService {
	
	// 若物料存量超过阈值，添加预警（历史）
	void addHistoryAlarm(String bar_code,double qty) throws Exception;
}
