package com.johe.api.pump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.johe.api.pump.entity.SysUserEntity;

public interface SysUserRepository extends JpaRepository<SysUserEntity, Long>,
								JpaSpecificationExecutor<SysUserEntity> {
	
	SysUserEntity findByAccount(String account);
	
	SysUserEntity findByAccountAndPwd(String account,String pwd);
	
	SysUserEntity findByAccountAndPwdAndWkno(String account,String pwd,String workerno);
	
	@Query(value="SELECT md.`PARENT_ID`,md.`PDA_MENU_ID`,md.`PDA_MENU_NAME` FROM t_sys_user u " + 
			"LEFT JOIN t_sys_role_user ru  ON u.USER_ID=ru.`USER_ID` " + 
			"LEFT JOIN t_sys_role r ON r.`ROLE_ID`=ru.`ROLE_ID`" + 
			"LEFT JOIN t_sys_role_menu_func_pda rmf ON rmf.`ROLE_ID`=r.`ROLE_ID` " + 
			"LEFT JOIN t_sys_menu_pda md ON rmf.`PDA_MENU_ID`=md.`PDA_MENU_ID`" + 
			"WHERE u.`POLICY_NO`='N' " + 
			"AND u.`ACCOUNT`=?1 " + 
			"AND u.`PWD`=?2 " + 
			"AND md.`PDA_MENU_NAME` IS NOT NULL",nativeQuery=true)//01主管、02上级、03库管 、04普通
	List<Object> loginByAccountAndPwd(String account,String pwd);
	
}
