package com.johe.api.pump.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johe.api.pump.dto.IllegalMaterialDto;
import com.johe.api.pump.dto.IllegalOutDto;
import com.johe.api.pump.dto.ResponseIllegalOutDto;
import com.johe.api.pump.entity.IllegalAlarmEntity;
import com.johe.api.pump.entity.MaterialEntity;
import com.johe.api.pump.entity.SysUserEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.IllegalAlarmRepository;
import com.johe.api.pump.repository.MaterialRepository;
import com.johe.api.pump.repository.OutOrderItemRepository;
import com.johe.api.pump.repository.SysUserRepository;
import com.johe.api.pump.service.IllegalAlarmService;

@Service
public class IllegalAlarmServiceImpl implements IllegalAlarmService {

	/*@Autowired
	InOrderRepository inOrderReps;
	
	@Autowired
	StorageRepository stgReps;
	
	@Autowired
	CategoryRepository cgReps;
	*/
	@Autowired
	OutOrderItemRepository outReps;
	
	@Autowired
	IllegalAlarmRepository illegalReps;
	
	@Autowired
	MaterialRepository mtReps;
	
	@Autowired
	SysUserRepository userReps;
	
	@Transactional
	@Override
	public IllegalAlarmEntity create(String src_id, String stg_code, String cg_big_code,
			 String cg_small_code, String sbin_code, String sbin_area_code,
			String sbin_rack_code, String sbin_pos_code, String bar_code, int out_qty, String mea_unit,
			String open_time, String close_time, String opt_time, String opt_person, String relate_code) {
		/*String[] sbinCodes = sbin_code.split(".");
		if(StringUtils.isAllEmpty(sbinCodes) || 
				StringUtils.isBlank(sbinCodes[0]) || 
				StringUtils.isBlank(sbinCodes[1]) || 
				StringUtils.isBlank(sbinCodes[2])) {
			return null;   
		}
		IllegalAlarmEntity ioe = new IllegalAlarmEntity();
		ioe.setSrc_id(src_id);
		ioe.setStg_code(stg_code);	
		StorageEntity se = stgReps.getByStgCode(stg_code);//仓库名称
		ioe.setStg_name(se == null?"":se.getStg_name());
		ioe.setCg_big_code(cg_big_code);
		CategoryEntity ceb= cgReps.findByCode(cg_big_code);//大类名称
		ioe.setCg_big_name(ceb == null?"":ceb.getName());
		ioe.setCg_small_code(cg_small_code);
		String smallCode = cg_big_code+"."+cg_small_code;
		CategoryEntity ces= cgReps.findByCode(smallCode);//小类名称
		ioe.setCg_small_name(ces == null?"":ces.getName());
		ioe.setSbin_code(sbin_code);
		ioe.setSbin_area_code(sbin_area_code);
		ioe.setSbin_rack_code(sbin_rack_code);
		ioe.setSbin_pos_code(sbin_pos_code);
		ioe.setOut_qty(out_qty);
		ioe.setBar_code(bar_code);
		ioe.setMea_unit(mea_unit);
		ioe.setOpen_time(open_time);
		ioe.setClose_time(close_time);
		ioe.setOpt_time(opt_time);
		ioe.setOpt_person(opt_person);
		ioe.setRelate_code(relate_code);
		
		if(se == null || ceb == null || ces == null) return null;
		
		// 比对是否为非法出库（仓库ID、
		long count = ooiReps.getItemByCategoryAndStgAndSbin(smallCode, bar_code, String.valueOf(se.getStg_id()), sbin_code);
		ioe.setIs_alarm(count == 0 ? "Y":"N");
		
		IllegalAlarmEntity ioeRet = ioReps.save(ioe);
		
		return ioeRet;*/
			return null;
	}

