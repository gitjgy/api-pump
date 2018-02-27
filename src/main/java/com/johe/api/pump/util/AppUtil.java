package com.johe.api.pump.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

public class AppUtil {

    public static boolean isOK(String param,int length,boolean required) {
    	if(required && StringUtils.isBlank(param)) {// 空参
    		System.out.println("[参数]"+param+"非法-为空！");
    		return false;
    	}
    	if(required && StringUtils.isNotBlank(param) &&  StringUtils.trimToEmpty(param).length() > length) {// 超长
    		System.out.println("[参数]"+param+"非法-过长！"+param.length() + " > "+length);
    		return false;
    	}
    	System.out.println("[参数]"+param+"OK！");
    	return true;
    }
    
    public static boolean isValidDateForyyyyMMdd(String strDate) {
    	if(StringUtils.isBlank(strDate)) {
    		return false;
    	}
    	boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
           format.setLenient(false);
           format.parse(strDate);
        } catch (ParseException e) {
            convertSuccess=false;
        } 
        return convertSuccess;
    }
    
    public static boolean isValidDate(String strDate,String pattern) {
    	if(StringUtils.isBlank(strDate)) {
    		return false;
    	}
    	boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
           format.setLenient(false);
           format.parse(strDate);
        } catch (ParseException e) {
            convertSuccess=false;
        } 
        return convertSuccess;
    }
    
    /**
     * 字符串转换成为16进制(无需Unicode编码)
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     * @param hexStr
     * @return
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        } 
        return new String(bytes); 
    }
    
//    public static void main(String[] args) {
//    	System.out.println(AppUtil.str2HexStr("AJ0102001A0102"));
//    	System.out.println(AppUtil.hexStr2Str("414A303130323030314130313032"));
//    }
}
