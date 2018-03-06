package com.johe.api.pump.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johe.api.pump.dto.DepartmentDto;
import com.johe.api.pump.dto.UserDto;
import com.johe.api.pump.entity.CategoryEntity;
import com.johe.api.pump.entity.CategoryEntity3;
import com.johe.api.pump.entity.StationEntity;
import com.johe.api.pump.entity.StockBinEntity;
import com.johe.api.pump.entity.StorageEntity;
import com.johe.api.pump.entity.SupplierEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.CategoryRepository;
import com.johe.api.pump.repository.DepartmentRepository;
import com.johe.api.pump.repository.StationRepository;
import com.johe.api.pump.repository.StockBinRepository;
import com.johe.api.pump.repository.StorageRepository;
import com.johe.api.pump.repository.SupplierRepository;
import com.johe.api.pump.repository.SysCodeRepository;
import com.johe.api.pump.repository.SysUserRepository;
import com.johe.api.pump.util.AppConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="包含大类、小类、仓库、仓位区、架、位、计量单位",tags="【COMMON】接口")
@RequestMapping("/api/v1/common")
@RestController
public class CommonAPI {
	
	@Autowired
	CategoryRepository categoryReposity;
	
//	@Autowired
//	CategoryRepository categoryReposity;
    
	@Autowired
	StorageRepository storageReposity;
	
	@Autowired
	StockBinRepository sbinReposity;
	
	@Autowired
	SysCodeRepository sysCodeReposity;
	
	@Autowired
	SupplierRepository spReps;
	
	@Autowired
	StationRepository sReps;
	
	@Autowired
	DepartmentRepository deptReps;
	
	@Autowired
	SysUserRepository sysuReps;
	
	// 物料大类列表
    @GetMapping("/category")
    @ApiOperation(value = "获取物料类别（大类）信息",notes="获取物料类别（大类）信息列表")
    public ResultEntity<List<CategoryEntity>> getCategorys(){
        return new ResultEntity<List<CategoryEntity>>(ResultStatus.OK.getCode(),
        											ResultStatus.OK.getMessage(),
        											categoryReposity.findAll(new Specification<CategoryEntity>() {

														@Override
														public Predicate toPredicate(Root<CategoryEntity> root,
																CriteriaQuery<?> query, CriteriaBuilder cb) {
															List<Predicate> list = new ArrayList<Predicate>();
															list.add(cb.equal(root.<String>get("parentid"), AppConstants.PARENT_CATEGORY_ID));
															query.orderBy(cb.desc(root.get("code")));
															Predicate[] pres = new Predicate[list.size()];
															pres = list.toArray(pres);
															return cb.and(pres);
														}
        												
        											}));
    }
   
    /*// 物料小类列表
    @GetMapping("/category/{parent_id}")
    @ApiOperation(value = "获取指定大类下的所有小类信息",notes="获取指定大类下的所有小类信息列表")
    @ApiImplicitParam(name="parent_id",value="大类类别ID",required=true, dataType="long",paramType="query")
    public ResultEntity<List<CategoryEntity>> getCategorysByParentId(@PathVariable("parent_id")Long id){
        return new ResultEntity<List<CategoryEntity>>(ResultStatus.OK.getCode(),
        											ResultStatus.OK.getMessage(),
        											categoryReposity.findByParentid(id));
    }*/
    
    // 物料小类列表（含 计量单位、规格型号）
    @GetMapping("/category/small/{parent_id}")
    @ApiOperation(value = "获取指定大类下的所有小类信息（含 计量单位、规格型号）",notes="获取指定大类下的所有小类信息列表（含 计量单位、规格型号）")
    @ApiImplicitParam(name="parent_id",value="大类类别ID",required=true, dataType="long",paramType="query")
    public ResultEntity<List<CategoryEntity3>> getCategorysForSmallByParentId(@PathVariable("parent_id") final long pId){
    	
    	List<Object> objList = categoryReposity.getSmallListWithSpecModelAndMUnit(pId);
    	List<CategoryEntity3> ceList = new ArrayList<CategoryEntity3>();
    	for(int i=0;i<objList.size();i++) {
    		Object[] obj = (Object[]) objList.get(i);
    		CategoryEntity3 ce = new CategoryEntity3();
    		ce.setId(Long.valueOf(obj[0].toString()));
    		ce.setParentid(Long.valueOf(obj[1].toString()));
    		ce.setCode(obj[2]==null?"":String.valueOf(obj[2]));
    		ce.setName(obj[3]==null?"":String.valueOf(obj[3]));
    		ce.setModel(obj[4]==null?"":String.valueOf(obj[4]));
    		ce.setMea_unit_name(obj[5]==null?"":String.valueOf(obj[5]));
    		ceList.add(ce);
    	}
    	
    	return new ResultEntity<List<CategoryEntity3>>(ResultStatus.OK.getCode(),
    			ResultStatus.OK.getMessage(),
    			ceList);
    }

