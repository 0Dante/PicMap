package com.tgb.manager;

import com.tgb.entity.Picture;
import com.tgb.dao.PicDao;

public interface PicManager {

	public void savePic(Picture p) ;
	
	public void setPicDao(PicDao picDao) ;
}

