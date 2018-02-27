package com.johe.api.pump.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.dto.CheckRecordDto;
import com.johe.api.pump.dto.MaterialCheckDto;
import com.johe.api.pump.entity.CategoryEntity;
import com.johe.api.pump.entity.CheckRecordEntity;
import com.johe.api.pump.entity.InventoryBookEntity;
import com.johe.api.pump.entity.MaterialEntity;
import com.johe.api.pump.entity.StorageEntity;
import com.johe.api.pump.repository.CategoryRepository;
import com.johe.api.pump.repository.CheckRecordRepository;
import com.johe.api.pump.repository.InOrderRepository;
import com.johe.api.pump.repository.InventoryBookRepository;
import com.johe.api.pump.repository.MaterialRepository;
import com.johe.api.pump.repository.OutOrderRepository;
import com.johe.api.pump.repository.StockBinRepository;
import com.johe.api.pump.repository.StorageRepository;
import com.johe.api.pump.service.CheckRecordService;

@Service
public class CheckRecordServiceImpl implements CheckRecordService {
	@Autowired
	CheckRecordRepository crReps;
	
	@Autowired
	CategoryRepository cReps;
	
	@Autowired
	StockBinRepository sbReps;
	
	@Autowired
	StorageRepository stReps;
	
	@Autowired
	MaterialRepository mReps;
	
	@Autowired
	InOrderRepository ioReps;
	
	@Autowired
	OutOrderRepository ooReps;
	
	@Autowired
	InventoryBookRepository ibReps;
	
	

	@Transactional
	@Override
	public CheckRecordEntity create(CheckRecordDto dto) {
		
		CheckRecordEntity cre = new CheckRecordEntity();
		String barCode = dto.getBar_code();
		cre.setBar_code(barCode);
		
		CategoryEntity bigC = cReps.findOne(dto.getBig_id());
		CategoryEntity smallC = cReps.findOne(dto.getSmall_id());
		
		cre.setBig_code(bigC.getCode());
		cre.setBig_name(bigC.getName());
		cre.setSmall_code(smallC.getCode());
		cre.setSmall_name(smallC.getName());
		cre.setCheck_qty(dto.getCheck_qty());
		cre.setCheck_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		cre.setCheck_type(dto.getCheck_type());
		cre.setIn_stock_qty(dto.getIn_stock_qty());
		cre.setInit_qty(0);
		cre.setMea_unit(dto.getMea_unit());
		cre.setMt_code(smallC.getCode());
		cre.setMt_encode(barCode.substring(0, barCode.length()-4));
		cre.setMt_fullname(dto.getMt_fullname());
		cre.setOut_stock_qty(dto.getOut_stock_qty());
		cre.setPerson_id(dto.getPerson_id());
		cre.setPerson_name(dto.getPerson_name());
		// 计算盈亏
		String plQty = String.valueOf(dto.getCheck_qty() -dto.getStock_qty());
		cre.setPl_qty(plQty);
		cre.setRemark(dto.getRemark());
		cre.setSbin_code(dto.getSbin_code());
		cre.setStorage_code(dto.getStorage_code());
		cre.setStorage_name(dto.getStorage_name());		
		
		return crReps.save(cre);
	}

