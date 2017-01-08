package com.tgb.dao;

import org.hibernate.SessionFactory;

import com.tgb.entity.Picture;


public class PicDaoImpl implements PicDao{
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public  void savePic(Picture p){
		
		sessionFactory.getCurrentSession().save(p);
		
	}
}
