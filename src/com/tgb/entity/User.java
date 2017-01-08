package com.tgb.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_USER")
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(length=32)
	private String mUserId;
	
	@Column(length=32)
	private String mPhone;
	
	@Column(length=32)
	private String mPassWord;
	
	@Column(length=32)
	private String mUserName;
	
	@Column(length=32)
	private String mEmail;
	
	@Column(length=32)
	private String mMessage;
	
	private String mFansNum;  //粉丝数量，新增
	
	private String mPostNum;  //发帖数量，新增

	private String mMotto;  //自我介绍，新增

	private String mHeadImageUrl;  //头像url
		
	
	public String getmFansNum() {
		return mFansNum;
	}

	public void setmFansNum(String mFansNum) {
		this.mFansNum = mFansNum;
	}

	public String getmPostNum() {
		return mPostNum;
	}

	public void setmPostNum(String mPostNum) {
		this.mPostNum = mPostNum;
	}

	public String getmMotto() {
		return mMotto;
	}

	public void setmMotto(String mMotto) {
		this.mMotto = mMotto;
	}

	public String getmUserId() {
		return mUserId;
	}

	public void setmUserId(String mUserId) {
		this.mUserId = mUserId;
	}

	public String getmPhone() {
		return mPhone;
	}

	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}

	public String getmPassWord() {
		return mPassWord;
	}

	public void setmPassWord(String mPassWord) {
		this.mPassWord = mPassWord;
	}

	public String getmUserName() {
		return mUserName;
	}

	public void setmUserName(String mUserName) {
		this.mUserName = mUserName;
	}

	public String getmEmail() {
		return mEmail;
	}


	public void setmEmail(String mEmail) {
		this.mEmail = mEmail;
	}


	public String getmMessage() {
		return mMessage;
	}


	public void setmMessage(String mMessage) {
		this.mMessage = mMessage;
	}



	public String getmHeadImageUrl() {
		return mHeadImageUrl;
	}

	public void setmHeadImageUrl(String mHeadImageUrl) {
		this.mHeadImageUrl = mHeadImageUrl;
	}

	public User(){}
}
