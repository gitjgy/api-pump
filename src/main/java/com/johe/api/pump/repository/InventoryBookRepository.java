package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.johe.api.pump.entity.InventoryBookEntity;

public interface InventoryBookRepository extends JpaRepository<InventoryBookEntity, Long>,
								JpaSpecificationExecutor<InventoryBookEntity> {
	// 期初(期初结存)：物料ID、最早时间
	@Query(value="SELECT last_qty FROM pump_inventory_book p WHERE p.mt_id=?1 ORDER BY p.cre_date ASC limit 0,1",nativeQuery=true)
	long getInitQty(long mt_id);	
	
	// 入库(sum(出入库数量))：物料ID、收支类型=01（sum)
	@Query(value="SELECT sum(in_out_qty) FROM pump_inventory_book p WHERE p.mt_id=?1 AND p.in_out_type=01",nativeQuery=true)
	long getInTotalQty(long mt_id);		
	
	// 出库(sum(出入库数量))：物料ID、收支类型=02（sum)
	@Query(value="SELECT sum(in_out_qty) FROM pump_inventory_book p WHERE p.mt_id=?1 AND p.in_out_type=02",nativeQuery=true)
	long getOutTotalQty(long mt_id);		
	
	// 库存(当期结存)：物料ID、最近时间
	@Query(value="SELECT cur_qty FROM pump_inventory_book p WHERE p.mt_id=?1 ORDER BY p.cre_date DESC limit 0,1",nativeQuery=true)
	long getInventoryQty(long mt_id);		
}
