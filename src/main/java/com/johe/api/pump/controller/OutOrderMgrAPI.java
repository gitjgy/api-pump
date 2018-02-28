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

import com.johe.api.pump.dto.AuditOutOrderDto;
import com.johe.api.pump.dto.AuditOutOrderItemDto;
import com.johe.api.pump.dto.OutOrderDto;
import com.johe.api.pump.dto.PickRecordDto;
import com.johe.api.pump.dto.ScanOutOrderDto;
import com.johe.api.pump.entity.AuditRecordEntity;
import com.johe.api.pump.entity.OutOrderEntity;
import com.johe.api.pump.entity.OutOrderItemEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.AuditRecordRepository;
import com.johe.api.pump.repository.OutOrderItemRepository;
import com.johe.api.pump.repository.OutOrderRepository;
import com.johe.api.pump.service.MaterialService;
import com.johe.api.pump.service.OutOrderService;
import com.johe.api.pump.util.AppUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "出库管理", tags = "【出库管理】接口")
@RequestMapping("/api/v1/outorder")
@RestController
public class OutOrderMgrAPI {

	@Autowired
	OutOrderRepository outOrderReps;

	@Autowired
	OutOrderService outOrderService;
	
	@Autowired
	MaterialService mtService;

	@Autowired
	OutOrderItemRepository outOrderItemRepository;

	@Autowired
	AuditRecordRepository auditRecordRepository;