	@Transactional
	@Override
	public IllegalAlarmEntity create(IllegalOutDto ioDto) {
		
//		IllegalAlarmEntity ioe = new IllegalAlarmEntity();
		/*ioe.setSrc_id(ioDto.getSrc_id());
		ioe.setStg_code(ioDto.getStg_code());	
		
		StorageEntity se = stgReps.getByStgCode(ioDto.getStg_code());//仓库名称
		ioe.setStg_name(se == null?"":se.getStg_name());
		ioe.setCg_big_code(ioDto.getCg_big_code());
		
		CategoryEntity ceb= cgReps.findByCode(ioDto.getCg_big_code());//大类名称
		ioe.setCg_big_name(ceb == null?"":ceb.getName());
		ioe.setCg_small_code(ioDto.getCg_small_code());
		
		String smallCode = ioDto.getCg_big_code()+"."+ioDto.getCg_small_code();
		CategoryEntity ces= cgReps.findByCode(smallCode);//小类名称
		ioe.setCg_small_name(ces == null?"":ces.getName());
		String sbinCode = ioDto.getArea_code()+"."+ioDto.getRack_code()+"."+ioDto.getPos_code();//仓位代码=区代码.架代码.位代码
		ioe.setSbin_code(sbinCode);
		ioe.setSbin_area_code(ioDto.getArea_code());
		ioe.setSbin_rack_code(ioDto.getRack_code());
		ioe.setSbin_pos_code(ioDto.getPos_code());
		ioe.setOut_qty(ioDto.getOut_qty());
		ioe.setBar_code(ioDto.getBar_code());
		ioe.setMea_unit(ioDto.getMea_unit());
		ioe.setOpen_time(ioDto.getOpen_time());
		ioe.setClose_time(ioDto.getClose_time());
		ioe.setOpt_time(ioDto.getOpt_time());
		ioe.setOpt_person(ioDto.getOpt_person());
		ioe.setRelate_code("");
		
		if(se == null || ceb == null || ces == null) return null;
		
		// 比对是否为非法出库（仓库ID、
		long count = ooiReps.getItemByCategoryAndStgAndSbin(smallCode, ioDto.getBar_code(), String.valueOf(se.getStg_id()), sbinCode);
		ioe.setIs_alarm(count == 0 ? "Y":"N");
		
		IllegalAlarmEntity ioeRet = ioReps.save(ioe);
		
		return ioeRet;*/
		return null;
	}

