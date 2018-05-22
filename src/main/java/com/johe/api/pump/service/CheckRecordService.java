package com.johe.api.pump.service;

import org.springframework.web.bind.annotation.PathVariable;

import com.johe.api.pump.dto.CheckRecordDto;
import com.johe.api.pump.dto.MaterialCheckDto;
import com.johe.api.pump.entity.CheckRecordEntity;

public interface CheckRecordService {
	
	// 创建盘点记录
	CheckRecordEntity create(CheckRecordDto dto);
	
	// 物料编码搜索
	MaterialCheckDto search(String smallCode,String stgCode,String areaCode,String rackCode,String posCode) throws Exception ;
	// 审核入库单
//	InOrderEntity audit(String bizType, long orderId, long auditUserId, String status, String auditSuggestion);
	
	// 扫描盘点搜索，条形码搜索（期初量、入库量、出库量、库存量、品名、仓库、仓位、类别）
	MaterialCheckDto searchByBarcode(String barcode) throws Exception ;
	
	// 手动盘点 选择物料搜索（物料ID）搜索（期初量、入库量、出库量、库存量、品名、仓库、仓位、类别）
	MaterialCheckDto searchByMtId(long mt_id) throws Exception ;
	
	// 手动盘点 选择物料搜索（物料条码（前14位））搜索（期初量、入库量、出库量、库存量、品名、仓库、仓位、类别）
	MaterialCheckDto searchByMtBarcode(String mt_barcode) throws Exception ;

}
