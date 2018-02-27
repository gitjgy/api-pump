package com.johe.api.pump.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.johe.api.pump.entity.MessageEntity;
import com.johe.api.pump.repository.MessageRepository;
import com.johe.api.pump.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {
	private final static Map<String,String> bizTypeMap = new HashMap<String,String>(){{
			// 业务类型：01采购申请、02采购入库、03归还入库、04其他入库、05加工入库，06借用出库、07领料出库、08加工出库 09 调拨
			put("采购申请","01");
			put("采购入库","02");
			put("归还入库","03");
			put("其他入库","04");
			put("加工入库","05");
			put("借用出库","06");
			put("领料出库","07");
			put("加工出库","08");
			put("调拨单","09");
		}
	};
	

	@Autowired
	MessageRepository msgReps;
	
	@Override
	public void addAuditMsg(long order_id, String feature, String type, long make_id, String msgtype) {
		String audit_post = "00";// 审核职位：00普通
		String audit_red = "已申请 -> 等待审核 ";// 审核记录
		
		if (feature.equals("01") || feature.equals("02")) {
			audit_post = "03";// 10库管
			audit_red = "已申请 -> 等待库管审核 ";// 审核记录
		}
		
		if (feature.equals("03") || feature.equals("04")) {
			audit_post = "01";// 01主管领导
			audit_red = "已申请 -> 等待主管审核 ";// 审核记录
		}
		Date now = new Date();
		String strNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
		MessageEntity msg = new MessageEntity();
		msg.setAudit_post(audit_post);
		msg.setAudit_records(audit_red);
		msg.setBiz_type(bizTypeMap.get(msgtype));
		msg.setMsg_content("您有一条待办事宜["+msgtype+"]，请及时审核！");
		msg.setSend_time(strNow);
		msg.setCre_time(strNow);
		msg.setFinish_time(strNow);
		msg.setMsg_creator(make_id);
		msg.setRelate_id(order_id);

		msgReps.save(msg);

	}

}