	// 获取出库单列表（全部、待审核、已通过、已驳回）
	@GetMapping("/{status}/{biz_type}/{page}/{size}")
	@ApiOperation(value = "获取出库单列表（全部、待审核、已通过、已驳回）", notes = "获取入库单列表（全部、待审核、已通过、已驳回）")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "status", value = "出库单审核状态：00全部、01待审核、02已通过、03已驳回", allowableValues = "00,01,02,03", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "biz_type", value = "业务类型：00全部、01借用出库、02领料出库、03加工出库", allowableValues = "00,01,02,03", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码（从0开始）", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "每页显示条数", dataType = "int", paramType = "query") })
	public ResultEntity<Page<OutOrderEntity>> getStockOutOrders(@PathVariable("status") final String status,
			@PathVariable("biz_type") final String bizType, @PathVariable("page") int page,
			@PathVariable("size") int size) {
		// 校验传值
		if (status.equals("00") || status.equals("01") || status.equals("02") || status.equals("03")
				|| bizType.equals("00") || bizType.equals("01") || bizType.equals("02") || bizType.equals("03")) {
		} else {
			return new ResultEntity<Page<OutOrderEntity>>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}

		// 分页获取
		Page<OutOrderEntity> orders = outOrderReps.findAll(new Specification<OutOrderEntity>() {

			@Override
			public Predicate toPredicate(Root<OutOrderEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();

				// Where条件：状态
				In<String> inStatus = cb.in(root.<String>get("osp_status"));
				if (status.equals("00")) {// 查询 所有类型的全部数据
					inStatus.value("01");// 待审
					inStatus.value("02");// 主管通过
					inStatus.value("03");// 主管驳回
					inStatus.value("04");// 上级通过
					inStatus.value("05");// 上级驳回
					inStatus.value("06");// 库管通过
					inStatus.value("07");// 库管驳回
					inStatus.value("08");// 审核完成
					inStatus.value("10");// 审核完成
				} else {
					if ("01".equals(status)) {// 待审核
						inStatus.value("10");//
//						inStatus.value("04");// 上级审核通过后，才能轮到库管审核
//						inStatus.value("0");// 上级审核通过后，才能轮到库管审核
						
						/*Predicate p1=cb.equal(root.<String>get("mt_feature"), "03");//重要
						Predicate p11=cb.equal(root.<String>get("osp_status"), "02");//主管审过，重要物料，主管审过后，就轮到库管
						Predicate p101=cb.and(p1,p11);
						
						Predicate p2=cb.equal(root.<String>get("mt_feature"), "04");//关键
						Predicate p21=cb.equal(root.<String>get("osp_status"), "04");//关键物料，上级领导审过后，就轮到库管
						Predicate p201=cb.and(p2,p21);
						
						Predicate p301=cb.equal(root.<String>get("osp_status"), "10");//库管待审
						// 组合
						Predicate p000=cb.or(p101,p201,p301);
						list.add(p000);	*/
						
						
					} else if ("02".equals(status)) {// 已通过
						inStatus.value("02");// 主管通过
						inStatus.value("04");// 上级通过
						inStatus.value("06");// 库管通过
						inStatus.value("08");// 审核完成
					} else if ("03".equals(status)) {// 已驳回
						inStatus.value("03");// 主管驳回
						inStatus.value("05");// 上级驳回
						inStatus.value("07");// 库管驳回
					}
				}
				list.add(inStatus);

				if (bizType.equals("00")) {// 所有类型
					In<String> inType = cb.in(root.<String>get("osp_type"));
					inType.value("01");// 借用出库
					inType.value("02");// 领料出库
					inType.value("03");// 加工出库
					list.add(inType);
				} else {// 指定类型
					list.add(cb.equal(root.<String>get("osp_type"), bizType));
				}
				query.orderBy(cb.desc(root.get("osp_date"))); // 时间降序
				Predicate[] pres = new Predicate[list.size()];
				pres = list.toArray(pres);

				return cb.and(pres);

			}

		}, new PageRequest(page, size, null));

		return new ResultEntity<Page<OutOrderEntity>>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(), orders);
	}

	// 创建出库单
	@PostMapping("/create")
	@ApiOperation(value = "创建出库单", notes = "创建出库单（借用出库、领料出库、加工出库）")
	public ResultEntity<OutOrderEntity> createOutOrder(@ApiParam @RequestBody OutOrderDto object) {
		
		if (!checkOutDto(object)) {
			return new ResultEntity<OutOrderEntity>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}
		OutOrderEntity outE = null;
		try {
			outE = outOrderService.createOutOrder(object);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultEntity<OutOrderEntity>(ResultStatus.UNKNOWN_EXCEPTION.getCode(), 
					ResultStatus.UNKNOWN_EXCEPTION.getMessage(),
					null);
		}
		return new ResultEntity<OutOrderEntity>(ResultStatus.OK.getCode(), 
				ResultStatus.OK.getMessage(),
				outE);
	}

	// 创建出库单（扫码）
	@PostMapping("/scancreate")
	@ApiOperation(value = "创建扫码出库单", notes = "创建扫码出库单（借用出库、领料出库、加工出库）")
	public ResultEntity<OutOrderEntity> createOutOrderScan(@ApiParam @RequestBody ScanOutOrderDto object) {
		// 参数校验
		if (!checkScanDto(object)) {
			return new ResultEntity<OutOrderEntity>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}

		OutOrderEntity outE = null;
		try {
			outE = outOrderService.createOutOrderByScan(object);
		} catch (Exception e) {
			return new ResultEntity<OutOrderEntity>(ResultStatus.UNKNOWN_EXCEPTION.getCode(), 
					ResultStatus.UNKNOWN_EXCEPTION.getMessage(),
					null);
		}
		return new ResultEntity<OutOrderEntity>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				outE);
	}

	// 获取物料列表
	@GetMapping("/{order_id}/item/{page}/{size}")
	@ApiOperation(value = "获取物料列表", notes = "获取物料列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "入库单ID", required = true, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码（从0开始）", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "query") })
	public ResultEntity<Page<OutOrderItemEntity>> getStockOutItems(@PathVariable("order_id") long id,
			@PathVariable("page") int page, @PathVariable("size") int size) {
		final long orderId = id;
		Page<OutOrderItemEntity> orderItems = outOrderItemRepository.findAll(new Specification<OutOrderItemEntity>() {

			@Override
			public Predicate toPredicate(Root<OutOrderItemEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				// Join<OutOrderEntity, InOrderEntity> storageJoin =
				// root.join(root.getModel().getSingularAttribute("stockInOrder",InOrderEntity.class),JoinType.LEFT);
				list.add(cb.equal(root.<Long>get("ospid"), orderId));

				query.orderBy(cb.desc(root.get("ospitem_barcode")));
				Predicate[] pres = new Predicate[list.size()];
				pres = list.toArray(pres);

				return cb.and(pres);
			}

		}, new PageRequest(page, size, null));

		return new ResultEntity<Page<OutOrderItemEntity>>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				orderItems);
	}

	// 获取物料详情
	@GetMapping("/{order_id}/item/{item_id}")
	@ApiOperation(value = "获取物料详情", notes = "获取物料详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "出库单ID", required = true, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "item_id", value = "栏目ID", required = true, dataType = "long", paramType = "query") })
	public ResultEntity<OutOrderItemEntity> getItemByOutOrderId(@PathVariable("order_id") Long orderId,
			@PathVariable("item_id") Long itemId) {
		return new ResultEntity<OutOrderItemEntity>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				outOrderItemRepository.findByOspidAndItemid(orderId, itemId));
	}

	// 获取出库单物料信息（扫描或输入条形码）
	@GetMapping("/{order_id}/query/{bar_code}")
	@ApiOperation(value = "获取出库单物料信息（扫描或输入条形码）", notes = "获取出库单物料信息（扫描或输入条形码）")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "出库单ID", required = true, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "bar_code", value = "条形码", required = true, dataType = "string", paramType = "query") })
	public ResultEntity<OutOrderItemEntity> scanAuditOutOrder(@PathVariable("order_id") Long orderId,
			@PathVariable("bar_code") String barcode) {
		return new ResultEntity<OutOrderItemEntity>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				outOrderItemRepository.getByOspidAndBarcode(orderId,
						barcode.length() > 18 ? barcode.substring(0, 18) : barcode));
	}

	// 查看审核流程
	@GetMapping("/{order_id}/{biz_type}/flow") // 01借用出库、02领料出库、03加工出库"
	@ApiOperation(value = "获取审批流程列表", notes = "获取审批流程列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "order_id", value = "入库单ID", dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "biz_type", value = "业务类型:01借用出库、02领料出库、03加工出库", allowableValues = "01,02,03", dataType = "string", paramType = "query") })
	public ResultEntity<List<AuditRecordEntity>> getAuditOutFlow(@PathVariable("order_id") final Long orderId,
			@PathVariable("biz_type") final String bizType) {
		if (bizType.equals("01") || bizType.equals("02") || bizType.equals("03")) {
		} else {
			return new ResultEntity<List<AuditRecordEntity>>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}
		List<AuditRecordEntity> auditReds = auditRecordRepository.findAll(new Specification<AuditRecordEntity>() {

			@Override
			public Predicate toPredicate(Root<AuditRecordEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.<Long>get("relate_id"), orderId));// 关联ID（出库单ID）
				String tempType = "07";// 领料出库
				if (bizType.equals("01")) {// 借用
					tempType = "08";
				} else if (bizType.equals("03")) {// 加工
					tempType = "09";
				} else {// 领料
					tempType = "07";
				}
				list.add(cb.equal(root.<String>get("biz_type"), tempType));

				query.orderBy(cb.desc(root.get("audit_time")));// 审核时间降序

				return cb.and(list.toArray(new Predicate[list.size()]));
			}

		});

		return new ResultEntity<List<AuditRecordEntity>>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				auditReds);
	}

	private boolean checkScanDto(ScanOutOrderDto dto) {
		if (dto == null
				|| !AppUtil.isOK(dto.getBarcode(), 18, true) || !AppUtil.isOK(dto.getStockbin_code(), 5, true) 
				|| !AppUtil.isOK(dto.getMeasure_unit(), 16, true) || !AppUtil.isOK(dto.getOut_type(), 2, true)
				|| !AppUtil.isOK(dto.getProduct_code(), 6, true) 
				|| !AppUtil.isOK(dto.getFeature(), 2, true) || !AppUtil.isOK(dto.getSpec_model(), 128, true)
				|| !AppUtil.isOK(dto.getPick_address(), 64, false) || !AppUtil.isOK(dto.getPick_person(), 32, false)
				|| !AppUtil.isOK(dto.getAssist_attr(), 32, false)
				|| !AppUtil.isOK(dto.getBrand(), 64, false) || !AppUtil.isOK(dto.getDelivery_person(), 32, false)) {
			return false;
		}

		if (StringUtils.isNotBlank(dto.getOut_type()) && (dto.getOut_type().equals("01")
				|| dto.getOut_type().equals("02") || dto.getOut_type().equals("03"))) {
		} else {
			return false;
		}

		if (StringUtils.isNotBlank(dto.getFeature())
				&& (dto.getFeature().equals("01") || dto.getFeature().equals("02") || dto.getFeature().equals("03"))
				|| dto.getFeature().equals("04")) {
		} else {
			return false;
		}

		return true;
	}
	
	private boolean checkOutDto(OutOrderDto dto) {
		if (dto == null
				|| !AppUtil.isOK(dto.getOut_type(), 2, true) || !AppUtil.isOK(dto.getFeature(), 2, true) 
				|| !AppUtil.isOK(dto.getPick_person(), 32, true) || !AppUtil.isOK(dto.getDelivery_person(), 32, true)
				|| !AppUtil.isOK(dto.getPick_address(), 64, true) ) {
			return false;
		}
		
		if (StringUtils.isNotBlank(dto.getOut_type()) && (dto.getOut_type().equals("01")
				|| dto.getOut_type().equals("02") || dto.getOut_type().equals("03"))) {
		} else {
			return false;
		}
		
		if (StringUtils.isNotBlank(dto.getFeature())
				&& ((dto.getFeature().equals("01") || dto.getFeature().equals("02") || dto.getFeature().equals("03"))
				|| dto.getFeature().equals("04"))) {
		} else {
			return false;
		}
		
		return true;
	}

	// 审核入库单
	@PostMapping("/audit")
	@ApiOperation(value = "审核出库单", notes = "审核出库单（借用出库、领料出库、加工出库）")
	public ResultEntity<String> auditOutOrderPost(@ApiParam @RequestBody AuditOutOrderDto object) {
		if (!checkAuditDto(object)) {
			return new ResultEntity<String>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}
		try {
			// 审核出库单、栏目、更新物料库存、添加审核记录
			outOrderService.audit(object);
			
			//检测库存预警（历史）
			for(int i=0;i<object.getItem_list().size();i++) {
				AuditOutOrderItemDto it = object.getItem_list().get(i);
				mtService.addHistoryAlarm(it.getBarcode(), it.getAct_qty_out());
			}
		} catch (Exception e) {
			return new ResultEntity<String>(ResultStatus.AUDIT_FAILED.getCode(), 
					ResultStatus.AUDIT_FAILED.getMessage(), e.getMessage());
		}

		return new ResultEntity<String>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(), "OK");
	}

	private boolean checkAuditDto(AuditOutOrderDto dto) {
		if (dto == null || !AppUtil.isOK(dto.getAudit_person_name(), 32, true) || dto.getItem_list() == null) {
			return false;// 审核人、物料列表
		}
		// 出库类型
		if (StringUtils.isNotBlank(dto.getOut_type()) && (dto.getOut_type().equals("02")
				|| dto.getOut_type().equals("03") || dto.getOut_type().equals("01"))) {
		} else {
			return false;
		}
		// 状态
		if (StringUtils.isNotBlank(dto.getAudit_status())
				&& (dto.getAudit_status().equals("06") || dto.getAudit_status().equals("07"))) {
		} else {
			return false;
		}

		// 驳回时，意见必填
		if (dto.getAudit_status().equals("07") && !AppUtil.isOK(dto.getReject_reason(), 200, true)) {
			return false;
		}

		return true;
	}
	
	// 借用人列表
    @GetMapping("/pickrecord")
    @ApiOperation(value = "获取物料借用记录",notes="获取物料借用记录")
    @ApiImplicitParam(name = "bar_code", value = "条形码", required = true, dataType = "string", paramType = "query") 
    public ResultEntity<List<PickRecordDto>> getDepartments(String barCode){
    	List<Object> objList = outOrderReps.getPickInfo(barCode);
    	List<PickRecordDto> redList = new ArrayList<PickRecordDto>();
    	for(int i=0;i<objList.size();i++) {
    		Object[] obj = (Object[]) objList.get(i);
    		PickRecordDto pr = new PickRecordDto();
    		pr.setPick_addr((obj[0]==null?"":String.valueOf(obj[0])));
    		pr.setPick_person(obj[1]==null?"":String.valueOf(obj[1]));
    		pr.setPick_time(obj[2]==null?"":String.valueOf(obj[2]));
    		redList.add(pr);
    	}
    	
    	return new ResultEntity<List<PickRecordDto>>(ResultStatus.OK.getCode(),
    			ResultStatus.OK.getMessage(),
    			redList);
    }
}
