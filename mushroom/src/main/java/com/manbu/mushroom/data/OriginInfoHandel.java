package com.manbu.mushroom.data;

import com.manbu.mushroom.bean.OriginInfo;

public class OriginInfoHandel {

//	public String[] spiltString(String str) {
//		String[] temp = null;
//		temp = str.split("\\^");
//
//		return temp;
//	}

	/**
	 * 把str 转换为 OriginInfo
	 * @param str
	 * @return
     */
	public static OriginInfo getResult(String str) {

		OriginInfo originInfo = new OriginInfo();
		String [] temp = str.split("\\^");

		originInfo.setName(temp[0]);
		originInfo.setAddress(temp[1]);
		originInfo.setLife(temp[2]);
		originInfo.setSpec(temp[3]);
		originInfo.setMoreInfo(temp[4]);
		
		return originInfo;
	}
}
