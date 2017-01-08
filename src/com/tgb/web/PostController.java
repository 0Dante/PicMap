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


//����������
@Controller
public class PostController {
	
	@Resource(name="postManager")
	private PostManager postManager;
	
	@Resource(name="userManager")
	private UserManager userManager;
	
	//������ӵķ���
	@RequestMapping(value="/post/pushtask",method=RequestMethod.POST)
	public @ResponseBody JSONObject post(@RequestBody String json)throws  IOException {
		//ֱ����@RequestBody��String�õ�json
		JSONObject jsonObj = JSONObject.fromObject(json);
		//json�����Զ�ת��Ϊpost������ͬ���Ե�ֱ�����ã�û�е�������Ϊnull
		Post p = new Post();
		//���÷����ѱ�ǩ�ַ����������mLabel�ַ������ö��ŷָ�
		p = p.JsonToStringlabel(jsonObj);
		//���ӵ����۵ȼ�Ϊ0
		p.setmLevel(0);
		p.setGradenum(0);
		p.setmReplyerId(null);    //���ӵ���һ��Ϊnull
		p.setmSuperId(null);
		p.setmRatingGrade((float)0.0);
		p.setSubtag("0");
		String rt = postManager.addPost(p);
		//�ж��Ƿ�������ӽ����ݿ�ɹ��������ظ�ǰ̨
		if(rt != null && !rt.equals("false") && !rt.isEmpty()){
			p.setmMessage("success");
			return JSONObject.fromObject(p.ToJson());
		}else{
			p.setmMessage("empty");
			return JSONObject.fromObject(p.ToJson());
		}
	}
	
