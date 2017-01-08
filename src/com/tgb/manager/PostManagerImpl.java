package com.tgb.manager;

import java.util.ArrayList;

import com.tgb.dao.PicDao;
import com.tgb.dao.PostDao;
import com.tgb.entity.Label;
import com.tgb.entity.Picture;
import com.tgb.entity.Post;

public class PostManagerImpl implements PostManager{

	
	private PostDao postDao;
	
	
	public PostDao getPostDao() {
		return postDao;
	}


	public void setPostDao(PostDao postDao) {
		this.postDao = postDao;
	}


	@Override
	public String addPost(Post post) {
		return postDao.addPost(post);
	}
	
	@Override
	public ArrayList<Post> getPostByTitle(String title){
		return postDao.getPostByTitle(title);
	}
	
	@Override
	public ArrayList<Post> getPostList(int index){
		return postDao.getPostList(index);
	}


	@Override
	public Post getPostByImgUrl(String url) {
		return postDao.getPostByImgUrl(url);
	}


	@Override
	public Post getPostById(String id) {
		return postDao.getPostById(id);
	}


	@Override
	public void setPostSubTag(String id) {
		postDao.setPostSubTag(id);
	}


	@Override
	public ArrayList<Post> getPostByUserId(String id) {
		
		return postDao.getPostByUserId(id);
	}


	@Override
	public ArrayList<Post> getAroundPosts(Double lat, Double lon) {

		return postDao.getAroundPosts(lat, lon);
	}


	@Override
	public ArrayList<Post> getCommentList(int index, String superid) {
		
		return postDao.getCommentList(index, superid);
	}


	@Override
	public void setGrade(String id, float grade) {
		
		postDao.setGrade(id, grade);
	}


	@Override
	public void addLabel(Label label) {
		postDao.addLabel(label);
		
	}


	@Override
	public void addmLabel(String id, String label) {
		
		postDao.addmLabel(id, label);
	}

}
