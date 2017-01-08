package com.tgb.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="T_PIC")
public class Picture implements Serializable{
	

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue(generator="system-uuid")
		@GenericGenerator(name = "system-uuid",strategy="uuid")
		@Column(length=32)
		private String id;
		
		@Column(length=32)
		private Long size;
		
		@Column(length=32)
		private String contenttype;
		
		@Column(length=32)
		private String picname;
		
		@Column(length=32)
		private String originalname;
		
		@Column(length=60)
		private String accesspath;
		

		public String getId() {
			return id;
		}



		public void setId(String id) {
			this.id = id;
		}



		public Long getSize() {
			return size;
		}



		public void setSize(Long size) {
			this.size = size;
		}



		public String getContenttype() {
			return contenttype;
		}



		public void setContenttype(String contenttype) {
			this.contenttype = contenttype;
		}



		public String getPicname() {
			return picname;
		}



		public void setPicname(String picname) {
			this.picname = picname;
		}



		public String getOriginalname() {
			return originalname;
		}



		public void setOriginalname(String originalname) {
			this.originalname = originalname;
		}


		public String getAccesspath() {
			return accesspath;
		}



		public void setAccesspath(String accessPath) {
			this.accesspath = accessPath;
		}

		public Picture(long size, String contenttype, String picname, String originalname,String accessPath){
			this.id = null;
			this.contenttype = contenttype;
			this.picname= picname;
			this.originalname = originalname;
			this.size = size;
			this.accesspath = accessPath;
		}
}
