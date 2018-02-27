package com.johe.api.pump.util;

public class AppConstants {
											//   5年转换成秒
	public static final long EXPIRES_IN_SECOND = 5 * 365 * 24 * 60 * 60;//开发环境暂时设置30天有效期（单位：秒）
	
	// 密钥
	public static final String SIGN_KEY = "PUMP_STATION_SECRET";
	public static final String AUTH_HEADER = "Authorization";
	public static final String AUTH_BEARER = "Bearer ";
	
	// 特性
	public static final String MATERIAL_ATTR_ALL = "000";//全部
	public static final String MATERIAL_ATTR_CITY = "001";//市采
	public static final String MATERIAL_ATTR_NORMAL = "002";//普通
	public static final String MATERIAL_ATTR_IMPORTANT = "003";//重要
	public static final String MATERIAL_ATTR_KEYT = "004";//关键
	
	// 审核状态
	public static final String STOCK_IN_ORDER_AUDIT_STATUS_WAITING = "01";//待审核
	public static final String STOCK_IN_ORDER_AUDIT_STATUS_PASS = "02";//已通过
	public static final String STOCK_IN_ORDER_AUDIT_STATUS_NO_PASS = "03";//已驳回
	
	// 入库单状态
	public static final String STOCK_IN_ORDER_STATUS_WAITING = "01";//待审核
	
	public static final String PARENT_CATEGORY_ID = "-1";//大类父ID
	public static final int QUERY_ID_ALL = -1;//大类ID、小类ID、仓库ID等
	
	// 入库类型
	public static final String IN_ORDER_TYPE_BUY = "02";//采购入库
	public static final String IN_ORDER_TYPE_RETURN = "03";//归还
	public static final String IN_ORDER_TYPE_OTHER = "04";//其他
	public static final String IN_ORDER_TYPE_PRO = "05";//加工
	
	public static final String SYS_CODE_RES_TYPE = "JLDW";//计量单位
	
	public static final String PWD_KEY = "pump_sys_123456";//登录密码加解密密钥
	public static final String WORKER_NO_STORAGE_MGR = "03";//库管
	
	// 艾贝尔非法出库上传数据时的密钥（加密算法见com.johe.api.pump.util.Base64Utils）
    // eZUqSllGSI7GfCgsnMRZrPINzGRM1nVf
	public static final String ILLEGA_OUT_SECRET_KEY = "eZUqSllGSI7GfCgsnMRZrPINzGRM1nVf";//库管
	
	// 授时接口，时间格式
	public static final String PATTERN_ONE  = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_TWO  = "yyyy年MM月dd日 HH时mm分ss秒";
	public static final String PATTERN_THREE  = "yyyy-MM-dd HH:mm:ss.SSSS";
	public static final String PATTERN_FOUR  = "yy/MM/dd HH:mm";
	public static final String PATTERN_FIVE  = "yyyy年MM月dd日 HH时mm分ss秒 E ";
	public static final String PATTERN_SIX  = "yyyy年MM月dd日 HH时mm分ss秒SSSS毫秒";
	public static final String PATTERN_SEVEN  = "HH时mm分ss秒SSSS毫秒";
	
	// 时间格式枚举
	public static enum TimePattern {
		ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN
	}
	
}
