package com.johe.api.pump.entity.result;

import org.springframework.data.domain.Page;

import com.johe.api.pump.entity.MaterialEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultEntity<T> {
	private int code;
	private String message;
	private T data;	
//	private Page<T> pageData;	
	
	public ResultEntity(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
//	public ResultEntity(int code, String message, Page<T> pagedata) {
//		this.code = code;
//		this.message = message;
//		this.pageData = pagedata;
//	}
	
}
