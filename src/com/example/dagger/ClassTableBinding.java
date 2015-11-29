package com.example.dagger;

import java.io.Serializable;

public class ClassTableBinding implements Serializable{
	String className;
	String tableName;
	public ClassTableBinding(String classname,String tablename){
		this.className=classname;
		this.tableName=tablename;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	

}
