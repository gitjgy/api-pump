package com.johe.api.pump.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.dto.AuditTransferItemDto;
import com.johe.api.pump.dto.AuditTransferOrderDto;
import com.johe.api.pump.dto.ScanTransferOrderDto;
import com.johe.api.pump.dto.TransferItemDto;
import com.johe.api.pump.dto.TransferOrderDto;
import com.johe.api.pump.entity.InventoryBookEntity;
import com.johe.api.pump.entity.MaterialEntity;
import com.johe.api.pump.entity.TransferItemEntity;
import com.johe.api.pump.entity.TransferOrderEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.AuditRecordRepository;
import com.johe.api.pump.repository.InventoryBookRepository;
import com.johe.api.pump.repository.MaterialRepository;
import com.johe.api.pump.repository.MessageRepository;
import com.johe.api.pump.repository.TransferOrderItemRepository;
import com.johe.api.pump.repository.TransferOrderRepository;
import com.johe.api.pump.service.MessageService;
import com.johe.api.pump.service.SeqNumberService;
import com.johe.api.pump.service.TransferService;
import com.johe.api.pump.util.AppConstants;

@Service
public class TransferServiceImpl implements TransferService {

	@Autowired
	TransferOrderRepository orderReps;
	
	@Autowired
	TransferOrderItemRepository itemReps;
	
	@Autowired
	AuditRecordRepository auditReps;

	@Autowired
	MessageRepository msgReps;
	
	@Autowired
	MaterialRepository mReps;
	
	@Autowired
	InventoryBookRepository ibReps;
	
	@Autowired
	MessageService msgService;
	
	@Autowired
	SeqNumberService seqService;
	
	
	@Transactional
	@Override
	public TransferOrderEntity save(TransferOrderDto dto) {
		// 校验
		checkDto(dto);
		
		// 保存调拨单
		TransferOrderEntity tranEntity = saveOrder(dto);
		
		// 批量保存调拨单栏目
		List<TransferItemDto> itemList = dto.getItem_list();
		for(int i=0;i<itemList.size();i++){
			TransferItemDto d = itemList.get(i);
			TransferItemEntity e = new TransferItemEntity();
			
			e.setTranid(tranEntity.getTranid());
			e.setTranitem_amount(d.getTranitem_amount());
			e.setMt_feature(d.getMt_feature());
			e.setTranitem_barcode(d.getTranitem_barcode());
			e.setTranitem_brand(d.getTranitem_brand());
			e.setTranitem_category(d.getTranitem_category());
			e.setTranitem_in_bin_area(d.getTranitem_in_bin_area());
			e.setTranitem_in_bin_rack(d.getTranitem_in_bin_rack());
			e.setTranitem_in_bin_pos(d.getTranitem_in_bin_pos());
			e.setTranitem_in_bin_code(d.getTranitem_in_bin_code());
			e.setTranitem_in_stock(d.getTranitem_in_stock());
			e.setTranitem_out_bin_area(d.getTranitem_out_bin_area());
			e.setTranitem_out_bin_rack(d.getTranitem_out_bin_rack());
			e.setTranitem_out_bin_pos(d.getTranitem_out_bin_pos());
			e.setTranitem_out_bin_code(d.getTranitem_out_bin_code());
			e.setTranitem_out_stock(d.getTranitem_out_stock());
			e.setTranitem_price(d.getTranitem_price());
			e.setTranitem_product_code(d.getTranitem_product_code());
			e.setTranitem_product_name(d.getTranitem_product_name());
			e.setTranitem_quantity(d.getTranitem_quantity());
			e.setTranitem_measure_unit(d.getTranitem_measure_unit());
			e.setTranitem_spec_model(d.getTranitem_spec_model());

			itemReps.save(e);
//			em.persist(itemEntity);
//			if(i % 30 == 0) {// 满30行一提交
//				em.flush();
//				em.clear();
//			}
		}
		
		// 添加审核消息
		msgService.addAuditMsg(tranEntity.getTranid(), dto.getMt_feature(), "09", dto.getTran_make_person(), "调拨单");
		return tranEntity;
	}
	
