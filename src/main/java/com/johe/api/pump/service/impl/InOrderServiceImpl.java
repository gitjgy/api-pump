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
import com.johe.api.pump.dto.AuditInOrderItemDto;
import com.johe.api.pump.dto.BarcodeIsUsableDto;
import com.johe.api.pump.dto.InOrderDto;
import com.johe.api.pump.dto.InOrderItemDto;
import com.johe.api.pump.dto.ScanInOrderDto;
import com.johe.api.pump.entity.CategoryEntity;
import com.johe.api.pump.entity.InOrderEntity;
import com.johe.api.pump.entity.InOrderItemEntity;
import com.johe.api.pump.entity.InventoryBookEntity;
import com.johe.api.pump.entity.MaterialEntity;
import com.johe.api.pump.entity.MessageEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.AuditRecordRepository;
import com.johe.api.pump.repository.CategoryRepository;
import com.johe.api.pump.repository.InOrderItemRepository;
import com.johe.api.pump.repository.InOrderRepository;
import com.johe.api.pump.repository.InventoryBookRepository;
import com.johe.api.pump.repository.MaterialRepository;
import com.johe.api.pump.repository.MessageRepository;
import com.johe.api.pump.repository.OutOrderRepository;
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
	
	@Autowired
	CategoryRepository categoryReps;

	@Autowired
	OutOrderRepository outOrderReps;
	
	@Transactional
	@Override
	public InOrderEntity save(InOrderDto dto) throws Exception {
		// 校验
		checkDto(dto);

		// 保存入库单
		InOrderEntity inOrderEntity = saveInOrder(dto);
		boolean autoFlg = true;//市采特性的物料自动审核通过（列表中所有都是市采的）
		AuditInOrderDto aiDto = new AuditInOrderDto();
		List<InOrderItemEntity> autoItemList = new ArrayList<InOrderItemEntity>();
		// 批量保存入库单栏目
		List<InOrderItemDto> itemList = dto.getItem_list();
		for (int i = 0; i < itemList.size(); i++) {
			InOrderItemDto it = itemList.get(i);
			
//			// 自动生成条码，唯一
			String newBarCode =it.getBarcode();
//			if(it.getBarcode().length()<18) {
//				List<MaterialEntity> mlist= mReps.searchByBarcodeLike(it.getBarcode().substring(0, 14));
//				if(mlist != null && mlist.size() > 0) {
//					newBarCode = mReps.makeBarcode(it.getBarcode());
//				}else {
//					newBarCode+="0000";
//				}
//			}
			
			//自动改为手动录入
			
			
			InOrderItemEntity itret = saveItem(inOrderEntity.getSinid(), dto.getSupply(), it);
			if("02".equals(itret.getMt_feature())) {//若特性是市采02
				//准备自动审核的数据
				InOrderItemEntity iie = new InOrderItemEntity();
				iie.setAct_qty_recv(it.getItem_qty());
				iie.setBarcode(newBarCode);
				CategoryEntity bigEntity = new CategoryEntity();
				bigEntity.setId(it.getBig_id());
				bigEntity.setName(it.getBig_name());
				iie.setBigCategoryEntity(bigEntity);
				CategoryEntity smallEntity = new CategoryEntity();
				smallEntity.setId(it.getSmall_id());
				smallEntity.setName(it.getSmall_name());
	//			smallEntity.setMt_min_quantity(itret);
				iie.setSupply(it.getSupply_name());
				iie.setSmallCategoryEntity(smallEntity);
				iie.setBrand(it.getBrand());
				iie.setCategory(it.getBig_id());
				iie.setItem_qty(it.getAct_qty_recv());
				iie.setItemid(itret.getItemid());
				iie.setMeasure_unit(it.getMeasure_unit());
				iie.setMt_feature(it.getFeature());
				iie.setOpen_invoice(it.getOpen_invoice());
				iie.setPrice(it.getPrice());
				iie.setProduct_code(it.getProduct_code());
				iie.setProduct_name(it.getSmall_id());
				iie.setReceipt_stock(it.getStorage());
				iie.setSinid(inOrderEntity.getSinid());
				iie.setSpec_model(it.getSpec_model());
				iie.setStock_bin_area(it.getSbin_area());
				iie.setStock_bin_code(it.getStockbin_code());
				iie.setStock_bin_pos(it.getSbin_pos());
				iie.setStock_bin_rack(it.getSbin_rack());
				iie.setSupply(dto.getSupply());
				autoItemList.add(iie);
			}
			if(!"02".equals(itret.getMt_feature())) {//若有一个不是“市采”02，就不自动审核
				autoFlg = false;
			}
		}	
			

		if(autoFlg == true) {
			try {
			aiDto.setAudit_person_id(dto.getMake_person());
			aiDto.setAudit_person_name(dto.getAcceptor());
			aiDto.setAudit_status("06");
			aiDto.setIn_type(dto.getIn_type());
			aiDto.setOrder_id(inOrderEntity.getSinid());			
			aiDto.setRecv_st_id(dto.getSin_receive_stock());
			aiDto.setItem_list(autoItemList);
			audit(aiDto);
			
			//添加审核消息
			Date now = new Date();
			String strNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
			MessageEntity msg = new MessageEntity();
			msg.setAudit_post("03");
			msg.setAudit_records("市采物料，系统自动审核通过");
			msg.setBiz_type(dto.getIn_type());
			msg.setMsg_content("有一条物料["+getMsgType(dto.getIn_type())+"]，市采物料，系统自动审核通过！");
			msg.setSend_time(strNow);
			msg.setCre_time(strNow);
			msg.setFinish_time(strNow);
			msg.setMsg_creator(dto.getMake_person());
			msg.setRelate_id(inOrderEntity.getSinid());

			msgReps.save(msg);
			}catch(Exception ex) {
				System.out.println(">>>>>>>>>>>>>>"+ex.getMessage());
			}
			
			
		}else {
			// 添加审核消息 02采购入库、03归还入库、04其他入库、05加工入库
			msgService.addAuditMsg(inOrderEntity.getSinid(), dto.getFeature(), dto.getIn_type(), dto.getMake_person(),getMsgType(dto.getIn_type()));
		}

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
//		boolean illegalFlg = false;//如果此条码不是被借出的，将不能归还入库
		// 校验物料列表（栏目）
		for (int i = 0; i < itemList.size(); i++) {
			if (!checkItemDto(itemList.get(i))) {// 检验参数是否合法
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
		Date now = new Date();
		o.setSin_type(dto.getIn_type());// 类型
		o.setSin_sn(seqService.queryByBiztype(AppConstants.SN_PREFIX_MAP.get("IN"+dto.getIn_type())));// 入库单号
		o.setSin_batch_no(new SimpleDateFormat("yyyyMMddHHmmss").format(now));// 批次号
		o.setSin_datetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now));
		o.setSin_receive_stock(dto.getSin_receive_stock());// 收货仓库
		o.setSin_make_person(dto.getMake_person());// 制单人
		o.setSin_make_institution(dto.getMake_inst());// 制单机构
		o.setSin_dept(dto.getDept());
		o.setMt_feature(dto.getFeature());// 特性
		o.setSin_supply(dto.getSupply());
		String kgStatus = "01";//待审(物料特性：01普通、02市采、03重要、04关键 )
		if(dto.getFeature().equals("01")) {//普通
			kgStatus="10";//直接状态设为“10库管待审”
		}else if(dto.getFeature().equals("02")) {
			kgStatus="08";//市采，直接状态设为“08库管审核通过”，自动审核
		}else if(dto.getFeature().equals("03") || dto.getFeature().equals("04")) {
			kgStatus="01";//重要、关键，直接状态设为“01主管待审”
		}
		o.setSin_status(kgStatus);// 状态
		o.setSin_acceptor(dto.getAcceptor());
		o.setSin_keeper(dto.getKeeper());
		o.setSin_leader(dto.getLeader());
		o.setSin_salesman(dto.getSalesman());
		o.setSin_tally_person(dto.getTally_person());
		o.setReturn_person(dto.getAcceptor());

		return inOrderReps.save(o);
	}

	private boolean checkItemDto(InOrderItemDto dto) {
		if (dto != null && StringUtils.isNotBlank(dto.getBarcode()) && dto.getBarcode().length() == 18 && StringUtils.isNotBlank(dto.getFeature())
				&& StringUtils.isNotBlank(dto.getProduct_code())) {
			if (StringUtils.isNotBlank(dto.getValid_until()) && AppUtil.isValidDateForyyyyMMdd(dto.getValid_until())
					&& StringUtils.isNotBlank(dto.getBuy_date()) && AppUtil.isValidDateForyyyyMMdd(dto.getBuy_date())
					&& StringUtils.isNotBlank(dto.getRecv_date())
					&& AppUtil.isValidDateForyyyyMMdd(dto.getRecv_date())) {
				return true;
			} else {
				return false;
			}
			//如果入库类型是“归还”，则校验是否为“借出”状态，只有被借出的物料，才可以走归还入库
			
		} else {
			return false;
		}
	}

	@Transactional
	@Override
	public InOrderEntity save(ScanInOrderDto dto) throws Exception {
		// 入库单
		InOrderEntity ioe = new InOrderEntity();
		Date now = new Date();
		ioe.setMt_feature(dto.getFeature());
		ioe.setSin_acceptor(dto.getAcceptor());
		ioe.setSin_datetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now));
		ioe.setSin_dept(dto.getDept());
		ioe.setSin_keeper(dto.getKeeper());
		ioe.setSin_leader(dto.getLeader());
		ioe.setSin_make_institution(dto.getMake_inst());
		ioe.setSin_make_person(dto.getMake_person());
		ioe.setSin_receive_stock(dto.getStorage_id());
		ioe.setSin_salesman(dto.getSalesman());
		ioe.setSin_sn(seqService.queryByBiztype(AppConstants.SN_PREFIX_MAP.get("IN"+dto.getIn_type())));// 编号
		ioe.setSin_batch_no(new SimpleDateFormat("yyyyMMddHHmmss").format(now));;//批次号
		ioe.setSin_summary(dto.getSummary());
		ioe.setSin_supply(dto.getSupply_id());
		ioe.setSin_tally_person(dto.getTally_person());
		ioe.setSin_type(dto.getIn_type());
