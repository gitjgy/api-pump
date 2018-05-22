package com.johe.api.pump;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.johe.api.pump.service.impl.UserDetailsServiceImpl;

@EnableTransactionManagement 
@SpringBootApplication
//public class ApiPumpApplication {// jar运行测试
public class ApiPumpApplication extends SpringBootServletInitializer {//=====================================打war包
//	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ApiPumpApplication.class);
	}
//====================================END WAR
	public static void main(String[] args) {
		SpringApplication.run(ApiPumpApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
}
