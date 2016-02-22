package com.github.chanming2015.domain.aop;

import com.alibaba.fastjson.JSON;

/**
 * Project:domain
 * Package:com.github.chanming2015.domain.aop
 * FileName:SimpleLogFormater.java
 * Comments:
 * JDK Version:
 * Author XuMaoSen
 * Create Date:2015年12月4日 下午7:32:19
 * Description: 日志格式化输出工具
 * Version:1.0.0
 */
public class SimpleLogFormater {

	/**
	 * 将方法调用的参数格式化输出
	 * @param params
	 * @return
	 */
	public static String formatParams(Object... params) {
		int paramsLength = params.length;

		StringBuffer formated = new StringBuffer("Parameters :");
		if (paramsLength == 0) {
			formated.append(" NO PARAM!");
		} else {
			for (int i = 0; i < paramsLength; i++) {
				formated.append(" PARAM");
				formated.append((i + 1));
				formated.append(":");
				formated.append(JSON.toJSON(params[i]));
				formated.append(";");
			}
		}
		return formated.toString();
	}

	/**
	 * 将方法调用的返回结果格式化输出
	 * @param result
	 * @return
	 */
	public static String formatResult(Object result){
		return "Result : " + JSON.toJSON(result) + ".";
	}
	
	/**
	 * 将捕捉到的异常格式化输出
	 * @param result
	 * @return
	 */
	public static String formatException(String bizErrorInfo, Exception e){
		StringBuffer formated = new StringBuffer("Error : ");
		formated.append(bizErrorInfo);
		formated.append("\nException message : ");
		formated.append(e.getMessage());
		formated.append("\nException trace : \n");
		for (StackTraceElement elment : e.getStackTrace()) {
			formated.append(elment.toString());
			formated.append("\n");
		}
		return formated.toString();
	}
}