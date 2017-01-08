package com.tgb.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tgb.entity.Post;
import com.tgb.entity.Label;
import com.tgb.entity.User;
import com.tgb.manager.PostManager;
import com.tgb.manager.UserManager;


//帖子请求处理
@Controller
public class PostController {
	
	@Resource(name="postManager")
	private PostManager postManager;
	
	@Resource(name="userManager")
	private UserManager userManager;
	
	//添加帖子的方法
	@RequestMapping(value="/post/pushtask",method=RequestMethod.POST)
	public @ResponseBody JSONObject post(@RequestBody String json)throws  IOException {
		//直接用@RequestBody的String得到json
		JSONObject jsonObj = JSONObject.fromObject(json);
		//json对象自动转化为post对象，相同属性的直接引用，没有的则设置为null
		Post p = new Post();
		//调用方法把标签字符数组解析成mLabel字符串，用逗号分隔
		p = p.JsonToStringlabel(jsonObj);
		//帖子的评论等级为0
		p.setmLevel(0);
		p.setGradenum(0);
		p.setmReplyerId(null);    //帖子的上一级为null
		p.setmSuperId(null);
		p.setmRatingGrade((float)0.0);
		p.setSubtag("0");
		String rt = postManager.addPost(p);
		//判断是否添加帖子进数据库成功，并返回给前台
		if(rt != null && !rt.equals("false") && !rt.isEmpty()){
			p.setmMessage("success");
			return JSONObject.fromObject(p.ToJson());
		}else{
			p.setmMessage("empty");
			return JSONObject.fromObject(p.ToJson());
		}
	}
	
	//得到按照发布时间排序的帖子
	//每次返回15个，安卓端下拉刷新就重新返回后面的15个，当然安卓端需要传回一个index
	@RequestMapping(value="/post/getlist",method=RequestMethod.GET)
	public @ResponseBody JSONObject getPostList(HttpServletRequest request) throws IOException {
		//得到int的index
		int index = Integer.valueOf(request.getParameter("index"));
		ArrayList<Post> postlist = postManager.getPostList(index);
		if(postlist.isEmpty()) {
			JSONObject json_f = new JSONObject();
			json_f.put("mMessage", "empty");
			json_f.put("mPosts", new JSONArray());
			return json_f;
		}
		JSONArray array = new Post().ToJson_list(postlist);
		
		//给每个post添加用户名和头像
		for(int i=0; i<array.size(); i++) {
			JSONObject json_tmp = array.getJSONObject(i);
			String id = json_tmp.getString("mUserId");
			User u = userManager.getUserById(id);
			if(u!=null) {
				//帖子详情需要返回用户名
				json_tmp.put("mUserName", u.getmUserName());
				json_tmp.put("mHeadImageUrl", u.getmHeadImageUrl());
			}
		}
		
		JSONObject json_s = new JSONObject();
		json_s.put("mMessage", "success");
		json_s.put("index", index+15);
		json_s.put("mPosts", array);
		return json_s;

	}
	
	//得到按照发布时间排序的评论
	//每次返回15个，安卓端下拉刷新就重新返回后面的15个，当然安卓端需要传回一个index
	@RequestMapping(value="/post/getcomments",method=RequestMethod.GET)
	public @ResponseBody JSONObject getCommentList(HttpServletRequest request) throws IOException {
		//得到int的index
		int index = Integer.valueOf(request.getParameter("index"));
		String superid = request.getParameter("mPostId");
		ArrayList<Post> postlist = postManager.getCommentList(index, superid);
		if(postlist.isEmpty()) {
			JSONObject json_f = new JSONObject();
			json_f.put("mMessage", "empty");
			json_f.put("mPosts", new JSONArray());
			return json_f;
		}
		JSONArray array = new Post().ToJson_list(postlist);
		
		//给每个post添加用户名和头像
		for(int i=0; i<array.size(); i++) {
			JSONObject json_tmp = array.getJSONObject(i);
			String id_user = json_tmp.getString("mUserId");
			String id_replyer = json_tmp.getString("mReplyerId");
			User u_user = userManager.getUserById(id_user);
			User u_replyer = userManager.getUserById(id_replyer);
			if(u_user!=null) {
				json_tmp.put("mUserName", u_user.getmUserName());
				json_tmp.put("mHeadImageUrl", u_user.getmHeadImageUrl());
			}
			if(u_replyer!=null) {
				json_tmp.put("mReplyerName", u_replyer.getmUserName());
			}
		}
		
		JSONObject json_s = new JSONObject();
		json_s.put("mMessage", "success");
		json_s.put("index", index+15);
		json_s.put("mPosts", array);
		System.out.println(json_s.toString());
		return json_s;

	}
	
