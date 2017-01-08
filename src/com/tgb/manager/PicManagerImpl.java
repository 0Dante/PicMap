package com.tgb.manager;

import java.util.List;

import com.tgb.dao.PicDao;
import com.tgb.entity.Picture;
import com.tgb.entity.User;

public class PicManagerImpl implements PicManager {

		private PicDao picDao;
		
		public void setPicDao(PicDao picDao) {
			this.picDao = picDao;
		}

		public void savePic(Picture p) {
			picDao.savePic(p);
		}

	

}
