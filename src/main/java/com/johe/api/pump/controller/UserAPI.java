package com.johe.api.pump.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.johe.api.pump.dto.PdaMenuDto;
import com.johe.api.pump.dto.SysUserDto;
import com.johe.api.pump.entity.SysUserEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.SysUserRepository;
import com.johe.api.pump.util.AppConstants;
import com.johe.api.pump.util.Base64Utils;
import com.johe.api.pump.util.MD5Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="用户管理",tags="【用户身份验证】接口")
@RequestMapping("/api/v1/users")
@RestController
public class UserAPI {
//	static Map<Integer,MyBody> bodys = Collections.synchronizedMap(new HashMap<Integer,MyBody>());
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	SysUserRepository userReposity;
	
	/*@PostMapping("/signup")
	public ResultEntity<SysUserEntity> signup(@RequestBody UserInfo user@RequestBody SysUserEntity user) {
//      UserInfo user1 = userReposity.findByUName(user.getUName());
//      if(null != user1){
//          throw new UsernameIsExitedException("用户已经存在~");
//      }
//      user.setUPwd(bCryptPasswordEncoder.encode(user.getUPwd()));
//	  userReposity.save(user);
	  SysUserEntity sysUser = userReposity.findByAccount(user.getAccount());
	  if(null != sysUser){
		  return new ResultEntity<SysUserEntity>(ResultStatus.ACCOUNT_IS_EXISTED.getCode(),ResultStatus.ACCOUNT_IS_EXISTED.getMessage(),user);
	  }
	  
	  user.setPwd(bCryptPasswordEncoder.encode(user.getPwd()));
	  userReposity.save(user);
	  
	  return new ResultEntity<SysUserEntity>(ResultStatus.ACCOUNT_IS_EXISTED.getCode(),ResultStatus.ACCOUNT_IS_EXISTED.getMessage(),user);
	}*/
	