	//�õ����շ���ʱ�����������
	//ÿ�η���15������׿������ˢ�¾����·��غ����15������Ȼ��׿����Ҫ����һ��index
	@RequestMapping(value="/post/getlist",method=RequestMethod.GET)
	public @ResponseBody JSONObject getPostList(HttpServletRequest request) throws IOException {
		//�õ�int��index
		int index = Integer.valueOf(request.getParameter("index"));
		ArrayList<Post> postlist = postManager.getPostList(index);
		if(postlist.isEmpty()) {
			JSONObject json_f = new JSONObject();
			json_f.put("mMessage", "empty");
			json_f.put("mPosts", new JSONArray());
			return json_f;
		}
		JSONArray array = new Post().ToJson_list(postlist);
		
		//��ÿ��post����û�����ͷ��
		for(int i=0; i<array.size(); i++) {
			JSONObject json_tmp = array.getJSONObject(i);
			String id = json_tmp.getString("mUserId");
			User u = userManager.getUserById(id);
			if(u!=null) {
				//����������Ҫ�����û���
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
	
	//�õ����շ���ʱ�����������
	//ÿ�η���15������׿������ˢ�¾����·��غ����15������Ȼ��׿����Ҫ����һ��index
	@RequestMapping(value="/post/getcomments",method=RequestMethod.GET)
	public @ResponseBody JSONObject getCommentList(HttpServletRequest request) throws IOException {
		//�õ�int��index
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
		
		//��ÿ��post����û�����ͷ��
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
	
	//�������֣������ӵ�����ȡ�����Ҽ����µ����֣�����gradenumҲ���ǲ������ֵ������������ֵ�ƽ��ֵ
	@RequestMapping(value="/post/pushgrade",method=RequestMethod.POST)
	public @ResponseBody JSONObject pushGrade(HttpServletRequest request) {
		String id = request.getParameter("mPostId");
		float grade_get = Float.parseFloat(request.getParameter("mGrade"));
		Post p = postManager.getPostById(id);
		//�õ�ԭ��������
	    float grade_before = p.getmRatingGrade();
	    //֮ǰ�������۵�����
	    int num = p.getGradenum();
	    float grade_after_tmp = (num*grade_before+grade_get)/(num+1);
	    float grade_after;
	    //С������
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
	
	//�õ��������ӵ��������
	@RequestMapping(value="/get/getdetial",method=RequestMethod.GET)
	public @ResponseBody JSONObject getDetailPost(HttpServletRequest request) {
		//�õ�ǰ̨������ͼƬurl��ÿ��Url��ӦΨһ��post
		String id = request.getParameter("mPostId");
		Post p = null;
		p = postManager.getPostById(id);
		if(p!=null) {
			JSONObject json = p.ToJson();
			User u = userManager.getUserById(p.getmUserId());

			//����������Ҫ�����û���
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
	
	
	//Ϊ�������һ����ǩ
	@RequestMapping(value="/post/addlabel",method=RequestMethod.POST)
	public @ResponseBody JSONObject addLabelToPost(HttpServletRequest request) {
		//�õ���Ҫ��ӱ�ǩ�����ӣ��Լ����ӵ�����
		String id = request.getParameter("mPostId");
		String content = request.getParameter("mContent");
		Post p = postManager.getPostById(id);
		String p_mLabel = p.getmLabel();
		p_mLabel = ","+p_mLabel+",";     //Ϊ�˷�����������϶���
		//����Ƿ����Ӧ�ü��϶��ţ���Ȼ������ǩ���õġ��Ͳ�����ӡ��á������ǲ��Ե�
		if(p.getmLabel().contains(","+content+",")) {
			JSONObject json_fail = new JSONObject();
			//��ǩ�ظ������
			json_fail.put("mMessage", "failure");
			return json_fail;
		}
		Label label = new Label(content);
		label.setmPostId(id);
		postManager.addLabel(label);
		//��post��Ķ�ӦmLabelҲ���ϱ�ǩ
		postManager.addmLabel(id, p.getmLabel()+","+content);
		JSONObject json = new JSONObject();
		json.put("mMessage", "success");
		return json;
	}
	
	
	//Ϊ����ɾ��һ����ǩ
	
	
	//Ϊ�������һ�����ۣ�����Ϊһ���������һ������
	//���ȰѸ����ӻ������۵�id��level�ñ���������Ȼ�����ӵ����۵�json������
	@RequestMapping(value="/post/pushcomment",method=RequestMethod.POST)
	public @ResponseBody JSONObject addComment(HttpServletRequest request, @RequestBody String json) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		String id = jsonObject.getString("mSuperId");   //�����Ҫǰ��Ҳ��������id���ϼ�id
		Post post = postManager.getPostById(id);
		if(post==null) {
			JSONObject j = new JSONObject();
			j.put("mMessage", "empty");
			return j;
		}
		//�����ϼ����۵�subtag����Ϊ1��Ҳ������������
		postManager.setPostSubTag(id);
		//����û�б�ǩ
		Post comment = (Post)JSONObject.toBean(jsonObject,Post.class);
		comment.setSubtag("0");
		comment.setmReplyerId(post.getmUserId());
		postManager.addPost(comment);
		comment.setmMessage("success");
		JSONObject jsonObj = JSONObject.fromObject(comment);
		
		//�õ������˺ͱ������˵�����
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
	
	//���ձ����������ӣ���ʵҲ�����˱�ǩ
	@RequestMapping(value="/post/getSearchPostResult",method=RequestMethod.GET)
	public @ResponseBody JSONObject getPostByTitle(HttpServletRequest request) throws IOException {

		//�õ������ؼ��֣�����ֻʵ���˱���
		String title = request.getParameter("edit_post_search");
		
		System.out.println(title);
		ArrayList<Post> postlist = null;
		postlist = postManager.getPostByTitle(title);
		//�Ҳ���ǰ̨��mTitle�򷵻�null    %3:%D%4%WD%
		//֮ǰ�жϵ���postlistΪnull���ǲ��Ե�
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
	
	//���չؼ��������û����ؼ��־����û���
	@RequestMapping(value="/post/getSearchUserResult",method=RequestMethod.GET)
	public @ResponseBody JSONObject getUserByName(HttpServletRequest request) throws IOException {
		
		//�õ������ؼ��֣�����ֻʵ���˱���
		String name = request.getParameter("edit_user_search");
		//�Ҳ���ǰ̨��mTitle�򷵻�null
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
	
	//��ȡ���û��йص��������ӣ�ע�����ֻ��levelΪ0�ķ��أ���Ϊ���ܷ�������
	@RequestMapping(value="/post/getPostOfUser",method=RequestMethod.GET)
	public @ResponseBody JSONObject getPostOfUser(HttpServletRequest request) throws IOException {
	
		//�����û�id�ҵ����������ӣ�����������
		String id = request.getParameter("mUserId");
		ArrayList<Post> postlist = postManager.getPostByUserId(id);
		//�Ҳ���ǰ̨��mTitle�򷵻�null
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
	
	
	//��ȡ�͵�ǰ����λ��������������
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
