package com.dagger.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;

import com.example.dagger.BindingClass;
import com.example.dagger.Dagger;
import com.example.dagger.FieldColumnBinding;
import com.example.dagger.ForignColumnBinding;
import com.example.dagger.IdColumnBinding;
import com.example.dagger.Id.Strategy;

public class Utils {

	public static Object getFieldValue(String fieldname, String type,
			Object entity) {
		Object value = null;
		Method getMethod = getColumnGetMethod(entity.getClass(), type,
				fieldname);
		try {
			value = getMethod.invoke(entity);
			if (type.equals("boolean.class")) {
				boolean bValue = (boolean) value;
				if (bValue) {
					value = "1";
				} else {
					value = "0";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	public static void bindCursorValue(Cursor cursor,Object entity)throws Exception{
		int index=0;
		Class<?> entityClass = entity.getClass();
		BindingClass bindClass = DataContainer.Instance().getBindingClass(
				entityClass);
		IdColumnBinding idBinding = bindClass.getIdColumnBinding();
		setIdValueToEntity(index,cursor,entity,idBinding.getName(),idBinding.getType(),idBinding);
		index++;
		Map<Integer, FieldColumnBinding> fieldColumnBindings = bindClass
				.getFieldColumnBinding();
		for (Integer key : fieldColumnBindings.keySet()) {
			FieldColumnBinding fieldBinding = fieldColumnBindings.get(key);
			setValueToEntity(index,cursor,entity,fieldBinding.getFieldname(),fieldBinding.getType());
			index++;
		}
		List<ForignColumnBinding> forignColumnBindings = bindClass
				.getForignColumnBinding();
		for (int i = 0; i < forignColumnBindings.size(); i++) {
			ForignColumnBinding forignBinding = forignColumnBindings.get(i);
			Class cls=Class.forName(forignBinding.getFieldType());
			String type="";
			if(forignBinding.isCollection()){
				type="list";
			}else{
				type=forignBinding.getFieldType();
			}
			setForignValueToEntity(entity,cls,forignBinding.getName(),String.valueOf(idBinding.getValue()),type,forignBinding.getFieldName());
			index++;
		}
	
	}
	public static void setIdValueToEntity(int index,Cursor cursor,Object entity,String name,String type,IdColumnBinding bind){
		Object value=getDbValue(index,cursor,type);
		bind.setValue(value);
		setFieldValue(entity, type, name,value);
		
	}
	public static void setValueToEntity(int index,Cursor cursor,Object entity,String name,String type){
		Object value=getDbValue(index,cursor,type);
		setFieldValue(entity, type, name,value);
		
	}
	public static void setForignValueToEntity(Object entity,Class cl,String forignCol,String idValue,String type,String name){
		List<Object> values=Dagger.instance().Select(cl).where(forignCol, idValue).query();
		setFieldValue(entity,type,name,values);
	}
    public static void setFieldValue(Object entity,String type,String field,Object value){
    	Method setMethod =getColumnSetMethod(entity.getClass(),type,field);
    	try {
			setMethod.invoke(entity, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
    }
	public static Object getDbValue(int index, Cursor cursor, String type) {
		Object value = null;
		if (type.equals("java.lang.String")) {
			value = cursor.getString(index);
		} else if (type.equals("int")) {
			value = cursor.getInt(index);
		} else if (type.equals("double")) {
			value = cursor.getDouble(index);
		} else if (type.equals("float")) {
			value = cursor.getFloat(index);
		} else if (type.equals("long")) {
			value = cursor.getLong(index);
		}
		return value;
	}
    public static Class strToClass(String type)throws Exception{
    	Class cls=null;
    	if (type.equals("java.lang.String")) {
    		cls =String.class;
		} else if (type.equals("int")) {
			cls =int.class;
		} else if (type.equals("double")) {
			cls =Double.class;
		} else if (type.equals("float")) {
			cls =Float.class;
		} else if (type.equals("long")) {
			cls =Long.class;
		} else if(type.equals("list")){
			cls=List.class;
		}else{
			cls=Class.forName(type);
		}
    	return cls;
    }
	// 获得Field的set 方法
	public static Method getColumnSetMethod(Class<?> entityType, String type,String field) {
		String fieldName = field;
		Method setMethod = null;
		if (type == "boolean") {
			setMethod = getBooleanColumnSetMethod(entityType, field,type);
		}
		if (setMethod == null) {
			String methodName = "set" + fieldName.substring(0, 1).toUpperCase()
					+ fieldName.substring(1);		
			try {
				Class[] cArg = new Class[1];
				Class paramType=strToClass(type);
				cArg[0]=paramType;
				setMethod = entityType.getDeclaredMethod(methodName,
						cArg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (setMethod == null
				&& !Object.class.equals(entityType.getSuperclass())) {
			return getColumnSetMethod(entityType.getSuperclass(),type, field);
		}
		return setMethod;
	}

	private static Method getBooleanColumnSetMethod(Class<?> entityType,
			String field,String type) {
		String fieldName = field;
		String methodName = null;
		if (isStartWithIs(field)) {
			methodName = "set" + fieldName.substring(2, 3).toUpperCase()
					+ fieldName.substring(3);
		} else {
			methodName = "set" + fieldName.substring(0, 1).toUpperCase()
					+ fieldName.substring(1);
		}		
		try {
			Class paramType=Class.forName(type);
			return entityType.getDeclaredMethod(methodName, paramType);
		} catch (Exception e) {

		}
		return null;
	}

	// 获取每个Field的 get方法
	public static Method getColumnGetMethod(Class<?> entityType, String type,
			String field) {
		String fieldName = field;
		Method getMethod = null;
		if (type.equals("boolean.class")) {
			getMethod = getBooleanColumnGetMethod(entityType, fieldName);
		}
		if (getMethod == null) {
			String methodName = "get" + fieldName.substring(0, 1).toUpperCase()
					+ fieldName.substring(1);
			try {
				getMethod = entityType.getDeclaredMethod(methodName);
			} catch (NoSuchMethodException e) {

			}
		}
		if (getMethod == null
				&& !Object.class.equals(entityType.getSuperclass())) {
			return getColumnGetMethod(entityType.getSuperclass(), type, field);
		}
		return getMethod;
	}

	private static Method getBooleanColumnGetMethod(Class<?> entityType,
			final String fieldName) {
		String methodName = "is" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
		if (isStartWithIs(fieldName)) {
			methodName = fieldName;
		}
		try {
			return entityType.getDeclaredMethod(methodName);
		} catch (NoSuchMethodException e) {
		}
		return null;
	}

	private static boolean isStartWithIs(final String fieldName) {
		return fieldName != null && fieldName.startsWith("is");
	}
}
