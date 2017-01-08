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
@Table(name="T_POST")
public class Post  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6173071286036965970L;
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name="mPostId",length=32)
	private String mPostId;
	private int mLevel;   //0:帖子  1:1级评论，2：2级，3：3级
    private String mTitle;
    private String mContent;      //内容
    private String mImageID;
    private String mImageUrl;        //对应的图片路径
    private String mRecordUrl; //录音路径
    private String mReleaseTime;
    private float mRatingGrade;  //帖子评分
    private int gradenum;     //参与评分的人数
    private String mLocate; //地理位置
    private String subtag;   //标记是否有下级评论   0:没有，1：有
    private String mSuperId;   //上级评论，也就是该评论对应的对象，如果是帖子的话，super为null
    private String mUserId;  //发起人用户Id，即token
    private String mReplyerId;   //被回复者，也就是上一级的id
    private String mMessage;  //前端需要得到的成功标志
    private Double mLon;//纬度
    private Double mLat;//经度
    
    //这是前台上传一个post之后后台就新建对应的Label对象set
    @OneToMany(targetEntity=Label.class, cascade=CascadeType.ALL)
    @JoinColumn(name="mPostId", referencedColumnName="mPostId")
    private Set<Label> mCompleteLabel = new HashSet<Label>();
    //这个主要是在发送给前台json的时候用，因为发送给前台不是Label的set
    //转化为字符串，用逗号分隔，否则没法识别
    private String mLabel;
    
    
    
    
    public int getGradenum() {
		return gradenum;
	}

	public void setGradenum(int gradenum) {
		this.gradenum = gradenum;
	}

	public Set<Label> getmCompleteLabel() {
		return mCompleteLabel;
	}

	public void setmCompleteLabel(Set<Label> mCompleteLabel) {
		this.mCompleteLabel = mCompleteLabel;
	}
    
	public String getmPostId() {
		return mPostId;
	}

	public void setmPostId(String mPostId) {
		this.mPostId = mPostId;
	}

	public String getmReplyerId() {
		return mReplyerId;
	}

	public void setmReplyerId(String mReplyerId) {
		this.mReplyerId = mReplyerId;
	}

	public String getmMessage() {
		return mMessage;
	}

	public void setmMessage(String mMessage) {
		this.mMessage = mMessage;
	}

	public int getmLevel() {
		return mLevel;
	}

	public void setmLevel(int mLevel) {
		this.mLevel = mLevel;
	}


	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmContent() {
		return mContent;
	}

	public void setmContent(String mContent) {
		this.mContent = mContent;
	}

	public String getmImageID() {
		return mImageID;
	}

	public void setmImageID(String mImageID) {
		this.mImageID = mImageID;
	}

	public String getmImageUrl() {
		return mImageUrl;
	}

	public void setmImageUrl(String mImageUrl) {
		this.mImageUrl = mImageUrl;
	}

	public String getmRecordUrl() {
		return mRecordUrl;
	}

	public void setmRecordUrl(String mRecordUrl) {
		this.mRecordUrl = mRecordUrl;
	}

	public String getmReleaseTime() {
		return mReleaseTime;
	}

	public void setmReleaseTime(String mReleaseTime) {
		this.mReleaseTime = mReleaseTime;
	}

	public float getmRatingGrade() {
		return mRatingGrade;
	}

	public void setmRatingGrade(float mRatingGrade) {
		this.mRatingGrade = mRatingGrade;
	}

	public String getmLocate() {
		return mLocate;
	}

	public void setmLocate(String mLocate) {
		this.mLocate = mLocate;
	}

	public String getSubtag() {
		return subtag;
	}

	public void setSubtag(String subtag) {
		this.subtag = subtag;
	}

	

	public String getmSuperId() {
		return mSuperId;
	}

	public void setmSuperId(String mSuperId) {
		this.mSuperId = mSuperId;
	}

	public String getmUserId() {
		return mUserId;
	}

	public void setmUserId(String mUserId) {
		this.mUserId = mUserId;
	}

	public Double getmLon() {
		return mLon;
	}

	public void setmLon(Double mLon) {
		this.mLon = mLon;
	}

	public Double getmLat() {
		return mLat;
	}

	public void setmLat(Double mLat) {
		this.mLat = mLat;
	}

	public String getmLabel() {
		return mLabel;
	}

	public void setmLabel(String mLabel) {
		this.mLabel = mLabel;
	}

	public JSONArray ToJson_list(ArrayList<Post> postlist) {
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		for(int i=0; i<postlist.size(); i++) {
			postlist.get(i).setmMessage("success");
			String label = postlist.get(i).getmLabel();
			String[] tmplabel = null;
			JSONArray labeljson = null;
			if(label != null) {
				tmplabel = label.split(",");
				labeljson = JSONArray.fromObject(tmplabel);
				
			}
			json = JSONObject.fromObject(postlist.get(i));
			json.put("mLabel", labeljson);
			//添加一个JSONObject进JSONArray
			array.add(json);
		}
		return array;
	}
	
	
	
	//把标签字符串转化为数组，从而变成JSONArray放到json里
	public JSONObject ToJson() {
		
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		String label = this.mLabel;
		json = JSONObject.fromObject(this);
		this.setmMessage("success");
		String[] tmplabel = null;
		JSONArray labeljson = null;
		if(label != null) {
			tmplabel = label.split(",");
			labeljson = JSONArray.fromObject(tmplabel);
		}
		json.put("mLabel", labeljson);
		return json;
	}
	
	//把标签jsonarray转化为字符串存在post对象的mLabel中
	//上传帖子调用
	public Post JsonToStringlabel(JSONObject json) {
		JSONArray array = null;
		array = json.getJSONArray("mLabel");
		//处理标签，合并相同内容的标签
		if(array!=null) {
			//设置String的set用于存放label数组
			Set<String> ll = new HashSet();
			//添加进set，重复标签会自动合并
			for (int i = 0; i < array.size(); i++) {
				ll.add(array.getString(i));
	        }
			Iterator<String> lt = ll.iterator();
			//label_x暂时存放标签数组
			String[] label_x = new String[ll.size()];
			int j = 0;
			while(lt.hasNext()){
				Label tmplabel = new Label(lt.next().toString());
				this.mCompleteLabel.add(tmplabel);
				label_x[j++] = tmplabel.getContent();
			}
			//用逗号分割，生成字符串，给mLabel
			this.mLabel = StringUtils.join(label_x, ",");
		}
		else {
			this.mLabel = null;
			this.mCompleteLabel = null;
		}
		System.out.println(this.mCompleteLabel.size());
		//把json中的JSONArray换成字符串，以便toBean的正常执行
		json.remove("mLabel");
		json.put("mLabel", this.mLabel);
		//注意mCompleteLabel在json里面并没有，所以直接调用toBean不行，需要给post赋值mCompleteLabel
		Post tp = (Post) JSONObject.toBean(json,Post.class);
		tp.setmCompleteLabel(this.getmCompleteLabel());
		return tp;
	}
	
    
	
}