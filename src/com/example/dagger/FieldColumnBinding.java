package com.example.dagger;

import java.io.Serializable;

import android.database.Cursor;

import com.dagger.util.Utils;

public class FieldColumnBinding implements Serializable {
	private String columname;
	private String fieldname;
	private String type;
	private int sort;
	private boolean isCollection;

	public FieldColumnBinding(String columnname, String fieldname, String type,
			int sort, boolean isCollection) {
		this.columname = columnname;
		this.fieldname = fieldname;
		this.type = type;
		this.sort = sort;
		this.isCollection = isCollection;
	}

	public String getColumnName() {
		return columname;
	}

	public void setColumnName(String name) {
		this.columname = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isCollection() {
		return isCollection;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public void setCollection(boolean isCollection) {
		this.isCollection = isCollection;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
