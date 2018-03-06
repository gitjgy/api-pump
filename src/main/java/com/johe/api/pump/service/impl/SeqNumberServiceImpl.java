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
		String curDate = new SimpleDateFormat("yyyyMmdd").format(new Date());
		SeqNumberEntity sn = seqReps.findByBtypeAndYmdhms(btype, curDate);
		
		if(sn == null) {
			sn = new SeqNumberEntity();
			sn.setBtype(btype);
			sn.setYmdhms(curDate);
			sn.setCur_value("0001");//初始化
			sn.setCur_date(curDate);
			seqReps.save(sn);
		}else {
			long curValue= Long.parseLong(sn.getCur_value());
			String curValueStr = String.valueOf(++curValue);
			for(int i=0;i<4-curValueStr.length();i++) {
				curValueStr+="0";
			}
			sn.setCur_value(curValueStr);
			seqReps.saveAndFlush(sn);
		}
		
		SeqNumberEntity snNew = seqReps.findByBtypeAndYmdhms(btype, curDate);
		if(snNew == null) {
			return "0000";
		}
		return snNew.getBtype() + snNew.getYmdhms() + snNew.getCur_value();
	}

}
