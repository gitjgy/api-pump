package com.johe.api.pump.service;

public interface MessageService {
	
	void addAuditMsg(long order_id, String feature, String type, long make_id, String msgtype);
	
}
