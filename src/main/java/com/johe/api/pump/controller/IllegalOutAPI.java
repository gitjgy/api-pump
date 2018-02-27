package com.johe.api.pump.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
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

import com.johe.api.pump.dto.GrantTimeDto;
import com.johe.api.pump.dto.IllegalOutDto;
import com.johe.api.pump.dto.ResponseIllegalOutDto;
import com.johe.api.pump.entity.IllegalAlarmEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.IllegalAlarmRepository;
import com.johe.api.pump.service.IllegalAlarmService;
import com.johe.api.pump.util.AppConstants;
import com.johe.api.pump.util.AppConstants.TimePattern;
import com.johe.api.pump.util.AppUtil;
import com.johe.api.pump.util.DateTimeUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="非法出库",tags="【非法出库】接口")
@RequestMapping("/api/v1/illegal")
@RestController
public class IllegalOutAPI {
	
	@Autowired
	IllegalAlarmRepository illgReps;
	
	@Autowired
	IllegalAlarmService ioService;
	

	Map<String,TimePattern> timeMap = new HashMap<String,TimePattern>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("01", TimePattern.ONE);
			put("02", TimePattern.TWO);
			put("03", TimePattern.THREE);
			put("04", TimePattern.FOUR);
			put("05", TimePattern.FIVE);
			put("06", TimePattern.SIX);
			put("07", TimePattern.SEVEN);
		}
	};
    @GetMapping("/{page}/{size}")
    @ApiOperation(value = "获取非法出库预警列表",notes="获取非法出库预警列表")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="page",value="页码（从0开始）",required=true, dataType="int",paramType="query"),
    	@ApiImplicitParam(name="size",value="每页显示条数",required=true, dataType="int",paramType="query")
    	})    
    public ResultEntity<Page<IllegalAlarmEntity>> getIllegalss( 
    		@PathVariable("page")int page, 
    		@PathVariable("size")int size){
    	Page<IllegalAlarmEntity>  illegals = illgReps.findAll(new Specification<IllegalAlarmEntity>() {

			@Override
			public Predicate toPredicate(Root<IllegalAlarmEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				
//				list.add(cb.equal(root.<String>get("is_alarm"), "Y"));
				
				query.orderBy(cb.desc(root.get("cre_time")));				
				Predicate[] pres = new Predicate[list.size()];
				pres = list.toArray(pres);
				
				return cb.and(pres);
				
			}
    		
    	}, new PageRequest(page, size, null));
    	
        return new ResultEntity<Page<IllegalAlarmEntity>>(ResultStatus.OK.getCode(),
        											ResultStatus.OK.getMessage(),
        											illegals);
    }
    
    
   /* @PostMapping("/create")
    @ApiOperation(value = "上传非法出库物料数据",notes="上传非法出库物料数据")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="src_id",value="来源标识（最大长度：4位字符）",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="storage_code",value="仓库代码（最大长度：6位字符）",example="例如：001、002...",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="category_big_code",value="大类代码（最大长度：3位字符）",example="例如：001、002、003...",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="category_small_code",value="小类代码（最大长度：3位字符）",example="例如：001、002、003...",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="sbin_code_area",value="仓位（区）代码（最大长度：1位字符）",example="例如：A、B、C...",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="sbin_code_rack",value="仓位（架）代码（最大长度：2位字符）",example="例如：01、02、03...",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="sbin_code_pos",value="仓位（位）代码（最大长度：2位字符）",example="例如：01、02、03...",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="bar_code",value="条形码（最大长度：20位字符）",example="例如：001002001A.01.010001",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="qty",value="数量（大于0）",example="例如：10、11",required=true, dataType="int",paramType="form"),
    	@ApiImplicitParam(name="mea_unit",value="计量单位（最大长度：6位字符）",example="例如：件、个、箱...",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="open_time",value="开门时间（最大长度：18位字符）",example="例如：2018-01-09 08:29:59",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="close_time",value="关门时间（最大长度：18位字符）",example="例如：2018-01-09 18:29:59",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="opt_time",value="操作时间（最大长度：18位字符）",example="例如：2018-01-09 19:29:59",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="opt_person",value="操作人（最大长度：16位字符）",example="例如：张三、李四...",required=true, dataType="string",paramType="form")
    	})    
    public ResultEntity<IllegalAlarmEntity> createIllegal(@RequestParam("src_id")String src_id,
    		@RequestParam("storage_code")String stg_code,
    		@RequestParam("category_big_code")String cg_big_code,
    		@RequestParam("category_small_code")String cg_small_code,
    		@RequestParam("sbin_code_area")String sbin_area_code,
    		@RequestParam("sbin_code_rack")String sbin_rack_code,
    		@RequestParam("sbin_code_pos")String sbin_pos_code,
    		@RequestParam("bar_code")String bar_code,
    		@RequestParam("qty")int out_qty,
    		@RequestParam("mea_unit")String mea_unit,
    		@RequestParam("open_time")String open_time,
    		@RequestParam("close_time")String close_time,
    		@PathVariable("opt_time")String opt_time,
    		@PathVariable("opt_person")String opt_person
    		){
    	
    	if(!isOK(src_id,4) || !isOK(stg_code,6) || !isOK(cg_big_code,3) || !isOK(cg_small_code,3) 
    			|| !isOK(sbin_area_code,1) || !isOK(sbin_rack_code,1) || !isOK(sbin_pos_code,1) || !isOK(bar_code,20)
    			||  out_qty <= 0 || !isOK(mea_unit,6) || !isOK(open_time,18) || !isOK(close_time,18) || !isOK(opt_time,18) 
    			|| !isOK(opt_person,16)) {
    		return new ResultEntity<IllegalAlarmEntity>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(),null);
    	}
    	
    	IllegalAlarmEntity ioe = ioService.create(src_id, stg_code, cg_big_code, cg_small_code, 
    			sbin_area_code+"."+sbin_rack_code+"."+sbin_pos_code, sbin_area_code, sbin_rack_code, 
    			sbin_pos_code, bar_code, out_qty, mea_unit, 
    			open_time, close_time, opt_time, opt_person, "");
    	if(ioe == null) {
    		return new ResultEntity<IllegalAlarmEntity>(ResultStatus.UN_SUCCESS_CREATE.getCode(),
					ResultStatus.UN_SUCCESS_CREATE.getMessage(),
					null);
    	}
    	
        return new ResultEntity<IllegalAlarmEntity>(ResultStatus.OK.getCode(),
        											ResultStatus.OK.getMessage(),
        											null);
    }*/
    
    @PostMapping("/upload")
    @ApiOperation(value = "上传非法出库物料数据",notes="上传非法出库物料数据")
    public ResultEntity<ResponseIllegalOutDto> uploadIllegalData(@RequestBody(required=true) IllegalOutDto jsonobject){
    	
    	if(!checkDto(jsonobject)) {
    		return new ResultEntity<ResponseIllegalOutDto>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
    				ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(),null);
    	}
    	
    	if(!AppConstants.ILLEGA_OUT_SECRET_KEY.equalsIgnoreCase(jsonobject.getRequestauth())) {// 密钥不正确
    		return new ResultEntity<ResponseIllegalOutDto>(ResultStatus.ILLEGAL_OUT_REQ_AUTH_ERR.getCode(),
					ResultStatus.ILLEGAL_OUT_REQ_AUTH_ERR.getMessage(),
					new ResponseIllegalOutDto(jsonobject.getRequestid(),
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
							String.valueOf(ResultStatus.ILLEGAL_OUT_REQ_AUTH_ERR.getCode()),null));
    	}
    	
    	ResponseIllegalOutDto ioe = null;
		try {
			ioe = ioService.checkIsIllegal(jsonobject);
			if(ioe == null) {
				return new ResultEntity<ResponseIllegalOutDto>(ResultStatus.ILLEGAL_OUT_REQ_SQL_EXP.getCode(),
						ResultStatus.ILLEGAL_OUT_REQ_SQL_EXP.getMessage(),
						new ResponseIllegalOutDto(jsonobject.getRequestid(),
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
								String.valueOf(ResultStatus.ILLEGAL_OUT_REQ_SQL_EXP.getCode()),null));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultEntity<ResponseIllegalOutDto>(ResultStatus.UNKNOWN_EXCEPTION.getCode(),
					ResultStatus.UNKNOWN_EXCEPTION.getMessage(),
					new ResponseIllegalOutDto(jsonobject.getRequestid(),
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
							String.valueOf(ResultStatus.ILLEGAL_OUT_REQ_SQL_EXP.getCode()),null));
		}
    	
    	return new ResultEntity<ResponseIllegalOutDto>(ResultStatus.ILLEGAL_OUT_SUCCESS.getCode(),
    			ResultStatus.ILLEGAL_OUT_SUCCESS.getMessage(),
    			ioe);
    }
    
    private boolean checkDto(IllegalOutDto object) {
		if(object == null || object.getMaterial() == null 
				|| object.getMaterial().size() == 0
				|| !AppUtil.isOK(object.getRequestid(),16,true) 
				|| !AppUtil.isValidDate(object.getOperatetime(), "yyyy-MM-dd HH:mm:ss") 
				|| !AppUtil.isOK(object.getRequestauth(),32,true)) {
			return false;
    	}
		return true;
    }
    
    @PostMapping("/pnt")
    @ApiOperation(value = "授时（获取服务器时间）",notes="授时（获取服务器时间）")
    public ResultEntity<ResponseIllegalOutDto> getServerTime(@RequestBody(required=true) GrantTimeDto object){
    	
    	if(!checkGrantTime(object)) {
    		return new ResultEntity<ResponseIllegalOutDto>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
    				ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(),null);
    	}
    	
    	if(!AppConstants.ILLEGA_OUT_SECRET_KEY.equalsIgnoreCase(object.getRequestauth())) {// 密钥不正确
    		return new ResultEntity<ResponseIllegalOutDto>(ResultStatus.ILLEGAL_OUT_REQ_AUTH_ERR.getCode(),
					ResultStatus.ILLEGAL_OUT_REQ_AUTH_ERR.getMessage(),
					new ResponseIllegalOutDto(object.getRequestid(),
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
							String.valueOf(ResultStatus.ILLEGAL_OUT_REQ_AUTH_ERR.getCode()),null));
    	}
    	
    	String strTime = null;
		try {
			strTime = DateTimeUtil.getServerTime(timeMap.get(object.getPattern()));
			if(strTime == null) {
				return new ResultEntity<ResponseIllegalOutDto>(ResultStatus.ILLEGAL_OUT_REQ_SQL_EXP.getCode(),
						ResultStatus.ILLEGAL_OUT_REQ_SQL_EXP.getMessage(),
						new ResponseIllegalOutDto(object.getRequestid(),
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
								String.valueOf(ResultStatus.ILLEGAL_OUT_REQ_SQL_EXP.getCode()),null));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultEntity<ResponseIllegalOutDto>(ResultStatus.UNKNOWN_EXCEPTION.getCode(),
					ResultStatus.UNKNOWN_EXCEPTION.getMessage(),
					new ResponseIllegalOutDto(object.getRequestid(),
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
							String.valueOf(ResultStatus.ILLEGAL_OUT_REQ_SQL_EXP.getCode()),null));
		}
    	
    	return new ResultEntity<ResponseIllegalOutDto>(ResultStatus.OK.getCode(),
    			ResultStatus.OK.getMessage(),
    			new ResponseIllegalOutDto(object.getRequestid(),
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
						String.valueOf(ResultStatus.OK.getCode()),strTime));
    }
    
    
    private boolean checkGrantTime(GrantTimeDto object) {
    	boolean isOk = true;
		if(object == null || StringUtils.isBlank(object.getEquipment())
				|| !AppUtil.isOK(object.getRequestid(),16,true) 
				|| !AppUtil.isOK(object.getRequestid(),16,true) 
				|| !AppUtil.isValidDate(object.getRequesttime(), "yyyy-MM-dd HH:mm:ss") 
				|| !AppUtil.isOK(object.getRequestauth(),32,true)) {
			isOk = false;
    	}
		if (StringUtils.isNotBlank(object.getPattern()) 
				&& (object.getPattern().equals("01") 
				|| object.getPattern().equals("02")
				|| object.getPattern().equals("03")
				|| object.getPattern().equals("04")
				|| object.getPattern().equals("05")
				|| object.getPattern().equals("06")
				|| object.getPattern().equals("07"))) {
		} else {
			isOk = false;
		}
		return isOk;
    }
}
