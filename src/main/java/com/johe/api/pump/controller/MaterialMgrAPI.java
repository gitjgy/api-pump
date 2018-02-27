package com.johe.api.pump.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.johe.api.pump.entity.MaterialEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.MaterialRepository;
import com.johe.api.pump.util.AppConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="物料管理",tags="【物料管理】接口")
@RequestMapping("/api/v1/materials")
@RestController
public class MaterialMgrAPI {
	
	@Autowired
	MaterialRepository materialReposity;
    
    @GetMapping("/{feature}/{big_id}/{small_id}/{storage_id}/{page}/{size}")
    @ApiOperation(value = "根据特性、大类、小类、仓库、页码、条数，获取物料信息",notes="根据特性、大类、小类、仓库、页码、条数，获取物料信息列表")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="feature",value="物料特性:000:全部、001:市采、002:普通、003:重要、004:关键",required=true, dataType="String",paramType="query"),
    	@ApiImplicitParam(name="big_id",value="大类ID（-1:全部）",required=true, dataType="long",paramType="query"),
    	@ApiImplicitParam(name="small_id",value="小类ID（-1:全部）",required=true, dataType="long",paramType="query"),
    	@ApiImplicitParam(name="storage_id",value="所属仓库ID（-1:全部）",required=true, dataType="long",paramType="query"),
    	@ApiImplicitParam(name="page",value="页码（从0开始）",required=true, dataType="int",paramType="query"),
    	@ApiImplicitParam(name="size",value="每页显示条数",required=true, dataType="int",paramType="query")
    	})    
    public ResultEntity<Page<MaterialEntity>> getMaterials(@PathVariable("feature")String feature, 
												    		@PathVariable("big_id")long bigId, 
												    		@PathVariable("small_id")long smallId, 
												    		@PathVariable("storage_id")long storageId, 
												    		@PathVariable("page")int page, 
												    		@PathVariable("size")int size){
    	final String mtFeature = feature;
    	final long mtBigId = bigId;
    	final long mtSmallId = smallId;
    	final long mtStorageId = storageId;
    	if(mtFeature.equals("000") || mtFeature.equals("001") || mtFeature.equals("002") || mtFeature.equals("003") || mtFeature.equals("004")) {
    	}else {
    		return new ResultEntity<Page<MaterialEntity>>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
        			ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(),
        			null);
    	}
    	Page<MaterialEntity>  materials = materialReposity.findAll(new Specification<MaterialEntity>() {

			@Override
			public Predicate toPredicate(Root<MaterialEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();

				
//				Join<MaterialEntity,StorageEntity> storageJoin = root.join(root.getModel().getSingularAttribute("storage",StorageEntity.class),JoinType.LEFT);
//				list.add(cb.equal(root.<Long>get("stg_id"), storageJoin.<Long>get("stg_id")));
				
				
//				Join<StorageEntity,MaterialEntity> storageJoin = root.join("storage",JoinType.LEFT);
//				SetJoin<MaterialEntity,StorageEntity> storageJoin = root.join(root.getModel().getSet("storage",StorageEntity.class),JoinType.LEFT);
				if(!mtFeature.equals(AppConstants.MATERIAL_ATTR_ALL)) {
					list.add(cb.equal(root.<String>get("mt_attr"), mtFeature));
				}
				if(mtBigId != AppConstants.QUERY_ID_ALL) {
					list.add(cb.equal(root.<Long>get("mt_category_big"), mtBigId));
				}
				if(mtSmallId != AppConstants.QUERY_ID_ALL) {
					list.add(cb.equal(root.<Long>get("mt_category_small"), mtSmallId));
				}
				if(mtStorageId != AppConstants.QUERY_ID_ALL) {
					list.add(cb.equal(root.<Long>get("stg_id"), mtStorageId));
				}
				
				query.orderBy(cb.desc(root.get("materialid")));				
				Predicate[] pres = new Predicate[list.size()];
				pres = list.toArray(pres);
				
				return cb.and(pres);
				/*root = query.from(MaterialEntity.class);
//				return cb.equal(root.get("mt_attr"), "001");
				
				Path<String> attr = root.get("mt_attr");
				query.where(cb.equal(attr, "001"));
				return null;*/
				
				
				
//				query.where(mtAttr);
				
			}
    		
    	}, new PageRequest(page, size, null));
    	
        return new ResultEntity<Page<MaterialEntity>>(ResultStatus.OK.getCode(),
        											ResultStatus.OK.getMessage(),
        											materials);
    }
    
    @GetMapping("/{material_id}")
    @ApiOperation(value = "根据物料ID，获取物料详细信息",notes="根据物料ID，获取物料详细信息")
    @ApiImplicitParam(name="material_id",value="物料ID",required=true, dataType="long",paramType="query")
    public ResultEntity<MaterialEntity> getMaterailById(@PathVariable("material_id")long materialId){
    	return new ResultEntity<MaterialEntity>(ResultStatus.OK.getCode(),
				ResultStatus.OK.getMessage(),
				materialReposity.findByMaterialid(materialId));
    }
    
    @GetMapping("/search/{bar_code}")
    @ApiOperation(value = "条形码模糊搜索物料",notes="条形码模糊搜索物料")
    @ApiImplicitParam(name="bar_code",value="物料条形码",required=true, dataType="string",paramType="query")
    public ResultEntity<List<MaterialEntity>> getMaterailByBarCode(@PathVariable("bar_code")String barCode){
//    	System.out.println("[模糊搜索]"+barCode);
    	return new ResultEntity<List<MaterialEntity>>(ResultStatus.OK.getCode(),
    			ResultStatus.OK.getMessage(),
    	materialReposity.searchByBarcodeLike(barCode));
    }
    
    @GetMapping("/scansearch/{bar_code}")
    @ApiOperation(value = "根据条形码搜索物料",notes="根据条形码搜索物料")
    @ApiImplicitParam(name="bar_code",value="物料条形码",required=true, dataType="string",paramType="query")
    public ResultEntity<MaterialEntity> searchaterailByBarCode(@PathVariable("bar_code")String barCode){
//    	System.out.println("[扫码搜索]"+barCode);
    	return new ResultEntity<MaterialEntity>(ResultStatus.OK.getCode(),
    			ResultStatus.OK.getMessage(),
    			materialReposity.findByBarcode(barCode));
    }
}
