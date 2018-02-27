package com.johe.api.pump.service;

import com.johe.api.pump.dto.AuditOutOrderDto;
import com.johe.api.pump.dto.OutOrderDto;
import com.johe.api.pump.dto.ScanOutOrderDto;
import com.johe.api.pump.entity.OutOrderEntity;

public interface OutOrderService {
	
	// 创建出库单
	OutOrderEntity createOutOrder(OutOrderDto dto) throws Exception ;
	
	// 创建出库单（扫码）
	OutOrderEntity createOutOrderByScan(ScanOutOrderDto dto) throws Exception ;
	
	// 审核出库单
//	OutOrderEntity audit(String bizType, long orderId, long auditUserId, String status, String auditSuggestion);
	
	// 出库单审核
	void audit(AuditOutOrderDto dto) throws Exception ;
}
