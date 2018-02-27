package com.johe.api.pump.config.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.util.AppConstants;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

// 校验Token
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response, 
									FilterChain chain)
					throws IOException, ServletException {
		String token = request.getHeader(AppConstants.AUTH_HEADER);
		
		if(token == null || !token.startsWith(AppConstants.AUTH_BEARER)) {
			chain.doFilter(request, response);
			return;
		}
		
		try {
			String user = Jwts.parser()
						.setSigningKey(AppConstants.SIGN_KEY)
						.parseClaimsJws(token.replace(AppConstants.AUTH_BEARER, ""))
						.getBody()
						.getSubject();
			UsernamePasswordAuthenticationToken authentication = null;
			if(user != null) {
				ArrayList<GrantedAuthority> authorities = new ArrayList<>();				
				authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
			}
			SecurityContextHolder.getContext().setAuthentication(authentication);  
		    chain.doFilter(request, response);
	
		    response.setCharacterEncoding("UTF-8");
		    response.setContentType("application/json;charset=utf-8");
		} catch (ExpiredJwtException e) {
			response.getWriter().write(new ObjectMapper().writeValueAsString(
					new ResultEntity<Object>(
						ResultStatus.TOKEN_EXPIRED_ERROR.getCode(),
						ResultStatus.TOKEN_EXPIRED_ERROR.getMessage(),
						e.getMessage())));
		} 
		catch (MalformedJwtException e) {
			e.printStackTrace();
			response.getWriter().write(new ObjectMapper().writeValueAsString(
					new ResultEntity<Object>(
							ResultStatus.TOKEN_FORMAT_ERROR.getCode(),
							ResultStatus.TOKEN_FORMAT_ERROR.getMessage(),
							e.getMessage())));
		}
		catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(new ObjectMapper().writeValueAsString(
					new ResultEntity<Object>(
							ResultStatus.TOKEN_OTHER_ERROR.getCode(),
							ResultStatus.TOKEN_OTHER_ERROR.getMessage(),
							e.getMessage())));
		}
	}
	
	/*@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, 
			FilterChain chain)
					throws IOException, ServletException {
		String token = request.getHeader(AppConstants.AUTH_HEADER);
		
		if(token == null || !token.startsWith(AppConstants.AUTH_BEARER)) {
			chain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);  
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		try {
			String user = Jwts.parser()
						.setSigningKey(AppConstants.SIGN_KEY)
						.parseClaimsJws(token.replace(AppConstants.AUTH_BEARER, ""))
						.getBody()
						.getSubject();
			if(user != null) {
				ArrayList<GrantedAuthority> authorities = new ArrayList<>();
				
				return new UsernamePasswordAuthenticationToken(user, null, authorities);
			}
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			return null;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedJwtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedJwtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}*/
	
//	@ExceptionHandler(value=ExpiredJwtException.class)
//	@ResponseBody
//	public ResultEntity<String> expiredJwtExceptionHandler(ExpiredJwtException exception) throws Exception{
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>TOKEN过期了....................");
//		return new ResultEntity<String>(ResultStatus.TOKEN_EXPIRED_EXCEPTION.getCode(),
//				ResultStatus.TOKEN_EXPIRED_EXCEPTION.getMessage(), 
//				null);
//	}
}
