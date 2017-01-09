package com.manbu.mushroom.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser{
	private String name;
	private String IDcard;
	private String nickName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIDcard() {
		return IDcard;
	}

	public void setIDcard(String IDcard) {
		this.IDcard = IDcard;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


}