	// 校验非法出库物料，添加非法预警
	@Override
	public ResponseIllegalOutDto checkIsIllegal(IllegalOutDto ioDto) {
		boolean isOk = true;
		ResponseIllegalOutDto res = new ResponseIllegalOutDto();
		List<IllegalMaterialDto> material = ioDto.getMaterial();
		if(material != null) {
			for(int i=0;i<material.size();i++) {
				IllegalMaterialDto dto = material.get(i);
				// 某条形码的操作时间在审核时间的3个小时外，认为是非法出库；
				// 操作时间 > 库管审核时间 + 3小时，非法出库
				/*long count = outReps.getCountByBarCodeAndAuditTime(dto.getBarcode(), ioDto.getOpttime());
				if(count > 0) {// 有出库单，但是出库时间在库管审核时间3小时之后了，被认定为非法出库
					IllegalAlarmEntity alarm = new IllegalAlarmEntity();
					alarm.setBarcode(dto.getBarcode());
					alarm.setOpen_time(ioDto.getOpentime());
					alarm.setClose_time(ioDto.getClosetime());
					alarm.setCre_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					alarm.setEqp_sn(ioDto.getEquipment());
					alarm.setOpt_person(ioDto.getOptperson());
					alarm.setOpt_time(ioDto.getOpttime());
					alarm.setReq_id(ioDto.getRequestid());
					alarm.setReq_time(ioDto.getRequesttime());
					illegalReps.save(alarm);
					isOk = false;
				} else {
					long cnt = outReps.getCountByBarCode(dto.getBarcode());
					if(cnt == 0) {// 此条形码压根不存在出库单中，有可能 是非法出库
						System.out.println(">>>>>>>>>>>>可疑非法出库条码:"+dto.getBarcode());
						//而且，此条码是在库状态，才认定为非法出库
						MaterialEntity mt = mtReps.findByBarcode(dto.getBarcode());
						if(mt != null && mt.getMt_in_remain_quantity() > 0) {//库存大于0，也就是说，库存量还有
							IllegalAlarmEntity alm = new IllegalAlarmEntity();
							alm.setBarcode(dto.getBarcode());
							alm.setOpen_time(ioDto.getOpentime());
							alm.setClose_time(ioDto.getClosetime());
							alm.setCre_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							alm.setEqp_sn(ioDto.getEquipment());
							alm.setOpt_person(ioDto.getOptperson());
							alm.setOpt_time(ioDto.getOpttime());
							alm.setReq_id(ioDto.getRequestid());
							alm.setReq_time(ioDto.getRequesttime());
							illegalReps.save(alm);
							System.out.println(">>>>>>>>>>>>确定非法出库条码:"+dto.getBarcode());
							isOk = false;
						}
					}
				}*/		
				
				
				//先判断是否有库存量，再判断是否有出库申请记录
				long cnt = outReps.getCountByBarCode(dto.getBarcode());
				if(cnt == 0) {// 此条形码压根不存在出库单中，有可能 是非法出库
					//System.out.println(">>>>>>>>>>>>可疑非法出库条码:"+dto.getBarcode());
					//而且，此条码是在库状态，才认定为非法出库
					MaterialEntity mt = mtReps.findByBarcode(dto.getBarcode());
					if(mt != null && mt.getMt_in_remain_quantity() > 0) {//库存大于0，也就是说，库存量还有
						IllegalAlarmEntity alm = new IllegalAlarmEntity();
						alm.setBarcode(dto.getBarcode());
						alm.setOpen_time(ioDto.getOpentime());
						alm.setClose_time(ioDto.getClosetime());
						alm.setCre_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						alm.setEqp_sn(ioDto.getEquipment());
						SysUserEntity user = userReps.findByUserCode(ioDto.getOptperson());
						alm.setOpt_person(user==null?ioDto.getOptperson():user.getUser_name());
						alm.setOpt_time(ioDto.getOpttime());
						alm.setReq_id(ioDto.getRequestid());
						alm.setReq_time(ioDto.getRequesttime());
						illegalReps.save(alm);
						//System.out.println(">>>>>>>>>>>>确定非法出库条码:"+dto.getBarcode());
						isOk = false;
					}
				} else {
					// 某条形码的操作时间在审核时间的3个小时外，认为是非法出库；
					// 操作时间 > 库管审核时间 + 3小时，非法出库
					long count = outReps.getCountByBarCodeAndAuditTime(dto.getBarcode(), ioDto.getOpttime());
					if(count > 0) {// 有出库单，但是出库时间在库管审核时间3小时之后了，被认定为非法出库
						MaterialEntity mt = mtReps.findByBarcode(dto.getBarcode());
						if(mt != null && mt.getMt_in_remain_quantity() > 0) {
							IllegalAlarmEntity alarm = new IllegalAlarmEntity();
							alarm.setBarcode(dto.getBarcode());
							alarm.setOpen_time(ioDto.getOpentime());
							alarm.setClose_time(ioDto.getClosetime());
							alarm.setCre_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							alarm.setEqp_sn(ioDto.getEquipment());
							SysUserEntity user = userReps.findByUserCode(ioDto.getOptperson());
							alarm.setOpt_person(user==null?ioDto.getOptperson():user.getUser_name());
							alarm.setOpt_time(ioDto.getOpttime());
							alarm.setReq_id(ioDto.getRequestid());
							alarm.setReq_time(ioDto.getRequesttime());
							illegalReps.save(alarm);
							isOk = false;
							//System.out.println(">>>>>超时，非法"+dto.getBarcode());
						}
					} 
				}
			}
		}
		
		res.setResponsetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		res.setResponseid(ioDto.getRequestid());
		if(isOk) {//合法
			res.setError(String.valueOf(ResultStatus.ILLEGAL_OUT_REQ_OK.getCode()));
			res.setData(ResultStatus.ILLEGAL_OUT_REQ_OK.getMessage());
		}else {//非法
			res.setError(String.valueOf(ResultStatus.ILLEGAL_OUT_BARCODE_ILLEGAL.getCode()));
			res.setData(ResultStatus.ILLEGAL_OUT_BARCODE_ILLEGAL.getMessage());
		}
		return res;
	}
	
	

}
