package com.tgb.entity;

import java.io.Serializable;

//һ��token�࣬�������ظ�ǰ��token����json�ĸ�ʽ������{"token":"121232132"}
public class Token implements Serializable{
	private String token;

	public Token(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
