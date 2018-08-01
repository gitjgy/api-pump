package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.johe.api.pump.entity.InventoryBookEntity;

public interface InventoryBookRepository extends JpaRepository<InventoryBookEntity, Long>,
								JpaSpecificationExecutor<InventoryBookEntity> {
	// 期初(期初结存)：物料ID、最早时间
	@Query(value="SELECT last_qty FROM pump_inventory_book p WHERE p.mt_barcode=?1 ORDER BY p.last_qty ASC limit 0,1",nativeQuery=true)
	Object getInitQty(String mt_barcode);	
	
	// 入库(sum(出入库数量))：物料ID、收支类型=01（sum)
	@Query(value="SELECT sum(in_out_qty) FROM pump_inventory_book p WHERE p.mt_barcode=?1 AND p.in_out_type='01'",nativeQuery=true)
	Object getInTotalQty(String mt_barcode);	
	
	// 出库(sum(出入库数量))：物料ID、收支类型=02（sum)
	@Query(value="SELECT sum(in_out_qty) FROM pump_inventory_book p WHERE p.mt_barcode=?1 AND p.in_out_type='02'",nativeQuery=true)
	Object getOutTotalQty(String mt_barcode);		
	
	// 库存(当期结存)：物料ID、最近时间
	@Query(value="SELECT last_qty FROM pump_inventory_book p WHERE p.mt_barcode=?1 ORDER BY p.cur_qty DESC limit 0,1",nativeQuery=true)
	Object getInventoryQty(String mt_barcode);	
	
	// 根据物料条码（前14位）获取库存台账
	@Query(value="SELECT * FROM pump_inventory_book p WHERE p.mt_barcode=?1 limit 0,1",nativeQuery=true)
	InventoryBookEntity getEntityByBarcode(String barcode);

}
