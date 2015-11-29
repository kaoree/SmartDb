package com.example.dagger;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.dagger.operator.InsertBuilder;
import com.dagger.operator.QueryBuilder;
import com.dagger.util.DBHelper;
import com.dagger.util.DataContainer;

public class Dagger {
	private static Dagger instance;
	private static HashMap<String, BindingClass> bindingMap = null;
	private static Context context;

	public void init(Context context,String dbName,int version) {
		DBHelper.Instance().setContext(context);
       if(!TextUtils.isEmpty(dbName)){
    	  DBHelper.Instance().setDbName(dbName); 
       }
       if(!TextUtils.isEmpty(version+"")){
    	   DBHelper.Instance().setVersion(version);
       }
      importDataFormSDCard(context);
	}
    public void importDataFormSDCard(Context context){
    	Resources resources=context.getResources();
		InputStream is=resources.openRawResource(R.raw.config);
		try {
			ObjectInputStream ois = new ObjectInputStream(is);
			DataContainer.Instance().setBindclassMap((HashMap<String, BindingClass>) ois.readObject());

		} catch (Exception e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		} 
    }
	public static Dagger instance() {
		if (instance == null) {
			instance = new Dagger();
		}
		return instance;
	}

	private Dagger() {

	}

//	public static void initBindClassMap(HashMap<String, BindingClass> map) {
//		bindingMap = map;
//	}
//
//	public static HashMap<String, BindingClass> getBindClassMap() {
//
//		return bindingMap;
//	}

	

	public void create(Object entity) {
		InsertBuilder.builder(entity).insert();
	}

	public void saveOrUpdate(Object entity) {

	}

	public void update(Object entity) {

	}
   
	public QueryBuilder Select(Class entityClass) {
		return QueryBuilder.builder(entityClass);
	}
}