    // 仓库列表
    @GetMapping("/storage")
    @ApiOperation(value = "获取仓库列表",notes="获取仓库列表")
    public ResultEntity<List<StorageEntity>> getStorages(){
        return new ResultEntity<List<StorageEntity>>(ResultStatus.OK.getCode(),
        											ResultStatus.OK.getMessage(),
        											storageReposity.findAll());
    }

    // 仓位区列表
	@GetMapping("/storage/{storage_id}/sbin")
    @ApiOperation(value = "获取仓位区列表（指定仓库）",notes="获取仓位区列表（指定仓库）")
	@ApiImplicitParams({
		@ApiImplicitParam(name="storage_id",value="所属仓库ID",dataType="long",paramType="query")
	})
    public ResultEntity<List<StockBinEntity>> getStockBinByStorageId(@PathVariable("storage_id") final Long storageId){
		List<StockBinEntity>  binAreaList = sbinReposity.findAll(new Specification<StockBinEntity>() {

			@Override
			public Predicate toPredicate(Root<StockBinEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.<Long>get("stg_id"), storageId));//所属仓库
				list.add(cb.equal(root.<String>get("sbin_type"), "01"));//01区
				
				query.orderBy(cb.desc(root.get("sbin_code")));//排序
				
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
			
		});
		
        return new ResultEntity<List<StockBinEntity>>(ResultStatus.OK.getCode(),
        											ResultStatus.OK.getMessage(),
        											binAreaList);
    }
   
	// 仓位架或位列表
	@GetMapping("/storage/{storage_id}/sbin/{parent_sbin_id}/{sbin_type}")
    @ApiOperation(value = "获取仓位架或位列表（指定仓位区或架）",notes="获取仓位架或位列表（指定仓位区或架）")
	@ApiImplicitParams({
		@ApiImplicitParam(name="storage_id",value="所属仓库ID",dataType="long",paramType="query"),
		@ApiImplicitParam(name="parent_sbin_id",value="仓位区ID或仓位架ID",dataType="long",paramType="query"),
		@ApiImplicitParam(name="sbin_type",value="类型：02架、03位",allowableValues="02,03", dataType="string",paramType="query")
	})
    public ResultEntity<List<StockBinEntity>> getStockBinForRackOrPos(@PathVariable("storage_id") final Long storageId,
    		@PathVariable("parent_sbin_id") final Long parentSbinId,
    		@PathVariable("sbin_type") final String sbinType){
		if(sbinType.equals("02") || sbinType.equals("03")) {//传值非法
		}else {
			return new ResultEntity<List<StockBinEntity>>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(),null);
		}
		List<StockBinEntity>  binList = sbinReposity.findAll(new Specification<StockBinEntity>() {

			@Override
			public Predicate toPredicate(Root<StockBinEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.<Long>get("stg_id"), storageId));
				list.add(cb.equal(root.<Long>get("parent_sbin_id"), parentSbinId));
				list.add(cb.equal(root.<String>get("sbin_type"), sbinType));
				query.orderBy(cb.desc(root.get("sbin_code")));//排序
				
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
			
		});
		
        return new ResultEntity<List<StockBinEntity>>(ResultStatus.OK.getCode(),
        											ResultStatus.OK.getMessage(),
        											binList);
    }

	// 计量单位列表
	// 物料大类列表
    /*@GetMapping("/meaunit")
    @ApiOperation(value = "获取计量单位列表",notes="获取计量单位列表")
    public ResultEntity<List<SysCodeEntity>> getMeaUnits(){
        return new ResultEntity<List<SysCodeEntity>>(ResultStatus.OK.getCode(),
        											ResultStatus.OK.getMessage(),
        											sysCodeReposity.findAll(new Specification<SysCodeEntity>() {

														@Override
														public Predicate toPredicate(Root<SysCodeEntity> root,
																CriteriaQuery<?> query, CriteriaBuilder cb) {
															List<Predicate> list = new ArrayList<Predicate>();
															list.add(cb.equal(root.<String>get("src_type"), AppConstants.SYS_CODE_RES_TYPE));
															query.orderBy(cb.desc(root.get("code")));
															Predicate[] pres = new Predicate[list.size()];
															pres = list.toArray(pres);
															return cb.and(pres);
														}
        												
        											}));
    }*/
	