    @PostMapping("/login")
    @ApiOperation(value = "验证账号密码",notes="验证账号密码")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="user_account",value="登录账号",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="password",value="登录密码（密文）",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="login_type",value="登录标识:Y库管登录、N身份验证",allowableValues="Y,N",required=true, dataType="string",paramType="form")
    })
    public ResultEntity<SysUserDto> login(@RequestParam("user_account")String account,@RequestParam("password")String pwd,@RequestParam("login_type")String loginType){
    	if(loginType.equals("Y") || loginType.equals("N")) {
    	}else {
    		return new ResultEntity<SysUserDto>(ResultStatus.ARGUMENT_VALUE_ILLEGAL.getCode(),
					ResultStatus.ARGUMENT_VALUE_ILLEGAL.getMessage(),null);
    	}
    	
    	//解密 
    	String strPwd="";
    	strPwd = MD5Util.decode(pwd, AppConstants.PWD_KEY);
    	//加密
    	String base64Pwd = Base64Utils.encryptBase64(strPwd,Base64Utils.encryptKey);
    	SysUserEntity sysUser= null;
    	sysUser= userReposity.findByAccountAndPwd(account, base64Pwd);//身份验证
    	if(sysUser == null) {
    		return new ResultEntity<SysUserDto>(ResultStatus.LOGIN_FAILED.getCode(),ResultStatus.LOGIN_FAILED.getMessage(),null);
    	}
    	// 取权限菜单
    	List<PdaMenuDto> menulist = new ArrayList<PdaMenuDto>();
		List<Object> objList = userReposity.loginByAccountAndPwd(account, base64Pwd);
		if(objList != null) {
			for(int i=0;i<objList.size();i++) {
				Object[] obj = (Object[]) objList.get(i);
						
				PdaMenuDto pm = new PdaMenuDto();
				pm.setParent_id(Long.parseLong(String.valueOf(obj[0])));
				pm.setMenu_id(Long.parseLong(String.valueOf(obj[1])));
				pm.setMeun_name(String.valueOf(obj[2]));
				menulist.add(pm);
			}
		}
		SysUserDto userDto = new SysUserDto();
		userDto.setAccount(sysUser.getAccount());
		userDto.setCert_no(sysUser.getCert_no());
		userDto.setDept(sysUser.getDept());
		userDto.setOrg_id(sysUser.getOrg_id());
		userDto.setUser_id(sysUser.getUser_id());
		userDto.setUser_name(sysUser.getUser_name());
		userDto.setWkno(sysUser.getWkno());
		userDto.setMenu_list(menulist);
		
//    	if(loginType.equals("Y")) {//
//    		sysUser= userReposity.findByAccountAndPwdAndWkno(account, base64Pwd, AppConstants.WORKER_NO_STORAGE_MGR);//库管登录
//    	}
//    	if(loginType.equals("N")) {
//    		sysUser= userReposity.findByAccountAndPwd(account, base64Pwd);//身份验证
//    	}
        return new ResultEntity<SysUserDto>(ResultStatus.OK.getCode(),ResultStatus.OK.getMessage(),userDto);
    }
    
    @PostMapping("/editpwd")
    @ApiOperation(value = "修改密码",notes="修改密码")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="user_account",value="登录账号",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="old_password",value="原密码（密文）",required=true, dataType="string",paramType="form"),
    	@ApiImplicitParam(name="new_password",value="新密码（密文）",required=true, dataType="string",paramType="form")
    })
    public ResultEntity<SysUserEntity> editPwd(@RequestParam("user_account")String account,@RequestParam("old_password")String oldPwd,@RequestParam("new_password")String newPwd){
    	//解密 
    	String strOld="";
    	strOld = MD5Util.decode(oldPwd, AppConstants.PWD_KEY);
    	String strNew="";
    	strNew = MD5Util.decode(newPwd, AppConstants.PWD_KEY);
    	
    	if(strNew.length() > 6) {
    		return new ResultEntity<SysUserEntity>(ResultStatus.PWD_TOO_LONG.getCode(),ResultStatus.PWD_TOO_LONG.getMessage(),null);
    	}
    	//加密
    	String base64OldPwd = Base64Utils.encryptBase64(strOld,Base64Utils.encryptKey);
    	String base64NewPwd = Base64Utils.encryptBase64(strNew,Base64Utils.encryptKey);
    	
        SysUserEntity sysUser= userReposity.findByAccountAndPwdAndWkno(account, base64OldPwd, AppConstants.WORKER_NO_STORAGE_MGR);//库管登录
        if(sysUser == null) {
        	return new ResultEntity<SysUserEntity>(ResultStatus.OLD_PWD_WRONG.getCode(),ResultStatus.OLD_PWD_WRONG.getMessage(),null);
        }
        
        SysUserEntity sue = new SysUserEntity();
        sue.setUser_id(sysUser.getUser_id());
        sue.setPwd(base64NewPwd);
        sue.setAccount(sysUser.getAccount());
        sue.setUser_name(sysUser.getUser_name());
        sue.setUpt_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        sue.setWkno(sysUser.getWkno());
        sue.setOrg_id(sysUser.getOrg_id());
        sue.setCert_no(sysUser.getCert_no());
        
        SysUserEntity userRet = userReposity.saveAndFlush(sue);
        
        return new ResultEntity<SysUserEntity>(ResultStatus.OK.getCode(),ResultStatus.OK.getMessage(), userRet);
    }
    
    @GetMapping("")
    @ApiOperation(value = "获取所有用户信息",notes="获取所有用户信息列表")
    public ResultEntity<List<SysUserEntity>> getAllUser(){
        List<SysUserEntity> myUsers = userReposity.findAll();
        return new ResultEntity<List<SysUserEntity>>(ResultStatus.OK.getCode(),ResultStatus.OK.getMessage(),myUsers);
    }

    /*@GetMapping("/all")
    @ApiOperation(value = "获取所有用户信息",notes="获取所有用户信息列表")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="page",value="第几页",required=true,dataType="int",paramType="query"),
    	@ApiImplicitParam(name="size",value="每页显示几条",required=true,dataType="int",paramType="query")
    })
    public ResultEntity<Page<UserInfo>> getAllUsers(@RequestParam int page,
    												@RequestParam int size){
        Page<UserInfo> myUsers = userReposity.findAll(new PageRequest(page,size));
        return new ResultEntity<Page<UserInfo>>(ResultStatus.OK.getCode(),ResultStatus.OK.getMessage(),myUsers);
    }*/
    
      
}
