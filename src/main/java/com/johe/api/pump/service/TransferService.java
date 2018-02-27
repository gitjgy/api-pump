package com.johe.api.pump.service;

import com.johe.api.pump.dto.AuditTransferOrderDto;
import com.johe.api.pump.dto.ScanTransferOrderDto;
import com.johe.api.pump.dto.TransferOrderDto;
import com.johe.api.pump.entity.TransferOrderEntity;

public interface TransferService {
	
	// 创建调拨单
	TransferOrderEntity save(TransferOrderDto dto); 
	
	// 创建调拨单（扫码）
	TransferOrderEntity createTranOrderByScan(ScanTransferOrderDto dto) throws Exception ;
	
	// 审核调拨单
//	TransferOrderEntity audit(long orderId, long auditUserId, String status, String auditSuggestion);
	
	// 调拨单审核
	void audit(AuditTransferOrderDto dto) throws Exception ;
}