//		if("02".equals(dto.getFeature())) {
//			ioe.setSin_status("06");//普通/市采的物料，直接进入库管审核10
//		}else {
//			ioe.setSin_status("01");//重要/关键的物料，正常走二级或三级审核流程
//		}
//		
		String kgStatus = "01";//待审(物料特性：01普通、02市采、03重要、04关键 )
		if(dto.getFeature().equals("01")) {//普通
			kgStatus="10";//直接状态设为“10库管待审”
		}else if(dto.getFeature().equals("02")) {
			kgStatus="08";//市采，直接状态设为“08库管审核通过”，自动审核
		}else if(dto.getFeature().equals("03") || dto.getFeature().equals("04")) {
			kgStatus="01";//重要、关键，直接状态设为“01主管待审”,//重要/关键的物料，正常走二级或三级审核流程
		}
		ioe.setSin_status(kgStatus);
		
		InOrderEntity en = inOrderReps.save(ioe);

		// 物料栏目
		InOrderItemEntity it = new InOrderItemEntity();
		it.setSinid(en.getSinid());
		it.setAct_qty_recv(dto.getItem_qty());
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
		//==============================================
		//市采特性的物料自动审核通过（列表中所有都是市采的）
		boolean autoFlg = true;
		AuditInOrderDto aiDto = new AuditInOrderDto();
		//
		/*
		// 自动生成条码，唯一
		String newBarCode =it.getBarcode();
		if(it.getBarcode().length()<18) {
			newBarCode = mReps.makeBarcode(it.getBarcode());
		}
		if("02".equals(en.getMt_feature())) {//若特性是市采02
			//准备自动审核的数据
			InOrderItemEntity iie = new InOrderItemEntity();
			iie.setAct_qty_recv(it.getItem_qty());
			iie.setBarcode(newBarCode);
			CategoryEntity bigEntity = new CategoryEntity();
			bigEntity.setId(ioe.getBig_id());
			bigEntity.setName(ioe.getBig_name());
			iie.setBigCategoryEntity(bigEntity);
			CategoryEntity smallEntity = new CategoryEntity();
			smallEntity.setId(ioe.getSmall_id());
			smallEntity.setName(ioe.getSmall_name());
//			smallEntity.setMt_min_quantity(itret);
			iie.setSupply(it.getSupply_name());
			iie.setSmallCategoryEntity(smallEntity);
			iie.setBrand(it.getBrand());
			iie.setCategory(it.getBig_id());
			iie.setItem_qty(it.getAct_qty_recv());
			iie.setItemid(itret.getItemid());
			iie.setMeasure_unit(it.getMeasure_unit());
			iie.setMt_feature(it.getFeature());
			iie.setOpen_invoice(it.getOpen_invoice());
			iie.setPrice(it.getPrice());
			iie.setProduct_code(it.getProduct_code());
			iie.setProduct_name(it.getSmall_id());
			iie.setReceipt_stock(it.getStorage());
			iie.setSinid(inOrderEntity.getSinid());
			iie.setSpec_model(it.getSpec_model());
			iie.setStock_bin_area(it.getSbin_area());
			iie.setStock_bin_code(it.getStockbin_code());
			iie.setStock_bin_pos(it.getSbin_pos());
			iie.setStock_bin_rack(it.getSbin_rack());
			iie.setSupply(dto.getSupply());
		}*/
		/*
		if(!"02".equals(en.getMt_feature())) {//若有一个不是“市采”02，就不自动审核
			autoFlg = false;
		}*/

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
			//AuditInOrderItemDto it = dto.getItem_list().get(i);
			InOrderItemEntity it = dto.getItem_list().get(i);
			inItemReps.auditOrderItem(it.getBarcode(), it.getAct_qty_recv(), dto.getOrder_id(),
					it.getProduct_code(), it.getReceipt_stock(), it.getStock_bin_code());
			
			// 库管审核通过时，才更新物料库存
			if (dto.getAudit_status().equals("06")) {
					CategoryEntity big = categoryReps.findOne(it.getCategory());
					CategoryEntity small = categoryReps.findOne(it.getProduct_name());
					String inType = dto.getIn_type();//02采购入库、03归还入库、04其他入库、05加工入库
					
					MaterialEntity mtExist = mReps.getByBarcode(it.getBarcode(), it.getReceipt_stock());
					double totalQty = it.getAct_qty_recv();
					double initQty = 0;
					if(mtExist != null) {
						totalQty=mtExist.getMt_in_total_quantity()+it.getAct_qty_recv();
						initQty = mtExist.getMt_in_total_quantity();
					}
					//采购、加工、其他入库，一物一条码，新增一条物料信息；  
					if("02".equals(inType) || "04".equals(inType) || "05".equals(inType)) {
						//更新此类物料的所有库存
						if(mtExist != null) {//若存在
							mReps.addQtyByBarcode14(it.getAct_qty_recv(), it.getBarcode(), it.getReceipt_stock());
						}
						mReps.insert(it.getSupply(), it.getReceipt_stock(), it.getStock_bin_area(), it.getStock_bin_rack(),
								it.getStock_bin_pos(), it.getProduct_code(), small.getName(), big.getName()+"-"+small.getName(), it.getBarcode(),
								it.getCategory(), it.getProduct_name(), it.getMt_feature(),it.getMeasure_unit(),
								totalQty, it.getAct_qty_recv(), "01", "01", small.getMt_min_quantity(),
								small.getMt_max_quantity(),String.valueOf(it.getPrice()));//默认状态 01在用、01正常	

					}else if("03".equals(inType)) {//归还
						//归还，直接更新条码对应的物料总库存、仓位库存
						mReps.addQtyByBarcode18(it.getAct_qty_recv(), it.getBarcode(), it.getReceipt_stock());
					}
					
					MaterialEntity mt = mReps.findByBarcode(it.getBarcode());
					// 添加库存台账（一种物料，对应一条台账记录）
					InventoryBookEntity ibe = new InventoryBookEntity();
					ibe.setMt_id(mt.getMaterialid());	
					String strbarcode=mt.getBarcode().substring(0, 14);
					ibe.setBarcode(strbarcode);
					ibe.setCre_date(auditDateTime);
					ibe.setIb_type(dto.getIn_type());
					ibe.setOrder_id(dto.getOrder_id());
					ibe.setItem_id(mt.getMaterialid());
					ibe.setIn_out_type("01");//收入支出类型：01收入、02支出
					ibe.setLast_qty(initQty);// 期初结存 就是 库存更新前的 实际库存
					ibe.setIn_out_qty(it.getAct_qty_recv());// 本次的出入库数量
					ibe.setCur_qty(initQty+it.getAct_qty_recv());//当期结存 =（期初结存+入库数量）
					ibReps.save(ibe);
			}
		}

		// 添加库管审核记录
		auditReps.create(dto.getAudit_person_id(), dto.getReject_reason(), dto.getIn_type(), dto.getAudit_status(),
				auditDateTime, dto.getOrder_id());
		String biz_type="05";//加工入库02采购入库、03归还入库、04其他入库、05加工入库
		if(dto.getIn_type().equals("02")) {//02采购入库、03归还入库、04其他入库、05加工入库
			biz_type="02";
		}else if(dto.getIn_type().equals("03")){
			biz_type="03";
		}else if(dto.getIn_type().equals("04")){
			biz_type="04";
		}
		
		// 更新审核消息
		msgReps.updateAuditMsg(auditDateTime, " -> 库管审核("+dto.getAudit_person_name()+") "+	(dto.getAudit_status().equals("06")?"通过  -> 完成":"驳回"), 
				dto.getOrder_id(), biz_type);
	}
	
	
	// 校验条形码是否可用
	@Override
	public BarcodeIsUsableDto verifyBarcodeIsUsable(BarcodeIsUsableDto dto) throws Exception{
		BarcodeIsUsableDto result = new BarcodeIsUsableDto();
		result.setBar_code(dto.getBar_code());
		result.setIn_type(dto.getIn_type());
		if(!"03".equals(dto.getIn_type())) {//采购、加工、其它入库时
			long count = inOrderReps.getCountForBarcode(dto.getBar_code());
			if(count > 0) {//有入库记录，说明此条码已被占用，所以不可用
				result.setFlg_usabled(false);
				result.setUnusable_cause("此条码"+dto.getBar_code()+"已被入库单占用，请重新输入！");
			}else {//若无入库记录
				MaterialEntity me = mReps.findByBarcode(dto.getBar_code());
				if(me == null) {
					result.setFlg_usabled(true);
				}else {
					result.setFlg_usabled(false);
					result.setUnusable_cause("此条码"+dto.getBar_code()+"已被占用，请重新输入！");
				}
			}						
		}else if("03".equals(dto.getIn_type())){//归还入库，去查询有没有借出记录
			//查询借出记录
			List<Object> objList = outOrderReps.getPickInfo(dto.getBar_code());
			if(objList == null || objList.size() == 0) {//没有借出记录，则此条码不可用
				result.setFlg_usabled(false);
				result.setUnusable_cause("此条码"+dto.getBar_code()+"没有借出记录，将不能归还！");
			}else {
				result.setFlg_usabled(true);
			}
		}
		return result;	
	}
}
