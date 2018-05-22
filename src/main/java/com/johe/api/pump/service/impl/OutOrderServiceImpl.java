package com.johe.api.pump.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.dto.AuditInOrderDto;
import com.johe.api.pump.dto.AuditOutOrderDto;
import com.johe.api.pump.dto.AuditOutOrderItemDto;
import com.johe.api.pump.dto.OutOrderDto;
import com.johe.api.pump.dto.OutOrderItemDto;
import com.johe.api.pump.dto.ScanOutOrderDto;
import com.johe.api.pump.entity.InOrderItemEntity;
import com.johe.api.pump.entity.InventoryBookEntity;
import com.johe.api.pump.entity.MaterialEntity;
import com.johe.api.pump.entity.MessageEntity;
import com.johe.api.pump.entity.OutOrderEntity;
import com.johe.api.pump.entity.OutOrderItemEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.AuditRecordRepository;
import com.johe.api.pump.repository.InventoryBookRepository;
import com.johe.api.pump.repository.MaterialRepository;
import com.johe.api.pump.repository.MessageRepository;
import com.johe.api.pump.repository.OutOrderItemRepository;
import com.johe.api.pump.repository.OutOrderRepository;
import com.johe.api.pump.service.MessageService;
import com.johe.api.pump.service.OutOrderService;
import com.johe.api.pump.service.SeqNumberService;
import com.johe.api.pump.util.AppConstants;

@Service
public class OutOrderServiceImpl implements OutOrderService {

	@Autowired
	OutOrderRepository outOrderReps;
	
	@Autowired
	OutOrderItemRepository outItemReps;
	

	@Autowired
	AuditRecordRepository auditReps;
	
	@Autowired
	MaterialRepository mReps;
	
	@Autowired
	MessageRepository msgReps;
	
	@Autowired
	MessageService msgService;
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	InventoryBookRepository ibReps;
	
	@Autowired
	SeqNumberService seqService;
	
	@Transactional
	@Override
	public OutOrderEntity createOutOrder(OutOrderDto dto) throws Exception {
		// 校验
		checkDto(dto);
		
		// 保存出库单
		OutOrderEntity outOrderEntity = saveOrder(dto);
		
		boolean autoFlg = true;//市采特性的物料自动审核通过（列表中所有都是市采的）
		AuditOutOrderDto auditDto = new AuditOutOrderDto();
		List<AuditOutOrderItemDto> autoItemList = new ArrayList<AuditOutOrderItemDto>();
		
		
		// 批量保存入库单栏目
		List<OutOrderItemDto> itemList = dto.getItem_list();
		for(int i=0;i<itemList.size();i++){
			OutOrderItemDto itemDto = itemList.get(i);
			OutOrderItemEntity ie = new OutOrderItemEntity();
			ie.setOspid(outOrderEntity.getOspid());
			ie.setOspitem_product_code(itemDto.getProduct_code());
			ie.setOspitem_product_name(itemDto.getProduct_name());//小类ID
			ie.setOspitem_barcode(itemDto.getBarcode());// 条形码
			ie.setOspitem_brand(itemDto.getBrand());
			ie.setOspitem_category(itemDto.getCategory_name());//大类ID
			ie.setMt_feature(itemDto.getFeature());
			ie.setOspitem_spec_model(itemDto.getSpec_model());
			ie.setOspitem_delivery_stock(itemDto.getStorage());
//			ie.setOspitem_out_bin(itemDto.getSbin_area()+"."+itemDto.getSbin_rack()+"."+itemDto.getSbin_pos());//编码规则：大类代码+小类代码+仓库代码+仓位区+仓位架+仓位位
			ie.setOspitem_out_bin_area(itemDto.getSbin_area());
			ie.setOspitem_out_bin_rack(itemDto.getSbin_rack());
			ie.setOspitem_out_bin_pos(itemDto.getSbin_pos());
			ie.setOspitem_out_bin_code(itemDto.getStockbin_code());
			ie.setOspitem_quantity(itemDto.getItem_qty());
			ie.setItem_qty(itemDto.getItem_qty());
			ie.setOspitem_measure_unit(itemDto.getMeasure_unit());
			ie.setOspitem_price(itemDto.getPrice());
						
			outItemReps.save(ie);
			
			if("02".equals(itemDto.getFeature())) {
				//准备市采出库单自动审核数据
				AuditOutOrderItemDto audit = new AuditOutOrderItemDto();
				audit.setAct_qty_out(itemDto.getItem_qty());
				audit.setBarcode(itemDto.getBarcode());
				audit.setOsp_id(outOrderEntity.getOspid());
				audit.setProduct_code(itemDto.getProduct_code());
				audit.setStockbin_code(itemDto.getStockbin_code());
				audit.setStorage(itemDto.getStorage());
				autoItemList.add(audit);	
			}
			
			if(!"02".equals(itemDto.getFeature())) {
				autoFlg = false;
			}			

		}
		if(autoFlg == true) {
			//市采自动审核
			auditDto.setAudit_person_id(dto.getMake_person());
			auditDto.setAudit_person_name(String.valueOf(dto.getMake_person()));
			auditDto.setAudit_status("06");
			auditDto.setDelivery_st_id(dto.getDelivery_storage());
			auditDto.setOrder_id(outOrderEntity.getOspid());
			auditDto.setOut_type(dto.getOut_type());
			auditDto.setReject_reason("");
			auditDto.setItem_list(autoItemList);
			audit(auditDto,true);
			//添加审核消息
			Date now = new Date();
			String strNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
			MessageEntity msg = new MessageEntity();
			msg.setAudit_post("03");
			msg.setAudit_records("市采物料，系统自动审核通过");
			msg.setBiz_type(dto.getOut_type());
			msg.setMsg_content("有一条物料["+getMsgType(dto.getOut_type())+"]，市采物料，系统自动审核通过！");
			msg.setSend_time(strNow);
			msg.setCre_time(strNow);
			msg.setFinish_time(strNow);
			msg.setMsg_creator(dto.getMake_person());
			msg.setRelate_id(outOrderEntity.getOspid());

			msgReps.save(msg);
		}else {
			// 添加待审核消息，提醒审核人
			msgService.addAuditMsg(outOrderEntity.getOspid(), dto.getFeature(), dto.getOut_type(), dto.getMake_person(), getMsgType(dto.getOut_type()));
		}	
		
		return outOrderEntity;
	}
	
