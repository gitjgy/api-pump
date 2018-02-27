package com.johe.api.pump.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johe.api.pump.entity.InOrderItemEntity;
import com.johe.api.pump.entity.MessageEntity;
import com.johe.api.pump.entity.result.ResultEntity;
import com.johe.api.pump.entity.result.ResultStatus;
import com.johe.api.pump.repository.MessageRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="消息管理",tags="【消息管理】接口")
@RequestMapping("/api/v1/message")
@RestController
public class MessageAPI {
	
	@Autowired
	MessageRepository msgReps;
	
    
    @GetMapping("/{page}/{size}")
    @ApiOperation(value = "获取库管审核消息列表",notes="获取库管审核消息列表")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="page",value="页码（从0开始）",required=true, dataType="int",paramType="query"),
    	@ApiImplicitParam(name="size",value="每页显示条数",required=true, dataType="int",paramType="query")
    	})    
    public ResultEntity<Page<MessageEntity>> getMessages( 
    		@PathVariable("page")int page, 
    		@PathVariable("size")int size){
    	Page<MessageEntity>  illegals = msgReps.findAll(new Specification<MessageEntity>() {

			@Override
			public Predicate toPredicate(Root<MessageEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				
				list.add(cb.equal(root.<String>get("audit_post"), "03"));// 审核职位：03库管
				
				query.orderBy(cb.desc(root.get("send_time")));				
				Predicate[] pres = new Predicate[list.size()];
				pres = list.toArray(pres);
				
				return cb.and(pres);
				
			}
    		
    	}, new PageRequest(page, size, null));
    	
        return new ResultEntity<Page<MessageEntity>>(ResultStatus.OK.getCode(),
        											ResultStatus.OK.getMessage(),
        											illegals);
    }
    
    
    // 获取消息详情
 	@GetMapping("/detail/{msg_id}")
 	@ApiOperation(value = "获取消息详情", notes = "获取消息详情")
 	@ApiImplicitParams({
 			@ApiImplicitParam(name = "msg_id", value = "消息ID", required = true, dataType = "long", paramType = "query") })
 	public ResultEntity<MessageEntity> getMessageById(@PathVariable("msg_id") Long msgId) {
 		return new ResultEntity<MessageEntity>(ResultStatus.OK.getCode(), ResultStatus.OK.getMessage(),
 				msgReps.findByMsgid(msgId));
 	}
    

    
}
