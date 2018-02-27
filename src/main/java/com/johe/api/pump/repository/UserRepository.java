package com.johe.api.pump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.johe.api.pump.entity.UserInfo;

public interface UserRepository /*extends JpaRepository<UserInfo, Integer>*/{

	UserInfo findByUName(String username);
}
