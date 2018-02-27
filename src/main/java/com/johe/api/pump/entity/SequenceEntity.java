package com.johe.api.pump.entity;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.Setter;

public class SequenceEntity {
	@Value("${com.johe.api.pump.orderno.current.value}")
	@Setter @Getter private int current_val;
	
}
