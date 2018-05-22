package com.johe.api.pump.service;

import com.johe.api.pump.dto.AuditInOrderDto;
import com.johe.api.pump.dto.BarcodeIsUsableDto;
import com.johe.api.pump.dto.InOrderDto;
import com.johe.api.pump.dto.ScanInOrderDto;
import com.johe.api.pump.entity.InOrderEntity;

public interface InOrderService {
	
	// 创建入库单
	InOrderEntity save(InOrderDto dto) throws Exception ;
	
	// 创建入库单（扫码入库）
	InOrderEntity save(ScanInOrderDto dto) throws Exception ;
	
	// 审核入库单
//	InOrderEntity audit(String bizType, long orderId, long auditUserId, String status, String auditSuggestion);
	
	// 入库审核
	void audit(AuditInOrderDto dto) throws Exception;
	
	// 校验条形码是否可用
	BarcodeIsUsableDto verifyBarcodeIsUsable(BarcodeIsUsableDto dto) throws Exception;
}
