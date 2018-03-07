package com.johe.api.pump.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.johe.api.pump.entity.SeqNumberEntity;
import com.johe.api.pump.repository.SeqNumberRepository;
import com.johe.api.pump.service.SeqNumberService;

@Service
public class SeqNumberServiceImpl implements SeqNumberService {

	@Autowired
	SeqNumberRepository seqReps;

//	@Transactional
	@Override
	public String queryByBiztype(String btype) {
		Date today = new Date();
		String curDate = new SimpleDateFormat("yyyy-MM-dd").format(today);
		String curYmdhms = new SimpleDateFormat("yyyyMMdd").format(today);
		SeqNumberEntity sn = seqReps.findByBtype(btype);
		
		if(sn == null) {
			sn = new SeqNumberEntity();
			sn.setBtype(btype);
			sn.setYmdhms(curYmdhms);
			sn.setCur_value("0001");//初始化
			sn.setCur_date(curDate);
			seqReps.save(sn);
		}else {
			long curValue= Long.parseLong(sn.getCur_value());
			if(curYmdhms.equals(sn.getYmdhms())) {//当天，则累加
				String tempCurValue = String.valueOf(++curValue);
				String curValueStr = tempCurValue;
				for(int i=0;i<(4-tempCurValue.length());i++) {
					curValueStr="0"+curValueStr;
				}
				sn.setCur_value(curValueStr);
			}else {//非当天，则初始化0001
				sn.setCur_value("0001");
			}
			sn.setCur_date(curDate);
			sn.setYmdhms(curYmdhms);
			seqReps.saveAndFlush(sn);
		}
		
		SeqNumberEntity snNew = seqReps.findByBtype(btype);
		if(snNew == null) {
			return "0000";
		}
		return snNew.getBtype() + snNew.getYmdhms() + snNew.getCur_value();
	}

}
