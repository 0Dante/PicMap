package com.tgb.entity;

import java.io.Serializable;

//一个token类，用来返回给前端token，以json的格式，比如{"token":"121232132"}
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
