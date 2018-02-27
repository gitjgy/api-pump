package com.johe.api.pump.config.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.johe.api.pump.entity.LoginEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.util.AppConstants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	
	public JWTLoginFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	// 接受并解析用户凭证
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, 
												HttpServletResponse response) throws AuthenticationException {
		try {
//			SysUserEntity user = new ObjectMapper().readValue(request.getInputStream(), SysUserEntity.class);
			LoginEntity user = new ObjectMapper().readValue(request.getInputStream(), LoginEntity.class);
			ArrayList<GrantedAuthority> authorities = new ArrayList<>();
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							user.getAccount(), user.getPwd(), authorities));
		} catch (UnrecognizedPropertyException e) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=utf-8");
			ResultEntity<Object> result = new ResultEntity<Object>(ResultStatus.LOGIN_PARAM_FIELD_UNKNOW.getCode(),
																	ResultStatus.LOGIN_PARAM_FIELD_UNKNOW.getMessage(),
																	null);
			ObjectMapper mapper = new ObjectMapper();
			try {
				response.getWriter().write(mapper.writeValueAsString(result));
			} catch (IOException e1) {}
			return null;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} 
	}

	// 用户登陆成功后，此方法会被调用（生成Token）
	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
											HttpServletResponse response, 
											FilterChain chain,
											Authentication auth) throws IOException, ServletException {
		
		Date expiresIn = new Date(System.currentTimeMillis() + AppConstants.EXPIRES_IN_SECOND * 1000);
		String token = Jwts.builder()
							.setSubject(((User)auth.getPrincipal()).getUsername())
							.setExpiration(expiresIn)// 有效期，24小时转换成毫秒
							.signWith(SignatureAlgorithm.HS512, AppConstants.SIGN_KEY)
							.compact();
//		response.addHeader(Contants.AUTH_HEADER, Contants.AUTH_BEARER + token);
		
		// TOKEN返回给客户端
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		
		Map<String,Object> accessToken = new HashMap<String,Object>();
		accessToken.put("access_token", token);	
		accessToken.put("token_type", "Bearer ");	
		accessToken.put("expires_in", AppConstants.EXPIRES_IN_SECOND);	
		
		ObjectMapper mapper = new ObjectMapper();
		ResultEntity<Object> result = new ResultEntity<Object>(ResultStatus.OK.getCode(),
																ResultStatus.OK.getMessage(),
																accessToken);
		response.getWriter().write(mapper.writeValueAsString(result));
	}
	
	// 用户登陆失败后，此方法被调用
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
//		super.unsuccessfulAuthentication(request, response, failed);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		ResultEntity<Object> result = new ResultEntity<Object>(ResultStatus.AUTHENTICATION_FAILED.getCode(),
																ResultStatus.AUTHENTICATION_FAILED.getMessage(),
																failed.getMessage());
		ObjectMapper mapper = new ObjectMapper();
		response.getWriter().write(mapper.writeValueAsString(result));
	}
}
