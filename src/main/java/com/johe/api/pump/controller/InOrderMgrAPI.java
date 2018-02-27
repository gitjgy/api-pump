package com.johe.api.pump.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johe.api.pump.dto.AuditInOrderDto;
import com.johe.api.pump.dto.AuditInOrderItemDto;
import com.johe.api.pump.dto.InOrderDto;
import com.johe.api.pump.dto.ScanInOrderDto;
import com.johe.api.pump.entity.AuditRecordEntity;
import com.johe.api.pump.entity.InOrderEntity;
import com.johe.api.pump.entity.InOrderItemEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.AuditRecordRepository;
import com.johe.api.pump.repository.InOrderItemRepository;
import com.johe.api.pump.repository.InOrderRepository;
import com.johe.api.pump.service.InOrderService;
import com.johe.api.pump.service.MaterialService;
import com.johe.api.pump.util.AppConstants;
import com.johe.api.pump.util.AppUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "入库管理", tags = "【入库管理】接口")
@RequestMapping("/api/v1/inorder")
@RestController
public class InOrderMgrAPI {

	@Autowired
	InOrderRepository stockInOrderReposity;

	@Autowired
	InOrderService inOrderService;
	
	@Autowired
	MaterialService mtService;

	@Autowired
	InOrderItemRepository inOrderItemRepository;

	@Autowired
	AuditRecordRepository auditRecordRepository;

