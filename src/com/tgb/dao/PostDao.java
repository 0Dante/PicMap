package com.tgb.dao;

import java.util.ArrayList;

import com.tgb.entity.Label;
import com.tgb.entity.Post;

public interface PostDao {
	
	public String addPost(Post post);
	
	public ArrayList<Post> getPostByTitle(String title);

	public ArrayList<Post> getPostList(int index);
	
	public ArrayList<Post> getCommentList(int index, String superid);
	
	public Post getPostByImgUrl(String url);
	
	public Post getPostById(String id);
	
	public void setPostSubTag(String id);
	
	public void setGrade(String id, float grade);
	
	public ArrayList<Post> getPostByUserId(String id);

	public ArrayList<Post> getAroundPosts(Double lat, Double lon);
	
	public void addLabel(Label label);
	
	public void addmLabel(String id, String label);
}
