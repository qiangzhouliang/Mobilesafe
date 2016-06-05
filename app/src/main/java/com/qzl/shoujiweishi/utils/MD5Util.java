package com.qzl.shoujiweishi.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	//银行 银行卡 6位数字 将密码进行10-30次md5加密
	/*public static void main(String[] args) {
		String password="c26be8aaf53b15054896983b43eb6a65";
		StringBuilder sb = new StringBuilder();
		try {
			//1.获取数据摘要器
			//arg0 : 加密的方式
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			//2.将一个byte数组进行加密，返回的是一个加密过的byte数组，二进制的哈希计算，md5加密第一步
			byte[] digest = messageDigest.digest(password.getBytes());
			//3.遍历byte数组
			for (int i = 0; i < digest.length; i++) {
				//4.MD5加密
				//byte值    -128-127
				int result = digest[i] & 0xff;
				//将得到int类型转化成16进制字符串
				//String hexString = Integer.toHexString(result)+1;//不规则加密,加盐
				String hexString = Integer.toHexString(result);
				if (hexString.length() < 2) {
//					System.out.print("0");
					sb.append("0");
				}
				//System.out.println(hexString);
				//e10adc3949ba59abbe56e057f20f883e
				sb.append(hexString);
			}
			System.out.println(sb.toString());
		} catch (NoSuchAlgorithmException e) {
			//找不到加密方式的异常
			e.printStackTrace();
		}
	}*/
	/**
	 * MD5加密
	 * @return
	 */
	public static String passwordMD5(String password){
		StringBuilder sb = new StringBuilder();
		try {
			//1.获取数据摘要器
			//arg0 : 加密的方式
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			//2.将一个byte数组进行加密，返回的是一个加密过的byte数组，二进制的哈希计算，md5加密第一步
			byte[] digest = messageDigest.digest(password.getBytes());
			//3.遍历byte数组
			for (int i = 0; i < digest.length; i++) {
				//4.MD5加密
				//byte值    -128-127
				int result = digest[i] & 0xff;
				//将得到int类型转化成16进制字符串
				//String hexString = Integer.toHexString(result)+1;//不规则加密,加盐
				String hexString = Integer.toHexString(result);
				if (hexString.length() < 2) {
//					System.out.print("0");
					sb.append("0");
				}
				//System.out.println(hexString);
				//e10adc3949ba59abbe56e057f20f883e
				sb.append(hexString);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			//找不到加密方式的异常
			e.printStackTrace();
		}
		return null;
	}
}
