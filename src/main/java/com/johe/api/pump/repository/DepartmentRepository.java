package com.johe.api.pump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.johe.api.pump.entity.DepartmentEntity;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long>,
								JpaSpecificationExecutor<DepartmentEntity> {
	
	@Query(value="SELECT " + 
			"  d.dept_id," + 
			"  d.dept_code," + 
			"  CONCAT(d.dept_name,'(',o.org_name,')') AS dname" + 
			"FROM" + 
			"  pump_org_unit o" + 
			"  JOIN " + 
			"  pump_department d" + 
			"  ON d.org_id=o.org_id",
			nativeQuery=true)
	List<Object> getAllDepartments();

	
}
