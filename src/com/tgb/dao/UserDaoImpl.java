package com.tgb.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.tgb.entity.PassWord;
import com.tgb.entity.Post;
import com.tgb.entity.User;

public class UserDaoImpl implements UserDao {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public User getUser(String id) {
		
		String hql = "from User u where u.id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (User)query.uniqueResult();
	}

	@Override
	public List<User> getAllUser() {
		
		String hql = "from User";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		return query.list();
	}

	@Override
	public void addUser(User user) {
		sessionFactory.getCurrentSession().save(user);
	}

	@Override
	public boolean delUser(String id) {
		
		String hql = "delete User u where u.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (query.executeUpdate() > 0);
	}

	@Override
	public boolean updateUser(User user) {
		
		String hql = "update User u set u.userName = ?,u.passWord=? where u.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, user.getmPhone());
		query.setString(1, user.getmPassWord());
		query.setString(2, user.getmUserId());
		
		return (query.executeUpdate() > 0);
	}

	@Override
	public User getUserByTel(String username) {
		String hql = "from User u where u.mUserName=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, username);
		
		return (User)query.uniqueResult();
	}

	@Override
	public User getUserById(String id) {
		String hql = "from User u where u.mUserId=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (User)query.uniqueResult();
	}

	@Override
	public ArrayList<User> getUserByName(String name) {
		String hql = "from User u where u.mUserName like ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, "%"+name+"%");
		ArrayList<User> result=(ArrayList<User>) query.list(); 
		
		return result;
	}
	@Override
	public String checkMail(String email) {
		String hql = "from User u where u.mEmail = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, email);
		if(query.uniqueResult()==null) {
			return null;
		}
		else {
			return ((User)query.uniqueResult()).getmUserId();
		}
		
	}

	@Override
	public void addPassWord(PassWord p) {
		sessionFactory.getCurrentSession().save(p);
		
	}

	@Override
	public Boolean getUserByPassWord(PassWord p) {
		String hql = "from PassWord p where p.url = ? and p.userId = ? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, p.getUrl());
		query.setString(1, p.getUserId());
		if(query.list().size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public void updatePassWord(String userid, String ps){
		String hql = "update User u set u.mPassWord=? where u.mUserId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, ps);
		query.setString(1, userid);
		query.executeUpdate();
	
		String hql1 = "delete PassWord t where t.userId = ?";
		Query query1 = sessionFactory.getCurrentSession().createQuery(hql1);
		query1.setString(0, userid);
		query1.executeUpdate();
	}
	
	
	
}