	// 获取入库单列表（全部、待审核、已通过、已驳回）
	@GetMapping("/{status}/{biz_type}/{page}/{size}")
	@ApiOperation(value = "获取入库单列表（全部、待审核、已通过、已驳回）", notes = "获取入库单列表（全部、待审核、已通过、已驳回）")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "status", value = "入库单审核状态：00全部、01待审核、02已通过、03已驳回", allowableValues = "00,01,02,03", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "biz_type", value = "业务类型：00全部、02采购入库、03归还入库、04其它入库、05加工入库", allowableValues = "00,02,03,04,05", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码（从0开始）", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "每页显示条数", dataType = "int", paramType = "query") })
	public ResultEntity<Page<InOrderEntity>> getStockInOrderList(@PathVariable("status") String status,
			@PathVariable("biz_type") String bizType, @PathVariable("page") int page, @PathVariable("size") int size) {
		final String strStatus = status;
		final String strBiztype = bizType;
		final int iPage = page;
		final int iSize = size;
		// 校验传值
		if (strStatus.equals("00") || strStatus.equals("01") || strStatus.equals("02") || strStatus.equals("03")
				|| strBiztype.equals("00") || strBiztype.equals("02") || strBiztype.equals("03")
				|| strBiztype.equals("04") || strBiztype.equals("05")) {
		} else {
			return new ResultEntity<Page<InOrderEntity>>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}

		// 分页获取
		Page<InOrderEntity> orders = stockInOrderReposity.findAll(new Specification<InOrderEntity>() {

			@Override
			public Predicate toPredicate(Root<InOrderEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				// Where条件：状态
				In<String> inStatus = cb.in(root.<String>get("sin_status"));
				if (strStatus.equals("00")) {// 查询 所有类型的全部数据
					inStatus.value("01");// 待审
					inStatus.value("02");// 主管通过
					inStatus.value("03");// 主管驳回
					inStatus.value("04");// 上级通过
					inStatus.value("05");// 上级驳回
					inStatus.value("06");// 库管通过
					inStatus.value("07");// 库管驳回
					inStatus.value("08");// 审核完成
					inStatus.value("10");// 库管待审
				} else {
					if (AppConstants.STOCK_IN_ORDER_AUDIT_STATUS_WAITING.equals(strStatus)) {// 待审核
//						inStatus.value("04");// 上级审过（关键特性的物料，三级流程上级审过后变为04）
//						inStatus.value("02");// 主管审过（重要特性的物料，二级流程主管审过后变为02）
						inStatus.value("10");// 库管待审
//						Predicate p1=;//重要
//						Predicate p11=;//主管审过，重要物料，主管审过后，就轮到库管
//						p1=cb.and(cb.equal(root.<String>get("mt_feature"),"03"), cb.equal(root.<String>get("sin_status"), "02"));
//						p2=cb.and(cb.equal(root.<String>get("mt_feature"),"04"), cb.equal(root.<String>get("sin_status"), "04"));
//						Predicate p101=cb.and(cb.equal(root.<String>get("mt_feature"), cb.equal(root.<String>get("sin_status"), "02")));
						
//						List<Predicate> list1 = new ArrayList<Predicate>();
//						list1.add(p101);
						
//						Predicate p2=cb.equal(root.<String>get("mt_feature"), "04");//关键
//						Predicate p21=cb.equal(root.<String>get("sin_status"), "04");//关键物料，上级领导审过后，就轮到库管
//						Predicate p201=cb.and(p2,p21);
//						List<Predicate> list2 = new ArrayList<Predicate>();
//						list2.add(p201);
						
//						Predicate p200=cb.or(p101,p201);
						
//						p3=cb.equal(root.<String>get("sin_status"), "10");//库管待审
						// 组合
//						Predicate p000=cb.or(p101,p201,p301);
						
//						list.add(cb.or(p101));						
//						list.add(cb.or(p201));						
//						list.add(cb.or(cb.and(p1,p11),cb.and(p2,p21),p301));						
						
					} else if (AppConstants.STOCK_IN_ORDER_AUDIT_STATUS_PASS.equals(strStatus)) {// 已通过
						inStatus.value("02");// 主管通过
						inStatus.value("04");// 上级通过
						inStatus.value("06");// 库管通过
						inStatus.value("08");// 审核完成
					} else if (AppConstants.STOCK_IN_ORDER_AUDIT_STATUS_NO_PASS.equals(strStatus)) {// 已驳回
						inStatus.value("03");// 主管驳回
						inStatus.value("05");// 上级驳回
						inStatus.value("07");// 库管驳回
					}
				}
				list.add(inStatus);
				if (strBiztype.equals("00")) {// 所有类型
					In<String> inType = cb.in(root.<String>get("sin_type"));
					inType.value("02");// 采购入库
					inType.value("03");// 归还入库
					inType.value("04");// 加工入库
					inType.value("05");// 其他入库
					list.add(inType);
				} else {// 指定类型
					list.add(cb.equal(root.<String>get("sin_type"), strBiztype));
				}
				query.orderBy(cb.desc(root.get("sin_datetime"))); // 时间降序
				Predicate[] pres = new Predicate[list.size()];
				pres = list.toArray(pres);

				return cb.and(pres);

			}

		}, new PageRequest(iPage, iSize, null));

		return new ResultEntity<Page<InOrderEntity>>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(), orders);
	}

	// 创建入库单
	@PostMapping("/create")
	@ApiOperation(value = "创建入库单", notes = "创建入库单（采购入库、归还入库、其他入库、加工入库）")
	public ResultEntity<InOrderEntity> createInOrderPost(@ApiParam @RequestBody InOrderDto object) {
		InOrderEntity ioe = null;
		try {
			ioe = inOrderService.save(object);
		} catch (Exception e) {
			return new ResultEntity<InOrderEntity>(ResultStatus.CREATE_FAILED.getCode(), 
					ResultStatus.CREATE_FAILED.getMessage(), null);
		}
		return new ResultEntity<InOrderEntity>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				ioe);
	}

	// 创建入库单（扫码入库）
	@PostMapping("/scancreate")
	@ApiOperation(value = "创建入库单（扫码）", notes = "创建入库单（采购入库、归还入库、其他入库、加工入库）")
	public ResultEntity<InOrderEntity> createInOrderPostScan(@ApiParam @RequestBody ScanInOrderDto object) {
		// 参数校验
		if (!checkScanDto(object)) {
			return new ResultEntity<InOrderEntity>(ResultStatus.CREATE_FAILED.getCode(),
					ResultStatus.CREATE_FAILED.getMessage(), null);
		}
		InOrderEntity ioe = null;
		try {
			ioe = inOrderService.save(object);
		} catch (Exception e) {
			return new ResultEntity<InOrderEntity>(ResultStatus.AUDIT_FAILED.getCode(), 
					ResultStatus.AUDIT_FAILED.getMessage(), null);
		}
		return new ResultEntity<InOrderEntity>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				ioe);
	}

	// 获取物料列表
	@GetMapping("/{order_id}/item/{page}/{size}")
	@ApiOperation(value = "获取物料列表", notes = "获取物料列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "入库单ID", required = true, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码（从0开始）", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "query") })
	public ResultEntity<Page<InOrderItemEntity>> getStockInOrderItemList(@PathVariable("order_id") long id,
			@PathVariable("page") int page, @PathVariable("size") int size) {
		final long orderId = id;
		Page<InOrderItemEntity> orderItems = inOrderItemRepository.findAll(new Specification<InOrderItemEntity>() {

			@Override
			public Predicate toPredicate(Root<InOrderItemEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				// Join<InOrderItemEntity, InOrderEntity> storageJoin =
				// root.join(root.getModel().getSingularAttribute("stockInOrder",InOrderEntity.class),JoinType.LEFT);
				list.add(cb.equal(root.<Long>get("sinid"), orderId));

				query.orderBy(cb.desc(root.get("barcode")));
				Predicate[] pres = new Predicate[list.size()];
				pres = list.toArray(pres);

				return cb.and(pres);
			}

		}, new PageRequest(page, size, null));

		return new ResultEntity<Page<InOrderItemEntity>>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				orderItems);
	}

	// 获取物料详情
	@GetMapping("/{order_id}/item/{item_id}")
	@ApiOperation(value = "获取物料详情", notes = "获取物料详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "入库单ID", required = true, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "item_id", value = "栏目ID", required = true, dataType = "long", paramType = "query") })
	public ResultEntity<InOrderItemEntity> getStockInItemById(@PathVariable("order_id") Long orderId,
			@PathVariable("item_id") Long itemId) {
		return new ResultEntity<InOrderItemEntity>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				inOrderItemRepository.findBySinidAndItemid(orderId, itemId));
	}

	// 查看审核流程
	@GetMapping("/{order_id}/{biz_type}/flow")
	@ApiOperation(value = "获取审批流程列表", notes = "获取审批流程列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "order_id", value = "入库单ID", dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "biz_type", value = "业务类型:02采购入库、03归还入库、04其它入库、05加工入库", allowableValues = "02,03,04,05", dataType = "string", paramType = "query") })
	public ResultEntity<List<AuditRecordEntity>> getInOrderAuditFlow(@PathVariable("order_id") final Long orderId,
			@PathVariable("biz_type") final String bizType) {
		List<AuditRecordEntity> auditReds = auditRecordRepository.findAll(new Specification<AuditRecordEntity>() {

			@Override
			public Predicate toPredicate(Root<AuditRecordEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.<Long>get("relate_id"), orderId));// 关联ID（入库单ID）
				list.add(cb.equal(root.<String>get("biz_type"), bizType));

				query.orderBy(cb.desc(root.get("audit_time")));// 降序

				return cb.and(list.toArray(new Predicate[list.size()]));
			}

		});

		return new ResultEntity<List<AuditRecordEntity>>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				auditReds);
	}

	// 获取入库单物料信息（扫描或输入条形码）
	@GetMapping("/{order_id}/query/{bar_code}")
	@ApiOperation(value = "获取入库单物料信息（扫描或输入条形码）", notes = "获取入库单物料信息（扫描或输入条形码）")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "入库单ID", required = true, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "bar_code", value = "条形码", required = true, dataType = "string", paramType = "query") })
	public ResultEntity<InOrderItemEntity> scanAuditInOrder(@PathVariable("order_id") Long orderId,
			@PathVariable("bar_code") String barcode) {
		System.out.println("[入库审核-条形码]" + barcode);
		return new ResultEntity<InOrderItemEntity>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				inOrderItemRepository.findBySinidAndBarcode(orderId,
						barcode.length() > 18 ? barcode.substring(0, 18) : barcode));// 截掉条形码后面的编号
	}

	// 审核入库单
	@PostMapping("/audit")
	@ApiOperation(value = "审核入库单", notes = "审核入库单（采购入库、归还入库、其他入库、加工入库）")
	public ResultEntity<String> auditInOrderPost(@ApiParam @RequestBody AuditInOrderDto object) {

		if (!checkAuditDto(object)) {
			return new ResultEntity<String>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}
		// 审核（更新入库单、栏目、更新库存、添加审核记录）
		try {
			inOrderService.audit(object);
			
			//检测库存预警（历史）
			for(int i=0;i<object.getItem_list().size();i++) {
				AuditInOrderItemDto it = object.getItem_list().get(i);
				mtService.addHistoryAlarm(it.getBarcode(), it.getAct_qty_recv());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultEntity<String>(ResultStatus.AUDIT_FAILED.getCode(), 
					ResultStatus.AUDIT_FAILED.getMessage(), e.getMessage());
		}

		return new ResultEntity<String>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(), "OK");
	}

	private boolean checkScanDto(ScanInOrderDto dto) {
		if (dto == null || !AppUtil.isOK(dto.getDept(), 32, true) || !AppUtil.isOK(dto.getMake_inst(), 64, true)
				|| !AppUtil.isOK(dto.getSummary(), 64, false) || !AppUtil.isOK(dto.getTally_person(), 32, false)
				|| !AppUtil.isOK(dto.getAcceptor(), 32, false) || !AppUtil.isOK(dto.getKeeper(), 32, false)
				|| !AppUtil.isOK(dto.getSalesman(), 32, false) || !AppUtil.isOK(dto.getLeader(), 32, false)
				|| !AppUtil.isOK(dto.getProduct_code(), 6, true) || !AppUtil.isOK(dto.getSpec_model(), 32, false)
				|| !AppUtil.isOK(dto.getBarcode(), 18, true) || !AppUtil.isOK(dto.getBrand(), 64, false)
				|| !AppUtil.isOK(dto.getStockbin_code(), 5, true) || !AppUtil.isOK(dto.getAssist_attr(), 128, false)
				|| !AppUtil.isOK(dto.getExpired(), 16, false)) {
			return false;
		}
		// 入库类型（采购入库、归还入库、其他入库、加工入库）
		if (StringUtils.isNotBlank(dto.getIn_type()) && (dto.getIn_type().equals("02") || dto.getIn_type().equals("03")
				|| dto.getIn_type().equals("04") || dto.getIn_type().equals("05"))) {
		} else {
			return false;
		}
		// 特性
		if (StringUtils.isNotBlank(dto.getFeature())
				&& (dto.getFeature().equals("01") || dto.getFeature().equals("02") || dto.getFeature().equals("03"))
				|| dto.getFeature().equals("04")) {
		} else {
			return false;
		}

		return true;
	}

	private boolean checkAuditDto(AuditInOrderDto dto) {
		boolean isOk = true;
		if (dto == null || !AppUtil.isOK(dto.getAudit_person_name(), 32, true) || dto.getItem_list() == null) {
			isOk = false;// 审核人、物料列表
		}
		
		// 校验物料
		for(int i=0;i<dto.getItem_list().size();i++) {
			AuditInOrderItemDto item = dto.getItem_list().get(i);
			if(!AppUtil.isOK(item.getMea_unit(), 16, true) 
				|| !AppUtil.isOK(item.getMt_name(), 64, true) 
				|| !AppUtil.isOK(item.getMt_fullname(), 128, true) 
				|| !AppUtil.isOK(item.getMt_feature(), 2, true) 
				|| !AppUtil.isOK(item.getBarcode(), 18, true)) {
				isOk = false;
				break;
			}
			if (StringUtils.isNotBlank(item.getMt_feature()) && (item.getMt_feature().equals("01") 
					|| item.getMt_feature().equals("02")
					|| item.getMt_feature().equals("03") 
					|| item.getMt_feature().equals("04"))) {
			} else {
				isOk = false;
			}
		}
		
		// 入库类型
		if (StringUtils.isNotBlank(dto.getIn_type()) && (dto.getIn_type().equals("02") || dto.getIn_type().equals("03")
				|| dto.getIn_type().equals("04") || dto.getIn_type().equals("05"))) {
		} else {
			isOk = false;
		}
		// 状态
		if (StringUtils.isNotBlank(dto.getAudit_status())
				&& (dto.getAudit_status().equals("06") || dto.getAudit_status().equals("07"))) {
		} else {
			isOk = false;
		}

		// 驳回时，意见必填
		if (dto.getAudit_status().equals("07") && !AppUtil.isOK(dto.getReject_reason(), 200, true)) {
			isOk = false;
		}

		return isOk;
	}

}
