package com.tgb.entity;

import java.io.Serializable;

//�����࣬������ǰ�˷��ش�����룬����{"token":1000}
public class LoginError implements Serializable{
	private int error;

	public LoginError(int error) {
		super();
		this.error = error;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}
	
}
