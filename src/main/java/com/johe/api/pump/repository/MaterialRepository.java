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

	@Query(value = "SELECT * FROM pump_material WHERE mt_barcode LIKE %?1%", nativeQuery = true)
	List<MaterialEntity> searchByBarcodeLike(String barCode);
	
	//@Query(value = "SELECT LEFT(mt_barcode,14) mt_barcode,mt_id,mt_code,mt_attr,mt_name,mt_fullname,mt_feature,"
	//		+ "mt_measure_unit,mt_category_big,mt_category_small,stg_id,sbin_area_id,sbin_rack_id,sbin_pos_id,sup_id FROM pump_material WHERE mt_barcode LIKE %?1%", nativeQuery = true)
//List<MaterialEntity> searchByBarcodeLike(String barCode);
	
	@Query(value = "SELECT * FROM pump_material WHERE mt_barcode=?1 limit 0,1", nativeQuery = true)
	MaterialEntity findByBarcode(String barCode);
	
	//查询调入的物料
	@Query(value = "SELECT * FROM pump_material WHERE mt_barcode=?1 and stg_id=?2 limit 0,1", nativeQuery = true)
	MaterialEntity getByBarcodeStgId(String barCode,long stg_id);
	
	@Query(value = "SELECT * FROM pump_material WHERE LEFT(mt_barcode,14)=LEFT(?1,14) and stg_id=?2 limit 0,1", nativeQuery = true)
	MaterialEntity getByBarcode(String barCode,long stg_id);

	@Modifying(clearAutomatically = true)//解释一下：辅助属性mt_assist_attr，用于存储 单价
	@Transactional
	@Query(value = "INSERT INTO pump_material(sup_id,stg_id,sbin_area_id,sbin_rack_id,sbin_pos_id,mt_code,mt_name,"
			+ "mt_fullname,mt_barcode,mt_category_big,mt_category_small,mt_feature,mt_measure_unit,mt_in_total_quantity,"
			+ "mt_in_remain_quantity,mt_status,mt_stock_status,mt_min_quantity,mt_max_quantity,mt_assist_attr) "
			+ "VALUES(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18,?19,?20)", nativeQuery = true)
	public void insert(long sup_id, long stg_id, long area_id, long rack_id, long pos_id, String mtCode, String name,
			String fullName, String barcode, long big_id, long small_id, String feature, String meaUnit,
			double total_qty, double rem_qty, String status, String sStaus, double min_qty, double max_qty,String price);

	//归还入库时，更新条码对应的库存
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE pump_material SET mt_in_total_quantity=mt_in_total_quantity+?1,mt_in_remain_quantity=mt_in_remain_quantity+?1 WHERE mt_barcode=?2 and stg_id=?3", nativeQuery = true)
	public void addQtyByBarcode18(double act_qty_total, String barcode18, long stg_id);
	
	//出库时
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE pump_material SET mt_in_total_quantity=?1, mt_in_remain_quantity=?2 WHERE mt_barcode=?3 and stg_id=?4", nativeQuery = true)
	public void updateQtyByBarcode18(double act_total_qty, double act_qty, String barcode18, long stg_id);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE pump_material SET mt_in_total_quantity=?1,mt_in_remain_quantity=?2 WHERE LEFT(mt_barcode,14)=LEFT(?2,14)  and stg_id=?3", nativeQuery = true)
	public void updateTotalQtyByBarcode14(double act_qty_total, String barcode18, long stg_id);
	
	//采购、加工、其他入库时，更新条码对应的此类物料的库存
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE pump_material SET mt_in_total_quantity=mt_in_total_quantity+?1 WHERE LEFT(mt_barcode,14)=LEFT(?2,14) and stg_id=?3", nativeQuery = true)
	public void addQtyByBarcode14(double act_qty, String barcode14, long stg_id);
	
	
	//出库时，更新条码对应的此类物料的库存
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE pump_material SET mt_in_total_quantity=mt_in_total_quantity-?1,mt_in_remain_quantity=mt_in_remain_quantity-?1 WHERE LEFT(mt_barcode,14)=LEFT(?2,14) and stg_id=?3", nativeQuery = true)
	public void subQtyByBarcode14(double act_qty, String barcode14, long stg_id);

	// 手动盘点，根据仓库、仓位，搜索出物料列表
	// 出库单，手动添加，物料列表
	@Query(value = "SELECT mt_id, mt_barcode,mt_fullname,mt_measure_unit,mt_category_big,mt_category_small,mt_feature,mt_code,mt_assist_attr FROM pump_material "
			+ " WHERE stg_id=?1 AND sbin_area_id=?2 AND sbin_rack_id=?3 AND sbin_pos_id=?4 GROUP BY mt_code", nativeQuery = true)
	List<Object> searchByStgIdAndBinId(long stg_id,long area_id,long rack_id,long pos_id);
	//手动入库时，生成不重复的条码
	@Query(value="SELECT CONCAT(LEFT(mt_barcode,14),LPAD(RIGHT(mt_barcode,4)+1,4,0)) FROM pump_material WHERE LEFT(mt_barcode,14) = ?1 and stg_id = ?2 ORDER BY mt_barcode DESC LIMIT 0,1",nativeQuery = true)
	public String makeBarcode(String barcode,long stg_id);
	
	// 手动盘点（库存数量）
	@Query(value="SELECT IFNULL(SUM(mt_in_remain_quantity),0) FROM pump_material WHERE stg_id=?1 AND sbin_area_id=?2 AND sbin_rack_id=?3 AND sbin_pos_id=?4 AND mt_category_big=?5 AND mt_category_small=?6 AND mt_status in('01','02','03')",nativeQuery=true)
	long getRemainQty(long stg_id,long area_id,long rack_id,long pos_id,long big_id,long small_id);		
	
	// 扫描盘点（库存数量）
	@Query(value="SELECT IFNULL(SUM(mt_in_remain_quantity),0) FROM pump_material WHERE LEFT(mt_barcode,14)=LEFT(?1,14) AND mt_status in('01','02','03')",nativeQuery=true)
	long getRemainQty(String strBarCode);	
	
}