	/*@Transactional
	@Override
	public TransferOrderEntity audit(long orderId, long auditUserId, String status, String auditSuggestion) {
		Date now = new Date();
		// 更新调拨单状态
		TransferOrderEntity toe = new TransferOrderEntity();
		toe.setTranid(orderId);
		toe.setTran_audit_person(Long.toString(auditUserId));
		toe.setTran_audit_date(new SimpleDateFormat("yyyy-MM-dd").format(now));
		toe.setTran_reject_reason(auditSuggestion);
		toe.setTran_status(status);
//		tranOrderRepository.saveAndFlush(toe);
		orderReps.audit(auditUserId, new SimpleDateFormat("yyyy-MM-dd").format(now), auditSuggestion, status, orderId);
		
		// 添加审核记录
//		AuditRecordEntity are =  new AuditRecordEntity();
//		are.setAudit_node(status);
//		are.setAudit_person(auditUserId);
//		are.setAudit_suggestion(auditSuggestion);
//		are.setAudit_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now));
//		are.setBiz_type("06");//调拨单
//		are.setRelate_id(orderId);
//		em.persist(are);
//		em.flush();
		auditReps.create(auditUserId, auditSuggestion, "06", status, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now), orderId);
		return toe;
	}*/
	
	
	
	private ResultEntity<String> checkDto(TransferOrderDto dto) {
//		if(StringUtils.isBlank(dto.getOut_type()) || //出库类型
//    			!dto.getOut_type().equals("01") || 
//    			!dto.getOut_type().equals("02") || 
//    			!dto.getOut_type().equals("03") ) {
//			return new ResultEntity<String>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
//					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(),
//					null);
//    	}
		
				
		List<TransferItemDto> itemList = dto.getItem_list();
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
				"OK");
	}

	private TransferOrderEntity saveOrder(TransferOrderDto dto) {
		Date now = new Date();
		TransferOrderEntity o = new TransferOrderEntity();
		o.setTran_make_person(dto.getTran_make_person());
		if("01".equals(dto.getMt_feature()) || "02".equals(dto.getMt_feature())) {
			o.setTran_status("10");//普通/市采的物料，直接进入库管审核10
		}else {
			o.setTran_status("01");//重要/关键的物料，正常走二级或三级审核流程
		}
		
		o.setTran_acceptor(dto.getTran_acceptor());
		o.setTran_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now));
		o.setTran_dept(dto.getTran_dept());
		o.setTran_leader(dto.getTran_leader());
		o.setTran_logistics_co(dto.getTran_logistics_co());
		o.setTran_logistics_order_sn(dto.getTran_logistics_order_sn());
		o.setTran_order_sn(seqService.queryByBiztype(AppConstants.SN_PREFIX_MAP.get("DB")));
		
