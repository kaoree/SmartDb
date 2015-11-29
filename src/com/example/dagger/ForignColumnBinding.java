package com.example.dagger;

import java.io.Serializable;
import java.lang.reflect.Type;

import android.database.Cursor;

import com.dagger.util.Utils;

public class ForignColumnBinding implements Serializable {
	private String name;
	private String forignColumn;
	private String fieldType;
	private boolean isCollection;
	private String fieldName;
	private String forignColumType;
	private String referenceClass;
	private String forignType;

	public ForignColumnBinding(String name, String field, String type,
			String forigncolType, String forignType, String forignColumn,
			String reference, boolean isC) {
		this.name = name;
		this.fieldName = field;
		this.fieldType = type;
		this.forignColumType = forigncolType;
		this.forignColumn = forignColumn;
		this.referenceClass = reference;
		this.forignType = forignType;
		this.isCollection = isC;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getForignType() {
		return forignType;
	}

	public void setForignType(String forignType) {
		this.forignType = forignType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getForignColumn() {
		return forignColumn;
	}

	public String getForignColumType() {
		return forignColumType;
	}

	public void setForignColumType(String forignColumType) {
		this.forignColumType = forignColumType;
	}

	public void setForignColumn(String forignColumn) {
		this.forignColumn = forignColumn;
	}

	public String getReferenceClass() {
		return referenceClass;
	}

	public void setReferenceClass(String referenceClass) {
		this.referenceClass = referenceClass;
	}

	public boolean isCollection() {
		return isCollection;
	}

	public void setCollection(boolean isCollection) {
		this.isCollection = isCollection;
	}

	// @Override
	// public void setValueToEntity(int index, Cursor cursor, Object entity) {
	// // TODO Auto-generated method stub
	// Object value = null;
	// // value=Dagger.instance().Select()
	// // value = Utils.getDbValue(index, cursor, type);
	// // Utils.setFieldValue(entity, type, name,value);
	// }

}
