package com.johe.api.pump.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.dto.AuditInOrderDto;
import com.johe.api.pump.dto.AuditInOrderItemDto;
import com.johe.api.pump.dto.InOrderDto;
import com.johe.api.pump.dto.InOrderItemDto;
import com.johe.api.pump.dto.ScanInOrderDto;
import com.johe.api.pump.entity.InOrderEntity;
import com.johe.api.pump.entity.InOrderItemEntity;
import com.johe.api.pump.entity.InventoryBookEntity;
import com.johe.api.pump.entity.MaterialEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.AuditRecordRepository;
import com.johe.api.pump.repository.InOrderItemRepository;
import com.johe.api.pump.repository.InOrderRepository;
import com.johe.api.pump.repository.InventoryBookRepository;
import com.johe.api.pump.repository.MaterialRepository;
import com.johe.api.pump.repository.MessageRepository;
import com.johe.api.pump.service.InOrderService;
import com.johe.api.pump.service.MessageService;
import com.johe.api.pump.service.SeqNumberService;
import com.johe.api.pump.util.AppConstants;
import com.johe.api.pump.util.AppUtil;

@Service
public class InOrderServiceImpl implements InOrderService {

	@Autowired
	InOrderRepository inOrderReps;

	@Autowired
	InOrderItemRepository inItemReps;

	@Autowired
	AuditRecordRepository auditReps;

	@Autowired
	MaterialRepository mReps;

	@Autowired
	MessageRepository msgReps;
	
	@Autowired
	InventoryBookRepository ibReps;
	
	@Autowired
	MessageService msgService;

	@Autowired
	SeqNumberService seqService;

	@Transactional
	@Override
	public InOrderEntity save(InOrderDto dto) throws Exception {
		// 校验
		checkDto(dto);

		// 保存入库单
		InOrderEntity inOrderEntity = saveInOrder(dto);

		// 批量保存入库单栏目
		List<InOrderItemDto> itemList = dto.getItem_list();
		for (int i = 0; i < itemList.size(); i++) {
			InOrderItemDto itemDto = itemList.get(i);

			saveItem(inOrderEntity.getSinid(), dto.getSupply(), itemDto);

		}

		// 添加审核消息 02采购入库、03归还入库、04其他入库、05加工入库
		msgService.addAuditMsg(inOrderEntity.getSinid(), dto.getFeature(), dto.getIn_type(), dto.getMake_person(),getMsgType(dto.getIn_type()));
		

		return inOrderEntity;
	}

	private String getMsgType(String type) {
		String msg = "";
		switch(type) {
		case "02":
			msg = "采购入库";
			break;
		case "03":
			msg = "归还入库";
			break;
		case "04":
			msg = "其他入库";
			break;
		case "05":
			msg = "加工入库";
			break;
		}
		return msg;
	}
	
	private InOrderItemEntity saveItem(long sinId, long supplyId, InOrderItemDto itemDto) {
		InOrderItemEntity ie = new InOrderItemEntity();
		ie.setSinid(sinId);
		ie.setProduct_code(itemDto.getProduct_code());
		ie.setProduct_name(itemDto.getSmall_id());// 小类ID
		ie.setBarcode(itemDto.getBarcode().length()<18?itemDto.getBarcode()+"0000":itemDto.getBarcode());// 条形码
		ie.setBrand(itemDto.getBrand());
		ie.setSupply(supplyId);// 供应商为入库单中
		ie.setCategory(itemDto.getBig_id());// 大类ID
		ie.setMt_feature(itemDto.getFeature());
		ie.setSpec_model(itemDto.getSpec_model());
		ie.setReceipt_stock(itemDto.getStorage());
		ie.setStock_bin_area(itemDto.getSbin_area());
		ie.setStock_bin_rack(itemDto.getSbin_rack());
		ie.setStock_bin_pos(itemDto.getSbin_pos());
		ie.setStock_bin_code(itemDto.getStockbin_code());//
		ie.setAmount(itemDto.getAmount());
		ie.setMeasure_unit(itemDto.getMeasure_unit());
		ie.setPrice(itemDto.getPrice());
		ie.setAssist_attr(itemDto.getAssist_attr());
		ie.setUnit_cost(itemDto.getUnit_cost());
		ie.setItem_qty(itemDto.getItem_qty());//应入数量
		ie.setAct_qty_recv(itemDto.getItem_qty());//实入数量
		ie.setBuy_date(itemDto.getBuy_date());
		ie.setRecv_date(itemDto.getRecv_date());
		ie.setExpir_date(itemDto.getExpired());
		ie.setValid_unti(itemDto.getValid_until());
		ie.setReceipt_total(itemDto.getReceipt_total());

		return inItemReps.save(ie);
	}

	

