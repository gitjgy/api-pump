package com.johe.api.pump.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.johe.api.pump.entity.SysUserEntity;
import com.johe.api.pump.repository.SysUserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	SysUserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
		SysUserEntity user = userRepository.findByAccount(account);
		if(user == null) {
			throw new UsernameNotFoundException(account);
		}
		ArrayList<GrantedAuthority> authorities = new ArrayList<>();
		return new org.springframework.security.core.userdetails.User(user.getAccount(),user.getPwd(),authorities); 
	}

}
