package com.tgb.dao;

import java.util.ArrayList;
import java.util.HashSet;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.tgb.entity.Label;
import com.tgb.entity.Post;
import com.tgb.entity.User;

public class PostDaoImpl implements PostDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Override
	public String addPost(Post post) {
		
		try{
			//System.out.println(post.getmCompleteLabel().size());
			sessionFactory.getCurrentSession().save(post);
			
			return "success";
		}catch(Exception e){
			return "false";
		}
	}
	
	@Override
	public ArrayList<Post> getPostByTitle(String title) {
		//Ӧ�����������ӣ�����levelΪ0
		String hql = "from Post p where p.mTitle like ? or p.mLabel like ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, "%"+title+"%");
		query.setString(1, "%"+title+"%");
		
		ArrayList<Post> result=(ArrayList<Post>) query.list();   
		
		return  result;
	}
	
	@Override
	public ArrayList<Post> getPostList(int index) {
		//ע�������ӣ�����levelΪ0
		String hql = "from Post p where p.mLevel=0 order by p.mReleaseTime desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		//��ҳ���ܣ�index��ǰ̨���صģ���ʾ���������ĵڼ�����ʼ
		//max��ʾ���ؼ�������
		query.setFirstResult(index);
		query.setMaxResults(15);
		
		ArrayList<Post> result=(ArrayList<Post>) query.list();   
		return  result;
	}
	
	@Override
	public ArrayList<Post> getCommentList(int index, String superid) {
		String hql = "from Post p where p.mSuperId=? order by p.mReleaseTime desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, superid);
		
		//��ҳ���ܣ�index��ǰ̨���صģ���ʾ���������ĵڼ�����ʼ
		//max��ʾ���ؼ�������
		query.setFirstResult(index);
		query.setMaxResults(15);
		
		ArrayList<Post> result=(ArrayList<Post>) query.list();   
		return  result;
	}
	
	@Override
	public Post getPostByImgUrl(String url) {
		String hql = "from Post p where p.mImageUrl=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, url);
		//һ��urlֻ�ܶ�Ӧһ������
		return (Post)query.uniqueResult();
		
	}

	@Override
	public Post getPostById(String id) {
		String hql = "from Post p where p.mPostId=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (Post)query.uniqueResult();
	}

	@Override
	public void setPostSubTag(String id) {
		String hql = "update Post p set p.subtag = '1' where p.mPostId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		query.executeUpdate();
	}

	@Override
	public ArrayList<Post> getPostByUserId(String id) {
		String hql = "from Post p where p.mUserId=? and p.mLevel=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		query.setInteger(1, 0);         //��ѯ�ı�����mLevelΪ0Ҳ�������Ӷ�������
		ArrayList<Post> result=(ArrayList<Post>) query.list();
		return  result;
	}
	
	@Override
	public ArrayList<Post> getAroundPosts(Double lat, Double lon) {
		String hql = "from Post t where t.mLevel = 0 and t.mLat is not null order by (POWER(? - t.mLat,2) + POWER(? - t.mLon,2))";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setDouble(0, lat);
		query.setDouble(1, lon);
		
		query.setFirstResult(0);
		query.setMaxResults(5);
		
		ArrayList<Post> result=(ArrayList<Post>) query.list();   
		
		return  result;
	}

	@Override
	public void setGrade(String id, float grade) {
		String hql = "update Post p set p.mRatingGrade =?, p.gradenum =p.gradenum+1 where p.mPostId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setFloat(0, grade);
		query.setString(1, id);
		query.executeUpdate();
		
	}

	@Override
	public void addLabel(Label label) {
		sessionFactory.getCurrentSession().save(label);
	}

	@Override
	public void addmLabel(String id ,String label) {
		String hql = "update Post p set p.mLabel =? where p.mPostId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, label);
		query.setString(1, id);
		query.executeUpdate();
		
	}
	
}