	private String getMsgType(String type) {
		String msg = "";//01借用出库、02领料出库、03加工出
		switch(type) {
		case "01":
			msg = "借用出库";
			break;
		case "02":
			msg = "领料出库";
			break;
		case "03":
			msg = "加工出库";
			break;
		}
		return msg;
	}
	
	/*@Transactional
	@Override
	public OutOrderEntity audit(String bizType, long orderId, long auditUserId, String status, String auditSuggestion) {
		Date now = new Date();
		// 更新入库单状态
		OutOrderEntity ioe = new OutOrderEntity();
		ioe.setOspid(orderId);
		ioe.setOsp_audit_person(Long.toString(auditUserId));
		ioe.setOsp_audit_date(new SimpleDateFormat("yyyy-MM-dd").format(now));
		ioe.setOsp_reason(auditSuggestion);
		ioe.setOsp_status(status);
		outOrderReps.saveAndFlush(ioe);
		
		// 添加审核记录
		AuditRecordEntity are =  new AuditRecordEntity();
		are.setAudit_node(status);
		are.setAudit_person(auditUserId);
		are.setAudit_suggestion(auditSuggestion);
		are.setAudit_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now));
		are.setBiz_type(bizType);
		are.setRelate_id(orderId);
		em.persist(are);
		em.flush();
		
		return ioe;
	}*/
	
	
	
