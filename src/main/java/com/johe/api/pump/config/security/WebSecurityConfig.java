package com.johe.api.pump.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.johe.api.pump.config.filter.JWTAuthenticationFilter;
import com.johe.api.pump.config.filter.JWTLoginFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
//	public WebSecurityConfig(UserDetailsService userDetailsService,BCryptPasswordEncoder bCryptPasswordEncoder){
//		this.userDetailsService = userDetailsService;
//		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs",
				"/swagger-resources/configuration/ui",
				"/swagger-resources",
				"/swagger-resources/configuration/security",
				"/swagger-ui.html",
				"/webjars/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.cors()
			.and()
			.csrf().disable()
//			.authorizeRequests().antMatchers(HttpMethod.POST,"/users/signup")// 所有 /users/signup 的POST请求 都放行
			.authorizeRequests().antMatchers("/login","/api/v1/illegal/upload")// 外部 上传非法出库数据 的请求放行
			.permitAll()
			.anyRequest().authenticated()  // 所有请求需要身份认证
			.and()
//			.formLogin()
//			.loginPage("/login")
//			.permitAll()
//			.and()
			.addFilter(new JWTLoginFilter(authenticationManager()))
			.addFilter(new JWTAuthenticationFilter(authenticationManager()));
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
		
		 // 使用自定义身份验证组件
//        auth.authenticationProvider(new CustomAuthenticationProvider(userDetailsService,bCryptPasswordEncoder));
	}
	
	
}
