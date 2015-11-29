package com.example.dagger;

import java.io.Serializable;

import android.database.Cursor;

import com.dagger.util.Utils;
import com.example.dagger.Id.Strategy;

public class IdColumnBinding  implements Serializable{
	private String name;
	private String type;
	private Object value;
	private Strategy stratery;

	public IdColumnBinding(String name, String type, Strategy stratery) {
		this.name = name;
		this.type = type;
		this.stratery = stratery;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Strategy getStratery() {
		return stratery;
	}

	public void setStratery(Strategy stratery) {
		this.stratery = stratery;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}



//	@Override
//	public void setValueToEntity(int index,Cursor cursor,Object entity) {
//		// TODO Auto-generated method stub
////		Object value = null;
////		value = Utils.getDbValue(index, cursor, type);
////		 Utils.setFieldValue(entity, type, name,value);
//	}
	
}