	private ResultEntity<String> checkDto(OutOrderDto dto) {
		if(StringUtils.isBlank(dto.getOut_type()) || //出库类型
    			!dto.getOut_type().equals("01") || 
    			!dto.getOut_type().equals("02") || 
    			!dto.getOut_type().equals("03") ) {
			return new ResultEntity<String>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(),
					null);
    	}
		
				
		List<OutOrderItemDto> itemList = dto.getItem_list();
		boolean errFlg = false;
		// 校验物料列表（栏目）
		for(int i=0;i<itemList.size();i++){
			if(!checkItemDto(itemList.get(i))) {
				errFlg = true;
				break;
			}
		}
		if(errFlg) {
			return new ResultEntity<String>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(),
					null);
		}
		
		
		return new ResultEntity<String>(ResultStatus.OK.getCode(),
				ResultStatus.OK.getMessage(),
				"Success");
	}

	private OutOrderEntity saveOrder(OutOrderDto dto) {
		OutOrderEntity o = new OutOrderEntity();
		o.setOsp_type(dto.getOut_type());//类型
		o.setOsp_make_person(dto.getMake_person());//制单人
		o.setMt_feature(dto.getFeature());// 特性
		if("01".equals(dto.getFeature()) || "02".equals(dto.getFeature())) {
			o.setOsp_status("10");//普通/市采的物料，直接进入库管审核10
		}else {
			o.setOsp_status("01");//重要/关键的物料，正常走二级或三级审核流程
		}
		o.setOsp_delivery_date(dto.getDelivery_date());//发货日期
		o.setOsp_pick_address(dto.getPick_address());//发货地点
		o.setOsp_delivery_person(dto.getDelivery_person());//发货人
		o.setOsp_delivery_stock(dto.getDelivery_storage());//发货仓库
		o.setOsp_pick_person(dto.getPick_person());
		
		Date now = new Date();
		o.setOsp_sn(seqService.queryByBiztype(AppConstants.SN_PREFIX_MAP.get("OUT"+dto.getOut_type())));
		o.setOsp_batch_no(new SimpleDateFormat("yyyyMMddHHmmss").format(now));
		o.setOsp_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now));
		
		return outOrderReps.save(o);
	}
	
	
	private boolean checkItemDto(OutOrderItemDto dto) {
		if(dto == null || 
//				StringUtils.isBlank(dto.getSbin_id()) || 
//				StringUtils.isBlank(dto.getCategory_name()) || 
				StringUtils.isBlank(dto.getFeature()) || 
				StringUtils.isBlank(dto.getMeasure_unit()) || 
//				StringUtils.isBlank(dto.getStorage()) || 
//				StringUtils.isBlank(dto.getSbin_area()) || 
//				StringUtils.isBlank(dto.getSbin_rack()) || 
//				StringUtils.isBlank(dto.getSbin_pos()) || 
				StringUtils.isBlank(dto.getProduct_code()) 
				) {
			return false;
		}
		return true;
	}

	@Transactional
	@Override
	public OutOrderEntity createOutOrderByScan(ScanOutOrderDto dto) throws Exception {
		boolean autoFlg = true;//市采特性的物料自动审核通过（列表中所有都是市采的）
		AuditOutOrderDto auditDto = new AuditOutOrderDto();
		List<AuditOutOrderItemDto> autoItemList = new ArrayList<AuditOutOrderItemDto>();
		OutOrderEntity ooe = new OutOrderEntity();
		ooe.setMt_feature(dto.getFeature());
		ooe.setOsp_batch_no(String.valueOf(System.currentTimeMillis()));
		ooe.setOsp_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		ooe.setOsp_delivery_date(dto.getDelivery_date());
		ooe.setOsp_delivery_person(dto.getDelivery_person());
		ooe.setOsp_delivery_stock(dto.getStorage_id());
		ooe.setOsp_make_person(dto.getMake_person());
		ooe.setOsp_pick_address(dto.getPick_address());
		ooe.setOsp_pick_person(dto.getPick_person());
		ooe.setOsp_sn(seqService.queryByBiztype(AppConstants.SN_PREFIX_MAP.get("OUT"+dto.getOut_type())));
		ooe.setOsp_status("01");
		ooe.setOsp_type(dto.getOut_type());
		OutOrderEntity oe = outOrderReps.save(ooe);
		
		OutOrderItemEntity oitem = new OutOrderItemEntity();
		oitem.setOspid(oe.getOspid());
		oitem.setMt_feature(dto.getFeature());
		oitem.setOspitem_assist_attr(dto.getAssist_attr());
		oitem.setOspitem_barcode(dto.getBarcode());
		oitem.setOspitem_brand(dto.getBrand());
		oitem.setOspitem_category(dto.getBig_id());
		oitem.setOspitem_delivery_stock(dto.getStorage_id());
		oitem.setOspitem_measure_unit(dto.getMeasure_unit());
		oitem.setOspitem_out_bin_area(dto.getSbin_area());
		oitem.setOspitem_out_bin_pos(dto.getSbin_pos());
		oitem.setOspitem_out_bin_rack(dto.getSbin_rack());
		oitem.setOspitem_out_bin_code(dto.getStockbin_code());
		oitem.setOspitem_price(dto.getPrice());
		oitem.setOspitem_product_code(dto.getProduct_code());
		oitem.setOspitem_product_name(dto.getSmall_id());
		oitem.setOspitem_quantity(dto.getItem_qty());
		oitem.setItem_qty(dto.getItem_qty());
		oitem.setOspitem_spec_model(dto.getSpec_model());
		outItemReps.save(oitem);
		
		if("02".equals(oitem.getMt_feature())) {
			//准备市采出库单自动审核数据
			AuditOutOrderItemDto audit = new AuditOutOrderItemDto();
			audit.setAct_qty_out(oitem.getItem_qty());
			audit.setBarcode(oitem.getOspitem_barcode());
			audit.setOsp_id(oe.getOspid());
			audit.setProduct_code(oitem.getOspitem_product_code());
			audit.setStockbin_code(dto.getStockbin_code());
			audit.setStorage(dto.getStorage_id());
			autoItemList.add(audit);	
		}
		
		if(!"02".equals(oitem.getMt_feature())) {
			autoFlg = false;
		}			
		
		if(autoFlg == true) {
			//市采自动审核
			auditDto.setAudit_person_id(dto.getMake_person());
			auditDto.setAudit_person_name(String.valueOf(dto.getMake_person()));
			auditDto.setAudit_status("06");
			auditDto.setDelivery_st_id(dto.getStorage_id());
			auditDto.setOrder_id(oe.getOspid());
			auditDto.setOut_type(dto.getOut_type());
			auditDto.setReject_reason("");
			auditDto.setItem_list(autoItemList);
			audit(auditDto,true);
			//添加审核消息
			Date now = new Date();
			String strNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
			MessageEntity msg = new MessageEntity();
			msg.setAudit_post("03");
			msg.setAudit_records("市采物料，系统自动审核通过");
			msg.setBiz_type(dto.getOut_type());
			msg.setMsg_content("有一条物料["+getMsgType(dto.getOut_type())+"]，市采物料，系统自动审核通过！");
			msg.setSend_time(strNow);
			msg.setCre_time(strNow);
			msg.setFinish_time(strNow);
			msg.setMsg_creator(dto.getMake_person());
			msg.setRelate_id(oe.getOspid());

			msgReps.save(msg);
		}else {		
			// 添加待审核消息，提醒审核人
			msgService.addAuditMsg(ooe.getOspid(), dto.getFeature(), dto.getOut_type(), dto.getMake_person(),getMsgType(dto.getOut_type()));
		}
		return ooe;
	}

	@Transactional
	@Override
	public void audit(AuditOutOrderDto dto, boolean isAuto) throws Exception {
		Date now = new Date();
		String auditRecords = " -> 库管审核(s%)s% ";
		String auditDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
		String strStatus = ((isAuto== true || "06".equals(dto.getAudit_status()))? "08":dto.getAudit_status());
		// 更新出库单
		outOrderReps.auditOrder(strStatus, dto.getAudit_person_name(), dto.getReject_reason(), auditDateTime, dto.getOrder_id());
		
		// 更新物料列表
		for (int i = 0; i < dto.getItem_list().size(); i++) {
			AuditOutOrderItemDto it = dto.getItem_list().get(i);
			outItemReps.auditOrderItem(it.getBarcode(), it.getAct_qty_out(), it.getOsp_id(),
					it.getProduct_code(), it.getStorage(), it.getStockbin_code());
			
			if(dto.getAudit_status().equals("06")) {//库管审核通过时，才更新物料库存
				// 更新库存
				MaterialEntity mt = mReps.findByBarcode(it.getBarcode());
				double qty = 0;
				double qtyTotal = 0;
				if(it.getAct_qty_out()>mt.getMt_in_remain_quantity()) {
					qty = 0;
				}else {
					qty = mt.getMt_in_remain_quantity() -it.getAct_qty_out();
				}
				if(it.getAct_qty_out() > mt.getMt_in_total_quantity()) {
					qtyTotal = 0;
				}else {
					qtyTotal = mt.getMt_in_total_quantity() - it.getAct_qty_out();
				}
				mReps.update(qtyTotal, qty, mt.getMaterialid());
				// 添加库存台账
				InventoryBookEntity ibe = new InventoryBookEntity();
				ibe.setBarcode(mt.getBarcode().substring(0, 14));
				ibe.setMt_id(mt.getMaterialid());
				ibe.setCre_date(auditDateTime);
				ibe.setIb_type(dto.getOut_type());
				ibe.setOrder_id(dto.getOrder_id());
				ibe.setItem_id(mt.getMaterialid());
				ibe.setIn_out_type("02");//收入支出类型：01收入、02支出
				ibe.setLast_qty(mt.getMt_in_remain_quantity());// 期初结存 就是 库存更新前的 实际库存
				ibe.setIn_out_qty(it.getAct_qty_out());// 本次的出库数量
				ibe.setCur_qty(qty);//当期结存 =（期初结存 - 出库数量）
				ibReps.save(ibe);
			}
		}
		
		// 添加审核记录
		auditReps.create(dto.getAudit_person_id(), dto.getReject_reason(), dto.getOut_type(), dto.getAudit_status(),
				auditDateTime, dto.getOrder_id());
		String biz_type="06";//借用
		if(dto.getOut_type().equals("02")) {//01借用出库、02领料出库、03加工出库
			biz_type="07";//领料
		}else if(dto.getOut_type().equals("03")){
			biz_type="08";//加工
		}
		
		// 更新审核消息
		msgReps.updateAuditMsg(auditDateTime, " -> 库管审核("+dto.getAudit_person_name()+") "+(dto.getAudit_status().equals("06")?"通过  -> 完成":"驳回"), dto.getOrder_id(), biz_type);
	}


}
