package com.johe.api.pump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.entity.MaterialEntity;
import com.johe.api.pump.entity.MaterialListEntity;

public interface MaterialRepository
		extends JpaRepository<MaterialEntity, Long>, JpaSpecificationExecutor<MaterialEntity> {

	MaterialEntity findByMaterialid(long materialId);

	List<MaterialEntity> findByBarcodeLike(String barCode);

	@Query(value = "SELECT * FROM pump_material WHERE barcode LIKE %?1%", nativeQuery = true)
	List<MaterialEntity> searchByBarcodeLike(String barCode);

	MaterialEntity findByBarcode(String barCode);

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "INSERT INTO pump_material(sup_id,stg_id,sbin_area_id,sbin_rack_id,sbin_pos_id,mt_code,mt_name,"
			+ "mt_fullname,mt_barcode,mt_category_big,mt_category_small,mt_feature,mt_measure_unit,mt_in_total_quantity,"
			+ "mt_in_remain_quantity,mt_status,mt_stock_status,mt_min_quantity,mt_max_quantity) "
			+ "VALUES(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18,?19)", nativeQuery = true)
	public void insert(long sup_id, long stg_id, long area_id, long rack_id, long pos_id, String mtCode, String name,
			String fullName, String barcode, long big_id, long small_id, String feature, String meaUnit,
			double total_qty, double rem_qty, String status, String sStaus, double min_qty, double max_qty);

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE pump_material SET mt_in_total_quantity=mt_in_total_quantity+?1,mt_in_remain_quantity=mt_in_remain_quantity+?2 WHERE mt_id=?3", nativeQuery = true)
	public void update(double act_qty1, double act_qty2, long mt_id);

	// 手动盘点，根据仓库、仓位，搜索出物料列表
	@Query(value = "SELECT mt_id,mt_barcode,mt_fullname,mt_measure_unit,mt_category_big,mt_category_small,mt_feature,mt_code FROM pump_material "
			+ " WHERE stg_id=?1 AND sbin_area_id=?2 AND sbin_rack_id=?3 AND sbin_pos_id=?4", nativeQuery = true)
	List<Object> searchByStgIdAndBinId(long stg_id,long area_id,long rack_id,long pos_id);
}