	/*
	 * @Transactional
	 * 
	 * @Override public InOrderEntity audit(String bizType, long orderId, long
	 * auditUserId, String status, String auditSuggestion) { Date now = new Date();
	 * // 更新入库单状态 InOrderEntity ioe = new InOrderEntity(); ioe.setSinid(orderId);
	 * ioe.setSin_audit_person(Long.toString(auditUserId));
	 * ioe.setSin_audit_date(new SimpleDateFormat("yyyy-MM-dd").format(now));
	 * ioe.setSin_reject_reason(auditSuggestion); ioe.setSin_status(status);
	 * inOrderReps.audit(auditUserId, new
	 * SimpleDateFormat("yyyy-MM-dd").format(now), auditSuggestion, status,
	 * orderId); // 添加审核记录 auditReps.create(auditUserId, auditSuggestion, bizType,
	 * status, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now), orderId);
	 * return ioe; }
	 */

	private ResultEntity<String> checkDto(InOrderDto dto) {
		if ((dto != null && StringUtils.isNotBlank(dto.getIn_type()) && StringUtils.isNotBlank(dto.getMake_inst()))
				|| AppConstants.IN_ORDER_TYPE_BUY.equals(dto.getIn_type())
				|| AppConstants.IN_ORDER_TYPE_RETURN.equals(dto.getIn_type())
				|| AppConstants.IN_ORDER_TYPE_OTHER.equals(dto.getIn_type())
				|| AppConstants.IN_ORDER_TYPE_PRO.equals(dto.getIn_type())) {

		} else {
			return new ResultEntity<String>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}

		List<InOrderItemDto> itemList = dto.getItem_list();
		boolean errFlg = false;
		// 校验物料列表（栏目）
		for (int i = 0; i < itemList.size(); i++) {
			if (!checkItemDto(itemList.get(i))) {
				errFlg = true;
				break;
			}
		}
		if (errFlg) {
			return new ResultEntity<String>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}

		return new ResultEntity<String>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(), "OK");
	}

	private InOrderEntity saveInOrder(InOrderDto dto) {
		InOrderEntity o = new InOrderEntity();
		o.setSin_type(dto.getIn_type());// 类型
		o.setSin_sn(seqService.queryByBiztype(AppConstants.SN_PREFIX_MAP.get("IN"+dto.getIn_type())));// 入库单号
		o.setSin_batch_no(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));// 批次号
		o.setSin_datetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		o.setSin_receive_stock(dto.getSin_receive_stock());// 收货仓库
		o.setSin_make_person(dto.getMake_person());// 制单人
		o.setSin_make_institution(dto.getMake_inst());// 制单机构
		o.setSin_dept(dto.getDept());
		o.setMt_feature(dto.getFeature());// 特性
		o.setSin_supply(dto.getSupply());
		String kgStatus = "01";//待审
		if(dto.getFeature().equals("01") || dto.getFeature().equals("02")) {
			kgStatus="10";//普通/市采，直接状态设为“10库管待审核”
		}
		o.setSin_status(kgStatus);// 状态
		o.setSin_acceptor(dto.getAcceptor());
		o.setSin_keeper(dto.getKeeper());
		o.setSin_leader(dto.getLeader());
		o.setSin_salesman(dto.getSalesman());
		o.setSin_tally_person(dto.getTally_person());

		return inOrderReps.save(o);
	}

	private boolean checkItemDto(InOrderItemDto dto) {
		if (dto != null && StringUtils.isNotBlank(dto.getBarcode()) && StringUtils.isNotBlank(dto.getFeature())
				&& StringUtils.isNotBlank(dto.getProduct_code())) {
			if (StringUtils.isNotBlank(dto.getValid_until()) && AppUtil.isValidDateForyyyyMMdd(dto.getValid_until())
					&& StringUtils.isNotBlank(dto.getBuy_date()) && AppUtil.isValidDateForyyyyMMdd(dto.getBuy_date())
					&& StringUtils.isNotBlank(dto.getRecv_date())
					&& AppUtil.isValidDateForyyyyMMdd(dto.getRecv_date())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Transactional
	@Override
	public InOrderEntity save(ScanInOrderDto dto) throws Exception {
		// 入库单
		InOrderEntity ioe = new InOrderEntity();
		ioe.setMt_feature(dto.getFeature());
		ioe.setSin_acceptor(dto.getAcceptor());
		ioe.setSin_datetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		ioe.setSin_dept(dto.getDept());
		ioe.setSin_keeper(dto.getKeeper());
		ioe.setSin_leader(dto.getLeader());
		ioe.setSin_make_institution(dto.getMake_inst());
		ioe.setSin_make_person(dto.getMake_person());
		ioe.setSin_receive_stock(dto.getStorage_id());
		ioe.setSin_salesman(dto.getSalesman());
		ioe.setSin_sn(seqService.queryByBiztype(AppConstants.SN_PREFIX_MAP.get("IN"+dto.getIn_type())));// 编号
		// ioe.setSin_batch_no(sin_batch_no);//批次号
		ioe.setSin_summary(dto.getSummary());
		ioe.setSin_supply(dto.getSupply_id());
		ioe.setSin_tally_person(dto.getTally_person());
		ioe.setSin_type(dto.getIn_type());
		if("01".equals(dto.getFeature()) || "02".equals(dto.getFeature())) {
			ioe.setSin_status("10");//普通/市采的物料，直接进入库管审核10
		}else {
			ioe.setSin_status("01");//重要/关键的物料，正常走二级或三级审核流程
		}
		InOrderEntity en = inOrderReps.save(ioe);

		// 物料栏目
		InOrderItemEntity it = new InOrderItemEntity();
		it.setSinid(en.getSinid());
		it.setAct_qty_recv(dto.getAct_qty_recv());
		it.setAmount(0.00);
		it.setAssist_attr(dto.getAssist_attr());
		it.setBarcode(dto.getBarcode().length()<18 ? dto.getBarcode() + "0000":dto.getBarcode());
		it.setBrand(dto.getBrand());
		it.setBuy_date(dto.getBuy_date());
		it.setCategory(dto.getBig_id());
		it.setExpir_date(dto.getExpired());
		it.setMeasure_unit(it.getMeasure_unit());
		it.setMt_feature(dto.getFeature());
		it.setPrice(it.getPrice());
		it.setProduct_name(dto.getSmall_id());
		it.setProduct_code(dto.getProduct_code());
		it.setItem_qty(dto.getItem_qty());
		it.setSpec_model(dto.getSpec_model());
		it.setStock_bin_area(dto.getSbin_area());
		it.setStock_bin_rack(dto.getSbin_rack());
		it.setStock_bin_pos(dto.getSbin_pos());
		it.setSupply(dto.getSupply_id());
		it.setReceipt_stock(dto.getStorage_id());
		it.setRecv_date(dto.getRecv_date());
		it.setUnit_cost(dto.getUnit_cost());
		it.setValid_unti(dto.getValid_until());

		inItemReps.save(it);

		// 添加待审核消息
		msgService.addAuditMsg(en.getSinid(), dto.getFeature(), dto.getIn_type(), dto.getMake_person(),getMsgType(dto.getIn_type()));
		return ioe;
	}

	@Transactional
	@Override
	public void audit(AuditInOrderDto dto) throws Exception {
		Date now = new Date();
		String auditDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
		// 更新入库单
		inOrderReps.auditOrder(dto.getAudit_status(), dto.getAudit_person_name(), dto.getReject_reason(), auditDateTime,
				dto.getOrder_id());

		// 更新物料列表
		for (int i = 0; i < dto.getItem_list().size(); i++) {
			AuditInOrderItemDto it = dto.getItem_list().get(i);
			inItemReps.auditOrderItem(it.getBarcode(), it.getAct_qty_recv(), it.getSin_id(),
					it.getProduct_code(), it.getRecv_st_id(), it.getStockbin_code());
			
			// 库管审核通过时，才更新物料库存
			if (dto.getAudit_status().equals("06")) {
					MaterialEntity mt = mReps.findByBarcode(it.getBarcode());
					if (mt == null) {// 添加新物料
						mReps.insert(it.getSupply_id(), it.getRecv_st_id(), it.getArea_id(), it.getRack_id(),
								it.getPos_id(), it.getProduct_code(), it.getMt_name(), it.getMt_fullname(), it.getBarcode(),
								it.getBig_id(), it.getSmall_id(), it.getMt_feature(), it.getMea_unit(),
								it.getAct_qty_recv(), it.getAct_qty_recv(), "01", "01", it.getMin_qty(), it.getMax_qty());
						                                                   //默认 01在用、01正常
					} else {// 更新库存
						mReps.update(it.getAct_qty_recv(), it.getAct_qty_recv(), mt.getMaterialid());
					}
					// 添加库存台账（一种物料，对应一条台账记录）
					InventoryBookEntity ibe = new InventoryBookEntity();
					ibe.setMt_id(mt.getMaterialid());
					ibe.setCre_date(auditDateTime);
					ibe.setIb_type(dto.getIn_type());
					ibe.setOrder_id(dto.getOrder_id());
					ibe.setItem_id(mt.getMaterialid());
					ibe.setIn_out_type("01");//收入支出类型：01收入、02支出
					ibe.setLast_qty(mt.getMt_in_remain_quantity());// 期初结存 就是 库存更新前的 实际库存
					ibe.setIn_out_qty(it.getAct_qty_recv());// 本次的出入库数量
					ibe.setCur_qty(mt.getMt_in_remain_quantity()+it.getAct_qty_recv());//当期结存 =（期初结存+入库数量）
					ibReps.save(ibe);
			}
		}

		// 添加库管审核记录
		auditReps.create(dto.getAudit_person_id(), dto.getReject_reason(), dto.getIn_type(), dto.getAudit_status(),
				auditDateTime, dto.getOrder_id());
		String auditRecords = " -> 库管审核(s%)s% ";
		String biz_type="05";//加工入库02采购入库、03归还入库、04其他入库、05加工入库
		if(dto.getIn_type().equals("02")) {//02采购入库、03归还入库、04其他入库、05加工入库
			biz_type="02";
		}else if(dto.getIn_type().equals("03")){
			biz_type="03";
		}else if(dto.getIn_type().equals("04")){
			biz_type="04";
		}
		
		// 更新审核消息
		msgReps.updateAuditMsg(auditDateTime, String.format(auditRecords, dto.getAudit_person_name(),
				dto.getAudit_status().equals("06")?"通过  -> 完成":"驳回"), dto.getOrder_id(), biz_type);
	}
}