//		o.setTran_order_sn(tran_order_sn);//调拨单编号 
		o.setTran_other_order_sn(dto.getTran_other_order_sn());
		o.setTran_out_address(dto.getTran_out_address());
		o.setTran_out_stock(dto.getTran_out_stock());
		o.setTran_pump_name(dto.getTran_pump_name());
		o.setTran_receipt_date(dto.getTran_receipt_date());
		o.setTran_transfer_person(dto.getTran_transfer_person());
		o.setTran_use_person(dto.getTran_use_person());
		
		return orderReps.save(o);
	}
	
	
	private boolean checkItemDto(TransferItemDto dto) {
		if(dto == null || 
				StringUtils.isBlank(dto.getMt_feature()) || 
//				StringUtils.isBlank(dto.getTranitem_in_stock()) || 
//				StringUtils.isBlank(dto.getTranitem_in_bin_area()) || 
//				StringUtils.isBlank(dto.getTranitem_in_bin_rack()) || 
//				StringUtils.isBlank(dto.getTranitem_in_bin_pos()) || 
//				StringUtils.isBlank(dto.getTranitem_out_bin_area()) || 
//				StringUtils.isBlank(dto.getTranitem_out_bin_rack()) || 
//				StringUtils.isBlank(dto.getTranitem_out_bin_pos()) || 
				StringUtils.isBlank(dto.getTranitem_in_bin_code()) || 
				StringUtils.isBlank(dto.getTranitem_out_bin_code()) || 
//				StringUtils.isBlank(dto.getTranitem_out_stock()) || 
//				StringUtils.isBlank(dto.getTranitem_product_name()) || 
				StringUtils.isBlank(dto.getTranitem_product_code()) ) {
			return false;
		}
		return true;
	}

	@Transactional
	@Override
	public TransferOrderEntity createTranOrderByScan(ScanTransferOrderDto dto) throws Exception {
		TransferOrderEntity toe = new TransferOrderEntity();
		toe.setTran_acceptor(dto.getAcceptor());
		toe.setTran_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		toe.setTran_dept(dto.getDept());
		toe.setTran_leader(dto.getLeader());
		toe.setTran_logistics_co(dto.getLogistics_co());
		toe.setTran_logistics_order_sn(dto.getLogistics_order_sn());
		toe.setTran_make_person(dto.getMake_person());
		toe.setTran_order_sn(seqService.queryByBiztype(AppConstants.SN_PREFIX_MAP.get("DB")));
		toe.setTran_other_order_sn(dto.getOther_order_sn());
		toe.setTran_out_address(dto.getOut_address());
		toe.setTran_out_stock(dto.getOut_stock());
		if("01".equals(dto.getMt_feature()) || "02".equals(dto.getMt_feature())) {
			toe.setTran_status("10");//普通/市采的物料，直接进入库管审核10
		}else {
			toe.setTran_status("01");//重要/关键的物料，正常走二级或三级审核流程
		}
		
		toe.setTran_pump_name(dto.getPump_name());
		toe.setTran_receipt_date(dto.getReceipt_date());
		toe.setTran_transfer_person(dto.getTransfer_person());
		toe.setTran_use_person(dto.getUse_person());
		toe.setMt_feature(dto.getMt_feature());
		TransferOrderEntity to = orderReps.save(toe);
		
		TransferItemEntity titem = new TransferItemEntity();
		titem.setTranid(to.getTranid());
		titem.setMt_feature(dto.getMt_feature());
		titem.setTranitem_barcode(dto.getBarcode());
		titem.setTranitem_brand(dto.getBrand());
		titem.setTranitem_category(dto.getBig_id());
		titem.setTranitem_in_bin_area(dto.getIn_bin_area());
		titem.setTranitem_in_bin_pos(dto.getIn_bin_pos());
		titem.setTranitem_in_bin_rack(dto.getIn_bin_rack());
		titem.setTranitem_in_bin_code(dto.getIn_bin_code());
		titem.setTranitem_in_stock(dto.getIn_stock_id());
		titem.setTranitem_out_stock(dto.getOut_stock());
		titem.setTranitem_out_bin_area(dto.getOut_bin_area());
		titem.setTranitem_out_bin_pos(dto.getOut_bin_pos());
		titem.setTranitem_out_bin_rack(dto.getOut_bin_rack());
		titem.setTranitem_out_bin_code(dto.getOut_bin_code());
		titem.setTranitem_measure_unit(dto.getMeasure_unit());
		titem.setTranitem_price(dto.getPrice());
		titem.setTranitem_product_code(dto.getProduct_code());
		titem.setTranitem_product_name(dto.getSmall_id());
		titem.setTranitem_quantity(dto.getItem_qty());
		titem.setTranitem_spec_model(dto.getSpec_model());
		itemReps.save(titem);
		
		// 添加审核消息
		msgService.addAuditMsg(toe.getTranid(), dto.getMt_feature(), "09", dto.getMake_person(), "调拨单");
		
		return toe;
	}

	@Transactional
	@Override
	public void audit(AuditTransferOrderDto dto) throws Exception {
		Date now = new Date();
		String auditDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
		// 更新调拨单
		orderReps.auditOrder(dto.getAudit_status(), dto.getAudit_person_name(), dto.getReject_reason(), auditDateTime,
				dto.getTran_id());

		// 更新物料列表
		for (int i = 0; i < dto.getItem_list().size(); i++) {
			AuditTransferItemDto it = dto.getItem_list().get(i);
			itemReps.auditOrderItem(it.getBarcode(), it.getAct_qty_tran(), it.getTran_id(),
					it.getProduct_code(), it.getTranitem_out_stock(), it.getTranitem_out_bin_code(),
					it.getTranitem_in_stock(),it.getTranitem_in_bin_code());
			if(dto.getAudit_status().equals("06")) {//库管审核通过时，才更新物料库存
				// 减少调出仓库的库存（调出仓库减库存）
				MaterialEntity mt = mReps.findByBarcode(it.getBarcode());
				mReps.update(-it.getAct_qty_tran(), -it.getAct_qty_tran(), mt.getMaterialid());// 调出库减库存         
				
				// 添加库存台账（调出）
				InventoryBookEntity ibeO = new InventoryBookEntity();
				ibeO.setMt_id(mt.getMaterialid());
				ibeO.setCre_date(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				ibeO.setIb_type("09");//台账类型：01采购入库、02归还入库、03其它入库、04加工入库、05调拨入库、06借用出库、07领料出库、08加工出库、09调拨出库
				ibeO.setOrder_id(dto.getTran_id());
//				ibeO.setItem_id(mt.getMaterialid());
				ibeO.setIn_out_type("02");//收入支出类型：01收入、02支出
				ibeO.setLast_qty(mt.getMt_in_remain_quantity());// 期初结存 就是 库存更新前的 实际库存
				ibeO.setIn_out_qty(it.getAct_qty_tran());// 本次的调出库数量
				ibeO.setCur_qty(mt.getMt_in_remain_quantity()-it.getAct_qty_tran());//当期结存 =（期初结存 - 调出库数量）
				ibReps.save(ibeO);
				// TODO: 调拨的调入逻辑待完善
				// 调入的物料条形码，仓库、仓位有变化
				String newBarCode = it.getBarcode().substring(0, 6)+it.getTranitem_in_stock()+it.getTranitem_in_bin_code()+it.getBarcode().substring(6, 9);
				MaterialEntity mtNewTemp= mReps.findByBarcode(newBarCode);
				long mtId = 0L;
				if (mtNewTemp== null) {// 添加新物料
					mReps.insert(mt.getSup_id(), it.getTranitem_in_stock(), mt.getStockbin_area().getSbin_id(), mt.getStockbin_rack().getSbin_id(),
							mt.getStockbin_pos().getSbin_id(), it.getProduct_code(), mt.getMt_name(), mt.getMt_fullname(), newBarCode,
							mt.getCategory_big().getId(), mt.getCategory_small().getId(), mt.getMt_feature(), mt.getMt_measure_unit(),
							it.getAct_qty_tran(), it.getAct_qty_tran(), "01", "01", mt.getMt_min_quantity(), mt.getMt_max_quantity());
																	   //01在用、01正常
					MaterialEntity mtNew= mReps.findByBarcode(newBarCode);//新增后，回查物料ID
					mtId = mtNew.getMaterialid();
					                                                   
				} else {// 更新库存
					mtId = mtNewTemp.getMaterialid();
					mReps.update(it.getAct_qty_tran(), it.getAct_qty_tran(), mtId);
				}				
				
				// 暂时仅更新了调出仓库库存，调入仓库库存暂时没有更新（由于没有新的条形码）
				// 添加库存台账（调入）
				InventoryBookEntity ibeI = new InventoryBookEntity();
				ibeI.setMt_id(mtId);
				ibeI.setCre_date(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				ibeI.setIb_type("05");//台账类型：01采购入库、02归还入库、03其它入库、04加工入库、05调拨入库、06借用出库、07领料出库、08加工出库、09调拨出库
				ibeI.setOrder_id(dto.getTran_id());
				ibeI.setItem_id(it.getItem_id());// 栏目ID
				ibeI.setIn_out_type("01");//收入支出类型：01收入、02支出
				ibeI.setLast_qty(mt.getMt_in_remain_quantity());// 期初结存 就是 库存更新前的 实际库存
				ibeI.setIn_out_qty(it.getAct_qty_tran());// 本次的调出库数量
				ibeI.setCur_qty(mt.getMt_in_remain_quantity()-it.getAct_qty_tran());//当期结存 =（期初结存 - 调出库数量）
				ibReps.save(ibeI);
			}
		}
		
		// 添加审核记录
		auditReps.create(dto.getAudit_person_id(), dto.getReject_reason(), "06", dto.getAudit_status(),
				auditDateTime, dto.getTran_id());
		
		String auditRecords = " -> 库管审核(s%)s% ";
		// 更新审核消息
		msgReps.updateAuditMsg(auditDateTime, String.format(auditRecords, dto.getAudit_person_name(),
				dto.getAudit_status().equals("06")?"通过  -> 完成":"驳回"), dto.getTran_id(), "09");
		
	}

}
