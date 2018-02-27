package com.johe.api.pump.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;


public class NumberUtils {
	
	/***  
	 *   
	 * getCommonBh:(获得通用的编号：前缀（一般是单位的前N位+当前时间（自定义格式）+格式化的sequence值：00001）). <br/>   
	 * @param prefix 编号的前缀  
	 * @param timeFormt 日期格式，在需求添加日期时候使用  
	 * @param sequenceName sequence名称  
	 * @param length sequence格式 %08d：8位不够的补0  
	 * @return 唯一的编号  
	 * @since JDK 1.6  
	 */    
	public String getCommonBh(String prefix, String timeFormt,String sequenceName, String length)    
	{    
		String datenumber ="";
		if(StringUtils.isNotBlank(timeFormt)) {
			datenumber = new java.text.SimpleDateFormat(timeFormt).format(new Date());
		}
	    if (StringUtils.isEmpty(length))    
	    {    
	        length = "8"; 
	    }    
	    String seqStr = getSequenceByName(sequenceName, length);    
	    return new StringBuilder(prefix).append(datenumber).append(seqStr).toString();    
	}    
	
	//时间+序列号
	private String getTimeSeq(String timeFormt,String sequenceName, String length)    
	{    
		String datenumber ="";
		if(StringUtils.isNotBlank(timeFormt)) {
			datenumber = new java.text.SimpleDateFormat(timeFormt).format(new Date());
		}
	    if (StringUtils.isEmpty(length))    
	    {    
	        length = "8"; 
	    }    
	    String seqStr = getSequenceByName(sequenceName, length);    
	    return new StringBuilder(datenumber).append(seqStr).toString();    
	}    
	    
	/**  
	 *   
	 * getSequenceByName:(获取一定格式的sequence值). <br/>  
	 * @param sequenceName sequence名称  
	 * @param length 需要的长度，格式例如：：8位不够的补0  
	 * @return String 格式化后的sequence值  
	 * @since JDK 1.6  
	 */    
	public String getSequenceByName(final String sequenceName, final String length)    
	{    
	    /*if (StringUtils.isBlank(sequenceName))    
	    {    
	        return "";    
	    }    
	    else    
	    {    
	        return (String)this.getHibernateTemplate().execute(new HibernateCallback()    
	        {    
	            public Object doInHibernate(Session session)    
	                throws SQLException    
	            {    
	                Query query = getSession().createSQLQuery(" SELECT " + sequenceName + ".nextval from dual");    
	                BigDecimal seq = (BigDecimal)query.uniqueResult();    
	                if (seq != null)    
	                {    
	                    return String.format("%0" + length + "d", seq.longValue());    
	                }    
	                else    
	                {    
	                    return "001";    
	                }    
	            }    
	        });    
	    }    */
		return null;
	}   
}
