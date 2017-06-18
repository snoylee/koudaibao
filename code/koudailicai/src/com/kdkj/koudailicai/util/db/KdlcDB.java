package com.kdkj.koudailicai.util.db;

import java.util.List;

import com.kdkj.koudailicai.domain.SelfCenterRecord;
import com.kdkj.koudailicai.view.KDLCApplication;

public class KdlcDB {
	
	
	public static void addByEntity(Object entity) {
		if(entity != null)
			KDLCApplication.app.kdDb.save(entity);
	}
	
	public static void addByEntityList(List<?> entityList) {
		for (int i=0; i < entityList.size(); i++) {
			KDLCApplication.app.kdDb.save(entityList.get(i));
		}
	}

	public static void updateByWhere(Class<?> clazz, String strWhere) {
		KDLCApplication.app.kdDb.update(clazz, strWhere);
	}
	
	public static List<?> findAllByClass(Class<?> clazz) {
		return KDLCApplication.app.kdDb.findAll(clazz);
	}
	
	public static List<?> findAllByClassByOrder(Class<?> clazz, String orderBy){
		return KDLCApplication.app.kdDb.findAll(clazz, orderBy);
	}
	
	public static Object findOneByWhere(Class<?> clazz, String strWhere) {
		List<?> dataList = KDLCApplication.app.kdDb.findAllByWhere(clazz, strWhere);
		if(dataList.size() == 1) {
			return dataList.get(0);
		} else {
			return null;
		}
	}
	
	public static List<?> findByWhere(Class<?> clazz, String strWhere) {
		return KDLCApplication.app.kdDb.findAllByWhere(clazz, strWhere);
	}
	
	public static void deleteByWhere(Class<?> clazz, String strWhere) {
		KDLCApplication.app.kdDb.deleteByWhere(clazz, strWhere);
	}
	
	public static void deleteByEntity(Object entity) {
		if(entity != null)
			KDLCApplication.app.kdDb.delete(entity);
	}
	
	public static void deleteAllByClass(Class<?> clazz) {
		KDLCApplication.app.kdDb.deleteByWhere(clazz, "1=1");
	}
	
	public static void deleteById(Class<?> clazz, Object id) {
		KDLCApplication.app.kdDb.deleteById(clazz, id);
	}
}
