package com.johe.api.pump.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johe.api.pump.entity.HistoryAlarmEntity;
import com.johe.api.pump.entity.InOrderEntity;
import com.johe.api.pump.entity.MaterialEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.HistoryAlarmRepository;
import com.johe.api.pump.repository.InOrderItemRepository;
import com.johe.api.pump.repository.MaterialRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="库存预警",tags="【库存预警】接口")
@RequestMapping("/api/v1/inventory")
@RestController
public class InventoryAlarmAPI {
	
	@Autowired
	MaterialRepository materialReps;
	
	@Autowired
	InOrderItemRepository itemReps;
	
	@Autowired
	HistoryAlarmRepository historyReps;
    
    @GetMapping("/alarm/{page}/{size}")
    @ApiOperation(value = "获取库存预警列表",notes="获取库存预警列表")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="page",value="页码（从0开始）",required=true, dataType="int",paramType="query"),
    	@ApiImplicitParam(name="size",value="每页显示条数",required=true, dataType="int",paramType="query")
    	})    
    public ResultEntity<Page<MaterialEntity>> getInventoryList( 
    		@PathVariable("page")int page, 
    		@PathVariable("size")int size){
    	Page<MaterialEntity>  materials = materialReps.findAll(new Specification<MaterialEntity>() {

			@Override
			public Predicate toPredicate(Root<MaterialEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				In<String> inStockStatus = cb.in(root.<String>get("mt_stock_status"));//库存状态
				inStockStatus.value("02");//短缺
				inStockStatus.value("03");//超储
				list.add(inStockStatus);
				In<String> inStatus = cb.in(root.<String>get("mt_status"));//物料状态
				inStatus.value("01");//在库
				inStatus.value("02");//借出
				inStatus.value("03");//使用
				inStatus.value("04");//报废
				list.add(inStatus);
				
				query.orderBy(cb.desc(root.get("mt_code")));				
				Predicate[] pres = new Predicate[list.size()];
				pres = list.toArray(pres);
				
				return cb.and(pres);
				
			}
    		
    	}, new PageRequest(page, size, null));
    	
        return new ResultEntity<Page<MaterialEntity>>(ResultStatus.OK.getCode(),
        											ResultStatus.OK.getMessage(),
        											materials);
    }
    
    @GetMapping("/history/{page}/{size}/{start_time}/{end_time}")
    @ApiOperation(value = "获取历史库存预警列表",notes="获取历史库存预警列表")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="page",value="页码（从0开始）",required=true, dataType="int",paramType="query"),
    	@ApiImplicitParam(name="size",value="每页显示条数",required=true, dataType="int",paramType="query"),
    	@ApiImplicitParam(name="start_time",value="年月日（0全部）",example="例：2018-01-16",required=true, dataType="string",paramType="query"),
    	@ApiImplicitParam(name="end_time",value="年月日（0全部）",example="例：2018-01-17",required=true, dataType="string",paramType="query")
    })
    public ResultEntity<Page<HistoryAlarmEntity>> getInventoryHistory( 
    		@PathVariable("page")int page,
    		@PathVariable("size")int size,
    		@PathVariable("start_time") final String startTime,
    		@PathVariable("end_time") final String endTime){
    	if(isValidDate(startTime) && isValidDate(endTime)) {
    	}else {
    		return new ResultEntity<Page<HistoryAlarmEntity>>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(),null);
    	}
    	Page<HistoryAlarmEntity>  history = historyReps.findAll(new Specification<HistoryAlarmEntity>() {
    		
    		@Override
    		public Predicate toPredicate(Root<HistoryAlarmEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    			List<Predicate> list = new ArrayList<Predicate>();
    			    	
    			if(!startTime.equals("0")) {
    				list.add(cb.greaterThanOrEqualTo(root.<String>get("alarm_time"), startTime+" 00:00:01"));
    			}
    			
    			if(!endTime.equals("0")) {
    				list.add(cb.lessThanOrEqualTo(root.<String>get("alarm_time"), endTime+" 23:59:59"));
    			}
    			query.orderBy(cb.desc(root.get("alarm_time")));
    			Predicate[] pres = new Predicate[list.size()];
    			pres = list.toArray(pres);
    			
    			return cb.and(pres);
    			
    		}
    		
    	}, new PageRequest(page, size, null));
    	
    	return new ResultEntity<Page<HistoryAlarmEntity>>(ResultStatus.OK.getCode(),
    			ResultStatus.OK.getMessage(),
    			history);
    }
     
    
    private boolean isValidDate(String strDate) {
    	if(StringUtils.isBlank(strDate)) {
    		return false;
    	}else if(strDate.equals("0")){//查全部时间范围内
    		return true;
    	}
    	boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
           format.setLenient(false);
           format.parse(strDate);
        } catch (ParseException e) {
          // e.printStackTrace();
          // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess=false;
        } 
        return convertSuccess;
    }
    
}