/*    @GetMapping("/current_value")
    public ResultEntity<Integer> getCurrentValue(){
    	SequenceEntity seq = new SequenceEntity();
    	int  cv = seq.getCurrent_val();cv++;
    	seq.setCurrent_val(cv);
    	return new ResultEntity<Integer>(ResultStatus.OK.getCode(),ResultStatus.OK.getMessage(),
    			cv);
    }*/
    
    // 供应商列表
    @GetMapping("/supply")
    @ApiOperation(value = "获取供应商列表",notes="获取供应商列表")
    public ResultEntity<List<SupplierEntity>> getSuppliers(){
        return new ResultEntity<List<SupplierEntity>>(ResultStatus.OK.getCode(),
        											ResultStatus.OK.getMessage(),
        											spReps.findAll());
    }
    
    // 领料地点列表
    @GetMapping("/station")
    @ApiOperation(value = "获取领料地点列表",notes="获取领料地点列表")
    public ResultEntity<List<StationEntity>> getStations(){
    	return new ResultEntity<List<StationEntity>>(ResultStatus.OK.getCode(),
    			ResultStatus.OK.getMessage(),
    			sReps.findAll(new Specification<StationEntity>() {

					@Override
					public Predicate toPredicate(Root<StationEntity> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> list = new ArrayList<Predicate>();
						list.add(cb.equal(root.<String>get("pump_status"), "01"));//在用
						
						query.orderBy(cb.desc(root.get("pump_code")));
						Predicate[] pres = new Predicate[list.size()];
						pres = list.toArray(pres);
						return cb.and(pres);
					}
					
				}));
    }
    
    
    // 部门列表
    @GetMapping("/department")
    @ApiOperation(value = "获取部门列表",notes="获取部门列表")
    public ResultEntity<List<DepartmentDto>> getDepartments(){
    	List<Object> objList = deptReps.getAllDepartments();
    	List<DepartmentDto> deptList = new ArrayList<DepartmentDto>();
    	for(int i=0;i<objList.size();i++) {
    		Object[] obj = (Object[]) objList.get(i);
    		DepartmentDto dept = new DepartmentDto();
    		dept.setDept_id(Long.valueOf(obj[0].toString()));
    		dept.setDept_code(obj[1]==null?"":String.valueOf(obj[1]));
    		dept.setDept_name(obj[2]==null?"":String.valueOf(obj[2]));
    		deptList.add(dept);
    	}
    	
    	return new ResultEntity<List<DepartmentDto>>(ResultStatus.OK.getCode(),
    			ResultStatus.OK.getMessage(),
    			deptList);
    }
    
    // 获取采购人员
    @GetMapping("/stadmin_buyer")
    @ApiOperation(value = "获取采购人员列表",notes="获取采购人员列表")
//    @ApiImplicitParam(name = "type", value = "类型01:仓库管理员、02:采购员", allowableValues = "01,02", required = true, dataType = "string", paramType = "query") 
    public ResultEntity<List<UserDto>> getUserDto(){
    		Object[] obj = null;
//    		if("01".equals(type)) {
//    			obj = (Object[]) sysuReps.getStorageAdmin();
//    		}else if("02".equals(type)) {
//    		}
    		List<Object> objList = sysuReps.getBuyer();
    		List<UserDto> userList = new ArrayList<UserDto>();
    		for(int i=0;i<objList.size();i++) {
    			obj = (Object[]) objList.get(i);
    			UserDto user = new UserDto();
	    		user.setUser_id(obj[0]==null?0:Long.parseLong(String.valueOf(obj[0])));
	    		user.setUser_name(obj[1]==null?"":String.valueOf(obj[1]));
	    		user.setRole_name(obj[2]==null?"":String.valueOf(obj[2]));
	    		userList.add(user);
    		}
    	
    	return new ResultEntity<List<UserDto>>(ResultStatus.OK.getCode(),
    			ResultStatus.OK.getMessage(),
    			userList);
    }
    
}
