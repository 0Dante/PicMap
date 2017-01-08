package com.tgb.dao;

import java.util.ArrayList;
import java.util.List;

import com.tgb.entity.PassWord;
import com.tgb.entity.User;

public interface UserDao {

	public User getUser(String id);
	
	public List<User> getAllUser();
	
	public void addUser(User user);
	
	public boolean delUser(String id);
	
	public boolean updateUser(User user);
	
	public User getUserByTel(String tel);
	
	public User getUserById(String id);
	
	public ArrayList<User> getUserByName(String name);
	
	public String checkMail(String email);

	public void addPassWord(PassWord p);

	public Boolean getUserByPassWord(PassWord p);

	public void updatePassWord(String userid, String ps);
	
}
