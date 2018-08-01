package com.johe.api.pump.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

import com.johe.api.pump.dto.CheckRecordDto;
import com.johe.api.pump.dto.MaterialCheckDto;
import com.johe.api.pump.entity.CheckRecordEntity;
import com.johe.api.pump.entity.MaterialListEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.CheckRecordRepository;
import com.johe.api.pump.repository.MaterialRepository;
import com.johe.api.pump.service.CheckRecordService;
import com.johe.api.pump.util.AppUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "盘点管理", tags = "【盘点管理】接口")
@RequestMapping("/api/v1/checkrecord")
@RestController
public class CheckRecordAPI {

	@Autowired
	CheckRecordRepository crReps;
	
	@Autowired
	MaterialRepository mtReps;

	@Autowired
	CheckRecordService crService;

	@GetMapping("/{user_id}/{page}/{size}")
	@ApiOperation(value = "获取盘点记录列表", notes = "获取盘点记录列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "user_id", value = "盘点人ID（当前登录人ID）", required = true, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码（从0开始）", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "query") })
	public ResultEntity<Page<CheckRecordEntity>> getCheckRecords(@PathVariable("user_id") final long userId,
			@PathVariable("page") int page, @PathVariable("size") int size) {
		Page<CheckRecordEntity> checkRec = crReps.findAll(new Specification<CheckRecordEntity>() {

			@Override
			public Predicate toPredicate(Root<CheckRecordEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.<Long>get("person_id"), userId));// 盘点人ID
				In<String> checkType = cb.in(root.<String>get("check_type"));
				checkType.value("01");// 终端手动盘点
				checkType.value("02");// 终端扫描盘点
				list.add(checkType);

				query.orderBy(cb.desc(root.get("check_time")));
				Predicate[] pres = new Predicate[list.size()];
				pres = list.toArray(pres);

				return cb.and(pres);

			}

		}, new PageRequest(page, size, null));

		return new ResultEntity<Page<CheckRecordEntity>>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				checkRec);
	}

	// 获取盘点详情
	@GetMapping("/detail/{check_id}")
	@ApiOperation(value = "获取盘点记录详情", notes = "获取盘点记录详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "check_id", value = "盘点记录ID", required = true, dataType = "long", paramType = "query") })
	public ResultEntity<CheckRecordEntity> getCheckDetailById(@PathVariable("check_id") Long checkId) {
		return new ResultEntity<CheckRecordEntity>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
				crReps.findOne(checkId));
	}

	/*// 获取物料编码对应的库存、入库、出库数据
	@GetMapping("/search/{small_code}/{stg_code}/{area_code}/{rack_code}/{pos_code}")
	@ApiOperation(value = "获取物料编码对应的库存、入库、出库数据", notes = "获取物料编码对应的库存、入库、出库、数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "small_code", value = "物料编码", example = "例：AJ0101", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "stg_code", value = "仓库代码", example = "例：001", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "area_code", value = "区代码", example = "例：A", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "rack_code", value = "架代码", example = "例：01", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "pos_code", value = "位代码", example = "例：01", required = true, dataType = "string", paramType = "query") })
	public ResultEntity<MaterialCheckDto> searchByCode(@PathVariable("small_code") String smallCode,
			@PathVariable("stg_code") String stgCode, @PathVariable("area_code") String areaCode,
			@PathVariable("rack_code") String rackCode, @PathVariable("pos_code") String posCode) {
		if (!AppUtil.isOK(smallCode, 6, true) || !AppUtil.isOK(stgCode, 3, true) || !AppUtil.isOK(areaCode, 2, true)
				|| !AppUtil.isOK(rackCode, 2, true) || !AppUtil.isOK(posCode, 2, true)) {
			return new ResultEntity<MaterialCheckDto>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}
		MaterialCheckDto mc = null;
		try {
			mc = crService.search(smallCode, stgCode, areaCode, rackCode, posCode);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultEntity<MaterialCheckDto>(ResultStatus.UNKNOWN_EXCEPTION.getCode(),
					ResultStatus.UNKNOWN_EXCEPTION.getMessage(), null);
		}

		return new ResultEntity<MaterialCheckDto>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(), mc);
	}*/
	
	// 手动盘点时，根据仓库、仓位，获取物料列表
	@GetMapping("/search/material/{stg_id}/{area_id}/{rack_id}/{pos_id}")
	@ApiOperation(value = "手动盘点时，根据仓库、仓位，获取物料列表", notes = "手动盘点时，根据仓库、仓位，获取物料列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "stg_id", value = "仓库ID", required = true, dataType = "long", paramType = "query"),
		@ApiImplicitParam(name = "area_id", value = "区ID", required = true, dataType = "long", paramType = "query"),
		@ApiImplicitParam(name = "rack_id", value = "架ID", required = true, dataType = "long", paramType = "query"),
		@ApiImplicitParam(name = "pos_id", value = "位ID", required = true, dataType = "long", paramType = "query")
		})
	public ResultEntity<List<MaterialListEntity>> searchByStgIdAndBinId(@PathVariable("stg_id") long stg_id,
			@PathVariable("area_id") long area_id, @PathVariable("rack_id") long rack_id,
			@PathVariable("pos_id") long pos_id) {
		List<MaterialListEntity> mtList = new ArrayList<MaterialListEntity>();
		try {
			List<Object> objList = mtReps.searchByStgIdAndBinId(stg_id, area_id, rack_id, pos_id);
			if(objList != null) {
				for(int i=0;i<objList.size();i++) {
					Object[] obj = (Object[]) objList.get(i);
					MaterialListEntity mt = new MaterialListEntity();
					mt.setMt_id(Long.parseLong(obj[0].toString()));
					mt.setMt_barcode(String.valueOf(obj[1]));
					mt.setMt_fullname(String.valueOf(obj[2]));
					mt.setMt_measure_unit(String.valueOf(obj[3]));
					mt.setMt_category_big(Long.parseLong(String.valueOf(obj[4])));
					mt.setMt_category_small(Long.parseLong(String.valueOf(obj[5])));
					mt.setMt_feature(String.valueOf(obj[6]));
					mt.setMt_code(String.valueOf(obj[7]));
					mt.setPrice(String.valueOf(obj[8]));
					mtList.add(mt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultEntity<List<MaterialListEntity>>(ResultStatus.UNKNOWN_EXCEPTION.getCode(),
					ResultStatus.UNKNOWN_EXCEPTION.getMessage(), null);
		}
		
		return new ResultEntity<List<MaterialListEntity>>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(), mtList);
	}
	
	
	// 根据物料ID，获取物料的期初、库存、入库、出库数据
/*	@GetMapping("/search/mtid/{mt_id}")
	@ApiOperation(value = "获取物料的期初、库存、入库、出库数据", notes = "获取物料的期初库存、入库、出库、数据")
	public ResultEntity<MaterialCheckDto> searchQtyByMtId(@PathVariable("mt_id") long mt_id){
		MaterialCheckDto mc = null;
		try {
			mc = crService.searchByMtId(mt_id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultEntity<MaterialCheckDto>(ResultStatus.UNKNOWN_EXCEPTION.getCode(),
					ResultStatus.UNKNOWN_EXCEPTION.getMessage(), null);
		}

		return new ResultEntity<MaterialCheckDto>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(), mc);
	}*/
	
	// 扫描盘点
	// 根据物料ID，获取物料的库存
	@GetMapping("/search/mtbarcode/{mt_barcode}")
	@ApiOperation(value = "根据扫描到的条形码，获取物料的库存数据", notes = "根据扫描到的条形码，获取物料的库存数据")
	public ResultEntity<MaterialCheckDto> searchQtyByMtId(@PathVariable("mt_barcode") String mt_barcode){
		MaterialCheckDto mc = null;
		try {
			mc = crService.searchByMtBarcode(mt_barcode);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultEntity<MaterialCheckDto>(ResultStatus.UNKNOWN_EXCEPTION.getCode(),
					ResultStatus.UNKNOWN_EXCEPTION.getMessage(), null);
		}

		return new ResultEntity<MaterialCheckDto>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(), mc);
	}
	
	//扫描盘点
	// 根据物料条形码，获取物料的期初、库存、入库、出库数据
	@GetMapping("/search/barcode/{bar_code}")
	@ApiOperation(value = "获取物料的期初、库存、入库、出库数据", notes = "获取物料的期初库存、入库、出库、数据")
	public ResultEntity<MaterialCheckDto> scanCheck(@PathVariable("bar_code") String bar_code){
		if (!AppUtil.isOK(bar_code, 18, true)) {
			return new ResultEntity<MaterialCheckDto>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}
		
		MaterialCheckDto mc = null;
		try {
			mc = crService.searchByBarcode(bar_code);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultEntity<MaterialCheckDto>(ResultStatus.UNKNOWN_EXCEPTION.getCode(),
					ResultStatus.UNKNOWN_EXCEPTION.getMessage(), null);
		}
		
		return new ResultEntity<MaterialCheckDto>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(), mc);
	}
	
	// 提交盘点数据
	@PostMapping("/submit")
	@ApiOperation(value = "提交盘点数据", notes = "提交盘点数据")
	public ResultEntity<CheckRecordEntity> submit(@RequestBody(required = true) CheckRecordDto object) {
		if (object != null || !AppUtil.isOK(object.getMea_unit(), 32, true)
				|| !AppUtil.isOK(object.getBar_code(), 18, true) /*|| !AppUtil.isOK(object.getBig_code(), 3, false)*/
				|| /*!AppUtil.isOK(object.getBig_name(), 32, false) ||*/ !AppUtil.isOK(object.getCheck_type(), 2, true)
				|| !AppUtil.isOK(object.getMea_unit(), 32, true) /*|| !AppUtil.isOK(object.getMt_code(), 6, true)*/
				|| /*!AppUtil.isOK(object.getMt_encode(), 14, false) ||*/ !AppUtil.isOK(object.getMt_fullname(), 32, true)
				|| !AppUtil.isOK(object.getPerson_name(), 32, true) || !AppUtil.isOK(object.getPl_qty(), 10, false)
				|| !AppUtil.isOK(object.getSbin_code(), 5, true) /*|| !AppUtil.isOK(object.getSmall_name(), 32, false)*/
				|| !AppUtil.isOK(object.getStorage_code(), 3, true) || !AppUtil.isOK(object.getStorage_name(), 32, true)
				|| !AppUtil.isOK(object.getRemark(), 128, false) /*|| !AppUtil.isOK(object.getSmall_code(), 6, false)*/) {
			// 参数合法
		} else {
			return new ResultEntity<CheckRecordEntity>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(), null);
		}

		CheckRecordEntity cre = null;
		try {// 创建盘点任务
			cre = crService.create(object);
			if (cre == null) {
				return new ResultEntity<CheckRecordEntity>(ResultStatus.UN_SUCCESS_CREATE.getCode(),
						ResultStatus.UN_SUCCESS_CREATE.getMessage(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultEntity<CheckRecordEntity>(ResultStatus.UNKNOWN_EXCEPTION.getCode(),
					ResultStatus.UNKNOWN_EXCEPTION.getMessage(), null);
		}

		return new ResultEntity<CheckRecordEntity>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(), cre);
	}

	
	@GetMapping("/manual/material/{stg_id}/{area_id}/{rack_id}/{pos_id}/{big_id}/{small_id}")
	@ApiOperation(value = "手动盘点时，根据仓库、仓位（区、架、位）、大类、小类，获取库存", notes = "手动盘点时，根据仓库、仓位（区、架、位）、大类、小类，获取库存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "stg_id", value = "仓库ID", required = true, dataType = "long", paramType = "query"),
		@ApiImplicitParam(name = "area_id", value = "区ID", required = true, dataType = "long", paramType = "query"),
		@ApiImplicitParam(name = "rack_id", value = "架ID", required = true, dataType = "long", paramType = "query"),
		@ApiImplicitParam(name = "pos_id", value = "位ID", required = true, dataType = "long", paramType = "query"),
		@ApiImplicitParam(name = "big_id", value = "大类ID", required = true, dataType = "long", paramType = "query"),
		@ApiImplicitParam(name = "small_id", value = "小类ID", required = true, dataType = "long", paramType = "query")
		})
	public ResultEntity<MaterialCheckDto> getRemailQty(@PathVariable("stg_id") long stg_id,
			@PathVariable("area_id") long area_id, @PathVariable("rack_id") long rack_id,
			@PathVariable("pos_id") long pos_id,@PathVariable("big_id") long big_id,
			@PathVariable("small_id") long small_id) {
		MaterialCheckDto mc = new MaterialCheckDto();
		try {
			mc = crService.manualCheck(stg_id, area_id, rack_id, pos_id, big_id, small_id);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultEntity<MaterialCheckDto>(ResultStatus.UNKNOWN_EXCEPTION.getCode(),
					ResultStatus.UNKNOWN_EXCEPTION.getMessage(), null);
		}
		
		return new ResultEntity<MaterialCheckDto>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(), mc);
	}
	
	
}
