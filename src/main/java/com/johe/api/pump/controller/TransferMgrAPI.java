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

import com.johe.api.pump.dto.AuditTransferItemDto;
import com.johe.api.pump.dto.AuditTransferOrderDto;
import com.johe.api.pump.dto.ScanTransferOrderDto;
import com.johe.api.pump.dto.TransferOrderDto;
import com.johe.api.pump.entity.AuditRecordEntity;
import com.johe.api.pump.entity.TransferItemEntity;
import com.johe.api.pump.entity.TransferOrderEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.AuditRecordRepository;
import com.johe.api.pump.repository.TransferOrderItemRepository;
import com.johe.api.pump.repository.TransferOrderRepository;
import com.johe.api.pump.service.MaterialService;
import com.johe.api.pump.service.TransferService;
import com.johe.api.pump.util.AppUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "调拨管理", tags = "【调拨管理】接口")
@RequestMapping("/api/v1/transfer")
@RestController
public class TransferMgrAPI {

	@Autowired
	TransferOrderRepository transferOrderRepository;

	@Autowired
	TransferService tranService;
	
	@Autowired
	MaterialService mtService;

	@Autowired
	TransferOrderItemRepository transferItemRepository;

	@Autowired
	AuditRecordRepository auditRecordRepository;

	// 获取调拨单列表（全部、待审核、已通过、已驳回）
	@GetMapping("/{status}/{page}/{size}")
	@ApiOperation(value = "获取调拨单列表（全部、待审核、已通过、已驳回）", notes = "获取调拨单列表（全部、待审核、已通过、已驳回）")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "status", value = "调拨单审核状态：00全部、01待审核、02已通过、03已驳回", allowableValues = "00,01,02,03", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码（从0开始）", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "每页显示条数", dataType = "int", paramType = "query") })
	public ResultEntity<Page<TransferOrderEntity>> getTranOrders(@PathVariable("status") final String status,
			@PathVariable("page") int page, @PathVariable("size") int size) {
		// 校验传值
		if (status.equals("00") || status.equals("01") || status.equals("02") || status.equals("03")) {
		} else {
			return new ResultEntity<Page<TransferOrderEntity>>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}

		// 分页获取
		Page<TransferOrderEntity> orders = transferOrderRepository.findAll(new Specification<TransferOrderEntity>() {

			@Override
			public Predicate toPredicate(Root<TransferOrderEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();

				// Where条件：状态
				In<String> inStatus = cb.in(root.<String>get("tran_status"));
				if (status.equals("00")) {// 查询 所有类型的全部数据
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
					if ("01".equals(status)) {// 待审核
//						inStatus.value("04");// 待审
						inStatus.value("10");// 待审
						
						/*Predicate p1=cb.equal(root.<String>get("mt_feature"), "03");//重要
						Predicate p11=cb.equal(root.<String>get("tran_status"), "02");//主管审过，重要物料，主管审过后，就轮到库管
						Predicate p101=cb.and(p1,p11);
						
						Predicate p2=cb.equal(root.<String>get("mt_feature"), "04");//关键
						Predicate p21=cb.equal(root.<String>get("tran_status"), "04");//关键物料，上级领导审过后，就轮到库管
						Predicate p201=cb.and(p2,p21);
						
						Predicate p301=cb.equal(root.<String>get("tran_status"), "10");//库管待审
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

				query.orderBy(cb.desc(root.get("tran_date"))); // 时间降序
				Predicate[] pres = new Predicate[list.size()];
				pres = list.toArray(pres);

				return cb.and(pres);

			}

		}, new PageRequest(page, size, null));

		return new ResultEntity<Page<TransferOrderEntity>>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				orders);
	}

	// 创建调拨单
	@PostMapping("/create")
	@ApiOperation(value = "创建调拨单", notes = "创建调拨单")
	public ResultEntity<TransferOrderEntity> createTranOrder(@ApiParam @RequestBody TransferOrderDto object) {

		return new ResultEntity<TransferOrderEntity>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				tranService.save(object));
	}

	// 创建调拨单（扫码）
	@PostMapping("/scancreate")
	@ApiOperation(value = "创建扫码调拨单", notes = "创建扫码调拨单")
	public ResultEntity<TransferOrderEntity> createTranOrderScan(@ApiParam @RequestBody ScanTransferOrderDto object) {
		// 参数校验
		if (!checkScanDto(object)) {
			return new ResultEntity<TransferOrderEntity>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}
		
		TransferOrderEntity toE = null;
		try {
			toE = tranService.createTranOrderByScan(object);
		} catch (Exception e) {			
			e.printStackTrace();
			return new ResultEntity<TransferOrderEntity>(ResultStatus.UN_SUCCESS_CREATE.getCode(), 
					ResultStatus.UN_SUCCESS_CREATE.getMessage(), null);
		}
		
		return new ResultEntity<TransferOrderEntity>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				toE);
	}

	// 获取物料列表
	@GetMapping("/{order_id}/item/{page}/{size}")
	@ApiOperation(value = "获取物料列表", notes = "获取物料列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "调拨单ID", required = true, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码（从0开始）", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "query") })
	public ResultEntity<Page<TransferItemEntity>> getStockInOrders(@PathVariable("order_id") long id,
			@PathVariable("page") int page, @PathVariable("size") int size) {
		final long orderId = id;
		Page<TransferItemEntity> orderItems = transferItemRepository.findAll(new Specification<TransferItemEntity>() {

			@Override
			public Predicate toPredicate(Root<TransferItemEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				// Join<TransferOrderEntity, InOrderEntity> storageJoin =
				// root.join(root.getModel().getSingularAttribute("stockInOrder",InOrderEntity.class),JoinType.LEFT);
				list.add(cb.equal(root.<Long>get("tranid"), orderId));

				query.orderBy(cb.desc(root.get("tranitem_barcode")));
				Predicate[] pres = new Predicate[list.size()];
				pres = list.toArray(pres);

				return cb.and(pres);
			}

		}, new PageRequest(page, size, null));

		return new ResultEntity<Page<TransferItemEntity>>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				orderItems);
	}

	// 获取物料详情
	@GetMapping("/{order_id}/item/{item_id}")
	@ApiOperation(value = "获取物料详情", notes = "获取物料详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "调拨单ID", required = true, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "item_id", value = "栏目ID", required = true, dataType = "long", paramType = "query") })
	public ResultEntity<TransferItemEntity> getItemById(@PathVariable("order_id") Long orderId,
			@PathVariable("item_id") Long itemId) {
		return new ResultEntity<TransferItemEntity>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				transferItemRepository.findByTranidAndItemid(orderId, itemId));
	}

	// 获取调拨单物料信息（扫描或输入条形码）
	@GetMapping("/{order_id}/query/{bar_code}")
	@ApiOperation(value = "获取调拨单物料信息（扫描或输入条形码）", notes = "获取调拨单物料信息（扫描或输入条形码）")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "调拨单ID", required = true, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "bar_code", value = "条形码", required = true, dataType = "long", paramType = "query") })
	public ResultEntity<TransferItemEntity> scanAuditTranOrder(@PathVariable("order_id") Long orderId,
			@PathVariable("bar_code") String barcode) {
		return new ResultEntity<TransferItemEntity>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				transferItemRepository.getByTranidAndBarcode(orderId,
						barcode.length() > 18 ? barcode.substring(0, 18) : barcode));
	}

	// 查看审核流程
	@GetMapping("/{order_id}/flow")
	@ApiOperation(value = "获取调拨单审批流程列表", notes = "获取调拨单审批流程列表")
	@ApiImplicitParam(name = "order_id", value = "调拨单ID", dataType = "long", paramType = "query")
	public ResultEntity<List<AuditRecordEntity>> getAuditFlow(@PathVariable("order_id") final Long orderId) {
		List<AuditRecordEntity> auditReds = auditRecordRepository.findAll(new Specification<AuditRecordEntity>() {

			@Override
			public Predicate toPredicate(Root<AuditRecordEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.<Long>get("relate_id"), orderId));// 关联ID（调拨单ID）
				list.add(cb.equal(root.<String>get("biz_type"), "06"));// 调拨单

				query.orderBy(cb.desc(root.get("audit_time")));// 降序

				return cb.and(list.toArray(new Predicate[list.size()]));
			}

		});

		return new ResultEntity<List<AuditRecordEntity>>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				auditReds);
	}

	private boolean checkScanDto(ScanTransferOrderDto dto) {
		if (dto == null || !AppUtil.isOK(dto.getAcceptor(), 32, false) || !AppUtil.isOK(dto.getBarcode(), 18, true)
				|| !AppUtil.isOK(dto.getBrand(), 64, false) || !AppUtil.isOK(dto.getDept(), 64, true)
				|| !AppUtil.isOK(dto.getMeasure_unit(), 16, true) || !AppUtil.isOK(dto.getLeader(), 32, true)
				|| !AppUtil.isOK(dto.getIn_bin_code(), 5, true) || !AppUtil.isOK(dto.getLogistics_co(), 64, false)
				|| !AppUtil.isOK(dto.getProduct_code(), 6, true) || !AppUtil.isOK(dto.getSpec_model(), 32, false)
				|| !AppUtil.isOK(dto.getBarcode(), 18, true) || !AppUtil.isOK(dto.getBrand(), 64, false)
				|| !AppUtil.isOK(dto.getOut_bin_code(), 5, true)
				|| !AppUtil.isOK(dto.getLogistics_order_sn(), 64, false)
				|| !AppUtil.isOK(dto.getOther_order_sn(), 64, false) || !AppUtil.isOK(dto.getOut_address(), 64, false)
				|| !AppUtil.isOK(dto.getProduct_code(), 6, false) || !AppUtil.isOK(dto.getPump_name(), 64, false)
				|| !AppUtil.isOK(dto.getReceipt_date(), 10, false) || !AppUtil.isOK(dto.getTransfer_person(), 32, false)
				|| !AppUtil.isOK(dto.getUse_person(), 32, false) || !AppUtil.isOK(dto.getMt_feature(), 2, true)) {
			return false;
		}

		if (StringUtils.isNotBlank(dto.getMt_feature()) && (dto.getMt_feature().equals("01")
				|| dto.getMt_feature().equals("02") || dto.getMt_feature().equals("03"))
				|| dto.getMt_feature().equals("04")) {
		} else {
			return false;
		}

		return true;
	}

	private boolean checkAuditDto(AuditTransferOrderDto dto) {
		if (dto == null || !AppUtil.isOK(dto.getAudit_person_name(), 32, true) || dto.getItem_list() == null) {
			return false;// 审核人、物料列表
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

	// 审核调拨单
	@PostMapping("/audit")
	@ApiOperation(value = "审核调拨单", notes = "审核调拨单")
	public ResultEntity<String> auditTranOrder(@ApiParam @RequestBody AuditTransferOrderDto object) {
		if (!checkAuditDto(object)) {
			return new ResultEntity<String>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}
		try {
			// 审核（更新调拨单、栏目、更新库存、添加审核记录、检测库存预警）
			tranService.audit(object);
			
			//检测库存预警（历史）
			for(int i=0;i<object.getItem_list().size();i++) {
				AuditTransferItemDto it = object.getItem_list().get(i);
				mtService.addHistoryAlarm(it.getBarcode(), it.getAct_qty_tran());
			}
		} catch (Exception e) {
			return new ResultEntity<String>(ResultStatus.AUDIT_FAILED.getCode(), 
					ResultStatus.AUDIT_FAILED.getMessage(), e.getMessage());
		}

		return new ResultEntity<String>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(), "OK");
	}

}
