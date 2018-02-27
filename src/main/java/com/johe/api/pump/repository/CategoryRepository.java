package com.johe.api.pump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.johe.api.pump.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>,
								JpaSpecificationExecutor<CategoryEntity> {
	
	List<CategoryEntity> findByParentid(Long parentCategoryId);

	CategoryEntity findByCodeAndParentid(String code,String parentid);
	
	CategoryEntity findByCode(String code);
	
	@Query(value="SELECT p.category_id,p.parent_category_id,p.category_code,p.category_name,"
			+ "p.spec_model,t.CODE_NAME FROM pump_material_category  p, "
			+ " t_sys_code t WHERE t.FIELD='JLDW' AND t.CODE=p.mea_unit_code AND P.parent_category_id=?1",
			nativeQuery=true)
	List<Object> getSmallListWithSpecModelAndMUnit(long pId);
}
