package com.foryuum.frontend.common.vo;

import java.io.Serializable;

public class UserInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String userName;

	public UserInfoVo(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}