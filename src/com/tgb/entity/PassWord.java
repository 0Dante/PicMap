package com.tgb.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_PASSWORD")
public class PassWord  implements Serializable{

	private static final long serialVersionUID = -6173071286036965970L;
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name="passwordid",length=32)
	private String passwordid; 
	private String email;
	private String url;
	private String userId;
	
	public PassWord() {
		
	}
	public PassWord(String email, String userid) {
		this.userId = userid;
		this.email  = email;
		this.url  = "http://169.254.141.198:8080/PicMap/user/changepassword/" + userid;
	}
	public String getPasswordid() {
		return passwordid;
	}
	public void setPasswordid(String passwordid) {
		this.passwordid = passwordid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}