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
	private int mLevel;   //0:����  1:1�����ۣ�2��2����3��3��
    private String mTitle;
    private String mContent;      //����
    private String mImageID;
    private String mImageUrl;        //��Ӧ��ͼƬ·��
    private String mRecordUrl; //¼��·��
    private String mReleaseTime;
    private float mRatingGrade;  //��������
    private int gradenum;     //�������ֵ�����
    private String mLocate; //����λ��
    private String subtag;   //����Ƿ����¼�����   0:û�У�1����
    private String mSuperId;   //�ϼ����ۣ�Ҳ���Ǹ����۶�Ӧ�Ķ�����������ӵĻ���superΪnull
    private String mUserId;  //�������û�Id����token
    private String mReplyerId;   //���ظ��ߣ�Ҳ������һ����id
    private String mMessage;  //ǰ����Ҫ�õ��ĳɹ���־
    private Double mLon;//γ��
    private Double mLat;//����
    
    //����ǰ̨�ϴ�һ��post֮���̨���½���Ӧ��Label����set
    @OneToMany(targetEntity=Label.class, cascade=CascadeType.ALL)
    @JoinColumn(name="mPostId", referencedColumnName="mPostId")
    private Set<Label> mCompleteLabel = new HashSet<Label>();
    //�����Ҫ���ڷ��͸�ǰ̨json��ʱ���ã���Ϊ���͸�ǰ̨����Label��set
    //ת��Ϊ�ַ������ö��ŷָ�������û��ʶ��
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
			//���һ��JSONObject��JSONArray
			array.add(json);
		}
		return array;
	}
	
	
	
	//�ѱ�ǩ�ַ���ת��Ϊ���飬�Ӷ����JSONArray�ŵ�json��
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
	
	//�ѱ�ǩjsonarrayת��Ϊ�ַ�������post�����mLabel��
	//�ϴ����ӵ���
	public Post JsonToStringlabel(JSONObject json) {
		JSONArray array = null;
		array = json.getJSONArray("mLabel");
		//�����ǩ���ϲ���ͬ���ݵı�ǩ
		if(array!=null) {
			//����String��set���ڴ��label����
			Set<String> ll = new HashSet();
			//��ӽ�set���ظ���ǩ���Զ��ϲ�
			for (int i = 0; i < array.size(); i++) {
				ll.add(array.getString(i));
	        }
			Iterator<String> lt = ll.iterator();
			//label_x��ʱ��ű�ǩ����
			String[] label_x = new String[ll.size()];
			int j = 0;
			while(lt.hasNext()){
				Label tmplabel = new Label(lt.next().toString());
				this.mCompleteLabel.add(tmplabel);
				label_x[j++] = tmplabel.getContent();
			}
			//�ö��ŷָ�����ַ�������mLabel
			this.mLabel = StringUtils.join(label_x, ",");
		}
		else {
			this.mLabel = null;
			this.mCompleteLabel = null;
		}
		System.out.println(this.mCompleteLabel.size());
		//��json�е�JSONArray�����ַ������Ա�toBean������ִ��
		json.remove("mLabel");
		json.put("mLabel", this.mLabel);
		//ע��mCompleteLabel��json���沢û�У�����ֱ�ӵ���toBean���У���Ҫ��post��ֵmCompleteLabel
		Post tp = (Post) JSONObject.toBean(json,Post.class);
		tp.setmCompleteLabel(this.getmCompleteLabel());
		return tp;
	}
	
    
	
}