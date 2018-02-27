package com.johe.api.pump.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Base64Utils {
	static Base64 base64= new Base64();
	/**
	* 用Base64对加密好的byte数组进行编码，返回字符串
	* @param str 需要加密的字符串
	* @param sKey 加密密钥
	* @return    经过加密的字符串
	*/
	public static String encryptBase64(String str, String sKey) {
	   // 声明加密后的结果字符串变量
	   String result = str;
	   if (str != null && str.length() > 0 && sKey != null && sKey.length() >= 8) {
	    try { 
	     //调用DES 加密数组的 encrypt方法，返回加密后的byte数组;
	     byte[] encodeByte = encryptBasedDes(str.getBytes("UTF-8"),sKey);
	     // 调用base64的编码方法，返回加密后的字符串;
	     result = base64.encodeToString(encodeByte).trim();
	    } catch (Exception e) {
	     e.printStackTrace();
	    }
	   }
	   return result;
	}


/**
	* 先用DES算法对byte[]数组加密
	* @param byteSource 需要加密的数据
	* @param sKey    加密密钥
	* @return      经过加密的数据
	* @throws Exception
	*/
	private static byte[] encryptBasedDes(byte[] byteSource, String sKey)
	    throws Exception { 
	   try {
	    // 声明加密模式;
	    int mode = Cipher.ENCRYPT_MODE;
	    // 创建密码工厂对象;
	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	    // 把字符串格式的密钥转成字节数组;
	    byte[] keyData = sKey.getBytes();
	    // 以密钥数组为参数，创建密码规则
	    DESKeySpec keySpec = new DESKeySpec(keyData);
	    // 以密码规则为参数，用密码工厂生成密码
	    Key key = keyFactory.generateSecret(keySpec);
	    // 创建密码对象
	    Cipher cipher = Cipher.getInstance("DES");
	    // 以加密模式和密码为参数对密码对象 进行初始化
	    cipher.init(mode, key);
	    // 完成最终加密
	    byte[] result = cipher.doFinal(byteSource);  
	    // 返回加密后的数组
	    return result;
	   } catch (Exception e) {
	    throw e;
	   } 
	}


// 密钥
	public final static String encryptKey = "maiboparking789123";

//	public static void main(String[] args) {
//		System.out.println(Base64Utils.encryptBase64("www.johesoft.com", "029-88787437"));
		// 艾贝尔非法出库上传数据时的密钥
//		eZUqSllGSI7GfCgsnMRZrPINzGRM1nVf
//	}
}