	//处理评分，将帖子的评分取出并且加上新的评分，根据gradenum也就是参与评分的人数计算评分的平均值
	@RequestMapping(value="/post/pushgrade",method=RequestMethod.POST)
	public @ResponseBody JSONObject pushGrade(HttpServletRequest request) {
		String id = request.getParameter("mPostId");
		float grade_get = Float.parseFloat(request.getParameter("mGrade"));
		Post p = postManager.getPostById(id);
		//得到原帖的评分
	    float grade_before = p.getmRatingGrade();
	    //之前参与评论的人数
	    int num = p.getGradenum();
	    float grade_after_tmp = (num*grade_before+grade_get)/(num+1);
	    float grade_after;
	    //小数部分
	    float grade_little = (float) (grade_after_tmp-Math.floor(grade_after_tmp));
	    if(grade_little<0.25) {
	    	grade_after = (float) Math.floor(grade_after_tmp);
	    }
	    else if(grade_little>=0.25 && grade_little<=0.75) {
	    	grade_after = (float) (Math.floor(grade_after_tmp) + 0.5);
	    }
	    else {
	    	grade_after = (float) (Math.floor(grade_after_tmp) + 1.0);
	    }
	    postManager.setGrade(id, grade_after);
	    JSONObject json = new JSONObject();
	    json.put("mMessage", "success");
	    return JSONObject.fromObject(json);
	}
	
	//得到单个帖子的详情界面
	@RequestMapping(value="/get/getdetial",method=RequestMethod.GET)
	public @ResponseBody JSONObject getDetailPost(HttpServletRequest request) {
		//得到前台发来的图片url，每个Url对应唯一的post
		String id = request.getParameter("mPostId");
		Post p = null;
		p = postManager.getPostById(id);
		if(p!=null) {
			JSONObject json = p.ToJson();
			User u = userManager.getUserById(p.getmUserId());

			//帖子详情需要返回用户名
			if(u!=null) {
				json.put("mUserName", u.getmUserName());
				json.put("mHeadImageUrl", u.getmHeadImageUrl());
			}
			
			return json;
		}
		else {
			JSONObject json = new JSONObject();
			json.put("mMessage", "empty");
			return json;
		}
	}
	
	
	//为帖子添加一个标签
	@RequestMapping(value="/post/addlabel",method=RequestMethod.POST)
	public @ResponseBody JSONObject addLabelToPost(HttpServletRequest request) {
		//得到需要添加标签的帖子，以及帖子的内容
		String id = request.getParameter("mPostId");
		String content = request.getParameter("mContent");
		Post p = postManager.getPostById(id);
		String p_mLabel = p.getmLabel();
		p_mLabel = ","+p_mLabel+",";     //为了方便检索，加上逗号
		//检查是否包含应该加上逗号，不然包含标签“好的”就不能添加“好”，这是不对的
		if(p.getmLabel().contains(","+content+",")) {
			JSONObject json_fail = new JSONObject();
			//标签重复则不添加
			json_fail.put("mMessage", "failure");
			return json_fail;
		}
		Label label = new Label(content);
		label.setmPostId(id);
		postManager.addLabel(label);
		//给post类的对应mLabel也加上标签
		postManager.addmLabel(id, p.getmLabel()+","+content);
		JSONObject json = new JSONObject();
		json.put("mMessage", "success");
		return json;
	}
	
	
	//为帖子删除一个标签
	
	
	//为帖子添加一个评论，或者为一个评论添加一个评论
	//首先把该帖子或者评论的id和level用表单发过来，然后把添加的评论的json发送来
	@RequestMapping(value="/post/pushcomment",method=RequestMethod.POST)
	public @ResponseBody JSONObject addComment(HttpServletRequest request, @RequestBody String json) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		String id = jsonObject.getString("mSuperId");   //这个需要前端也存在帖子id，上级id
		Post post = postManager.getPostById(id);
		if(post==null) {
			JSONObject j = new JSONObject();
			j.put("mMessage", "empty");
			return j;
		}
		//将该上级评论的subtag设置为1，也就是有子评论
		postManager.setPostSubTag(id);
		//评论没有标签
		Post comment = (Post)JSONObject.toBean(jsonObject,Post.class);
		comment.setSubtag("0");
		comment.setmReplyerId(post.getmUserId());
		postManager.addPost(comment);
		comment.setmMessage("success");
		JSONObject jsonObj = JSONObject.fromObject(comment);
		
