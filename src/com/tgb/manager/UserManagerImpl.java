package com.tgb.manager;

import java.util.ArrayList;
import java.util.List;

import com.tgb.dao.UserDao;
import com.tgb.entity.PassWord;
import com.tgb.entity.User;

public class UserManagerImpl implements UserManager {

	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User getUser(String id) {
		return userDao.getUser(id);
	}

	@Override
	public List<User> getAllUser() {
		return userDao.getAllUser();
	}

	@Override
	public void addUser(User user) {
		userDao.addUser(user);
	}

	@Override
	public boolean delUser(String id) {
		
		return userDao.delUser(id);
	}

	@Override
	public boolean updateUser(User user) {
		return userDao.updateUser(user);
	}

	@Override
	public User getUserByTel(String mPhone) {
		
		return userDao.getUserByTel(mPhone);
	}

	@Override
	public User getUserById(String id) {
		
		return userDao.getUserById(id);
	}

	@Override
	public ArrayList<User> getUserByName(String name) {
		
		return userDao.getUserByName(name);
	}

	@Override
	public String checkMail(String email) {
		return userDao.checkMail(email);
		
	}

	@Override
	public void addPassWord(PassWord p) {
		userDao.addPassWord(p);
		
	}

	@Override
	public Boolean getUserByPassWord(PassWord p) {
		
		return userDao.getUserByPassWord(p);
	}
	@Override
	public void updatePassWord(String userid, String ps){
		userDao.updatePassWord(userid,ps);
	}
	
}