	@Override
	public MaterialCheckDto search(String smallCode,String stgCode,String areaCode,String rackCode,String posCode) throws Exception {
//		String smallCode = "";
//		String bigCode= "";
//		String tempCode = "";
//		if(small_code.indexOf(".")<0) {
//			bigCode=small_code.substring(0, 3);
//			tempCode=small_code.substring(3);
//			smallCode=bigCode+tempCode;
//		}
		MaterialCheckDto mcDto = new MaterialCheckDto();
		
		// 解析物料编码、仓位代码
		// 大类代码
//		String bigCode = materialEncode.substring(0, 3);
		mcDto.setBig_code("");
		// 大类名称
//		CategoryEntity ceb = cReps.findByCode(bigCode);
//		mcDto.setBig_name(ceb == null?"":ceb.getName());
		// 小类代码
//		String smallCode = materialEncode.substring(0, 6);
		mcDto.setSmall_code(smallCode);
		// 小类名称
		CategoryEntity ces = cReps.findByCode(smallCode);
		mcDto.setSmall_name(ces == null?"":ces.getName());
		
		// 品名
		mcDto.setMt_fullname(ces.getName());
		// 仓位代码
//		String sbinCode = materialEncode.substring(9);
		String sbinCode = areaCode+rackCode+posCode;
		mcDto.setSbin_code(sbinCode);
		// 仓位-区代码
//		String[] sbins = sbinCode.split(".");
		mcDto.setArea_code(areaCode);
		// 仓位-区名称
//		StockBinEntity sbeArea = sbReps.getByCodeAndType(sbins[0], "01");
//		mcDto.setArea_name(sbeArea.getSbin_name());
		// 仓位-架代码
		mcDto.setRack_code(rackCode);
		// 仓位-架名称
		// 仓位-位代码
		mcDto.setPos_code(posCode);
		// 仓位-位名称
		
		// 仓库代码
//		String stgCode = materialEncode.substring(6, 9);
		mcDto.setStorage_code(stgCode);
		// 仓库名称
		StorageEntity se = stReps.getByStgCode(stgCode);
		mcDto.setStorage_name(se == null?"":se.getStg_name());
		
		// 拼装 条形码
		String materialEncode=smallCode+stgCode+sbinCode;
		mcDto.setMt_encode(materialEncode);
		// 根据物料编码查询物料信息
		MaterialEntity material = mReps.findByBarcode(materialEncode);
		// 计量单位
		mcDto.setMea_unit(material == null?"":material.getMt_measure_unit());
		// 期初
		mcDto.setInit_qty(0L);
		// 入库
		long inQty = ioReps.getInQty(materialEncode);
		mcDto.setIn_stock_qty(inQty);
		// 出库
		long outQty = ooReps.getOutQty(materialEncode);
		mcDto.setOut_stock_qty(outQty);
		// 库存
		mcDto.setStock_qty(material==null?0L:material.getMt_in_remain_quantity());
		// 实盘
		mcDto.setCheck_qty(0L);
		// 盈亏
		mcDto.setPl_qty("--");
		
		// 盘点人
		mcDto.setPerson_name("");
		
		return mcDto;
	}

	
	@Override
	public MaterialCheckDto searchByBarcode(String barcode) throws Exception {
		MaterialCheckDto mc = new MaterialCheckDto();
		// 根据物料编码查询物料信息
		MaterialEntity mt = mReps.findByBarcode(barcode);
		if(mt == null) return null;
		long mt_id = mt.getMaterialid();
		InventoryBookEntity ibe = ibReps.findOne(mt_id);
		long initQty=0,inTotalQty=0,outTotalQty=0,inventoryQty=0;
		if(ibe != null) {
			// 期初(期初结存)：物料ID、最早时间
			initQty = ibReps.getInitQty(mt_id);
			// 入库(sum(出入库数量))：物料ID、收支类型=01（sum)
			inTotalQty = ibReps.getInTotalQty(mt_id);
			// 出库(sum(出入库数量))：物料ID、收支类型=02（sum)
			outTotalQty = ibReps.getOutTotalQty(mt_id);
			// 库存(当期结存)：物料ID、最近时间
			inventoryQty = ibReps.getInventoryQty(mt_id);
		}
		mc.setInit_qty(initQty);
		mc.setIn_stock_qty(inTotalQty);
		mc.setOut_stock_qty(outTotalQty);
		mc.setStock_qty(inventoryQty);
		// 仓库名称
		mc.setStorage_name(mt.getStorage().getStg_name());
		// 仓库code
		mc.setStorage_code(mt.getStorage().getStg_code());
		// 仓位CODE
		mc.setSbin_code(mt.getStockbin_area().getSbin_code()+mt.getStockbin_rack().getSbin_code()+mt.getStockbin_pos().getSbin_code());
		// 计量单位
		mc.setMea_unit(mt.getMt_measure_unit());
		// 品名
		mc.setMt_fullname(mt.getMt_fullname());
		
		mc.setBig_id(mt.getMt_category_big());
		mc.setSmall_id(mt.getMt_category_small());
		
//		mc.setBig_name(mt.getCategory_big().getName());
//		mc.setSmall_code(mt.getCategory_small().getCode());
//		mc.setSmall_name(mt.getCategory_small().getName());
		
		
		
		return mc;
	}

	@Override
	public MaterialCheckDto searchByMtId(long mt_id) throws Exception {
		MaterialCheckDto mc = new MaterialCheckDto();
		InventoryBookEntity ibe = ibReps.findOne(mt_id);
		long initQty =0,inTotalQty =0,outTotalQty =0,inventoryQty =0;
		if(ibe != null) {
			initQty = ibReps.getInitQty(mt_id);
			inTotalQty = ibReps.getInTotalQty(mt_id);
			outTotalQty = ibReps.getOutTotalQty(mt_id);
			inventoryQty = ibReps.getInventoryQty(mt_id);
		}
		// 期初(期初结存)：物料ID、最早时间
		mc.setInit_qty(initQty);
		// 入库(sum(出入库数量))：物料ID、收支类型=01（sum)
		
		mc.setIn_stock_qty(inTotalQty);
		// 出库(sum(出入库数量))：物料ID、收支类型=02（sum)
		mc.setOut_stock_qty(outTotalQty);
		// 库存(当期结存)：物料ID、最近时间
		mc.setStock_qty(inventoryQty);
		
		return mc;
	}

	

	
	
	
}