		//得到评论人和被评论人的名字
		User replyer = userManager.getUserById(comment.getmReplyerId());
		String replyer_name = null;
		if(replyer!=null) {
			replyer_name = replyer.getmUserName();
		}
		User user = userManager.getUserById(comment.getmUserId());
		String user_name = null;
		if(user!=null) {
			user_name = user.getmUserName();
		}
		
		jsonObj.put("mReplyerName", replyer_name);
		jsonObj.put("mUserName", user_name);
		return jsonObj;
	}
	
	//按照标题搜索帖子，其实也搜索了标签
	@RequestMapping(value="/post/getSearchPostResult",method=RequestMethod.GET)
	public @ResponseBody JSONObject getPostByTitle(HttpServletRequest request) throws IOException {

		//得到搜索关键字，这里只实现了标题
		String title = request.getParameter("edit_post_search");
		
		System.out.println(title);
		ArrayList<Post> postlist = null;
		postlist = postManager.getPostByTitle(title);
		//找不到前台的mTitle则返回null    %3:%D%4%WD%
		//之前判断的是postlist为null，是不对的
		if(postlist.isEmpty() || title==null || "".equals(title)) {
			JSONObject json_f = new JSONObject();
			json_f.put("mMessage", "empty");
			json_f.put("mPosts", new JSONArray());
			return json_f;
		}else{		
			JSONArray array = new Post().ToJson_list(postlist);
			JSONObject json_s = new JSONObject();
			json_s.put("mMessage", "success");
			json_s.put("mPosts", array);
			return json_s;	
		}	
	}
	
	//按照关键字搜索用户，关键字就是用户名
	@RequestMapping(value="/post/getSearchUserResult",method=RequestMethod.GET)
	public @ResponseBody JSONObject getUserByName(HttpServletRequest request) throws IOException {
		
		//得到搜索关键字，这里只实现了标题
		String name = request.getParameter("edit_user_search");
		//找不到前台的mTitle则返回null
		ArrayList<User> userlist = userManager.getUserByName(name);
		if(name==null || "".equals(name) || userlist.isEmpty()) {
			JSONObject json_f = new JSONObject();
			json_f.put("mMessage", "empty");
			json_f.put("mUsers", new JSONArray());
			return json_f;
		}else{
			JSONArray array = JSONArray.fromObject(userlist);
			for(int i=0; i<array.size(); i++) {
				((JSONObject)array.get(i)).remove("mPassWord");
			}
			JSONObject json_s = new JSONObject();
			json_s.put("mMessage", "success");
			json_s.put("mUsers", array);
			return json_s;
		}	
	}
	
	//获取和用户有关的所有帖子，注意必须只把level为0的返回，因为不能返回评论
	@RequestMapping(value="/post/getPostOfUser",method=RequestMethod.GET)
	public @ResponseBody JSONObject getPostOfUser(HttpServletRequest request) throws IOException {
	
		//根据用户id找到他发的帖子，不包括评论
		String id = request.getParameter("mUserId");
		ArrayList<Post> postlist = postManager.getPostByUserId(id);
		//找不到前台的mTitle则返回null
		if(id==null || "".equals(id) || postlist.isEmpty()) {
			JSONObject json_f = new JSONObject();
			json_f.put("mMessage", "empty");
			json_f.put("mPosts", new JSONArray());
			return json_f;
		}else{
			JSONArray array = new Post().ToJson_list(postlist);
			JSONObject json_s = new JSONObject();
			json_s.put("mMessage", "success");
			json_s.put("mPosts", array);
			return json_s;	
		}	
	}
	
	
	//获取和当前地理位置最近的五个帖子
	@RequestMapping(value="/map/aroundPosts",method=RequestMethod.POST)
	public @ResponseBody JSONObject getAroundPosts(HttpServletRequest request) throws IOException {
		System.out.println(request.getParameter("mLat"));
		Double lat = Double.parseDouble(request.getParameter("mLat"));
		Double lon = Double.parseDouble(request.getParameter("mLon"));
		
		ArrayList<Post> postlist = postManager.getAroundPosts(lat,lon);
		
		JSONArray array = new Post().ToJson_list(postlist);
		JSONObject json_s = new JSONObject();
		json_s.put("mMessage", "success");
		json_s.put("mPosts", array);
		return json_s;	
		
	}
	
}
