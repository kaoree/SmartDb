package com.example.dagger;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.dagger.util.DataContainer;
import com.dagger.util.Utils;
import com.example.dagger.Id.Strategy;

public class BindingClass implements Serializable {
	private static final long serialVersionUID = 7981560250804078637l;
	public String tagetType;
	public String packetName;
	public String className;
	private int fieldIndex;
	public IdColumnBinding idbind = null;
	private StringBuilder forignbuilder = new StringBuilder();
	private HashMap<String, Integer> autoIdMaps = new LinkedHashMap<String, Integer>();
	private List<ForignColumnBinding> forignColumnBindings = new ArrayList<ForignColumnBinding>();
	private ClassTableBinding classBinding = null;
	// private Map<String, ClassTableBinding> classTableBindings = new
	// LinkedHashMap<String, ClassTableBinding>();
	private Map<Integer, FieldColumnBinding> fieldColumnBindings = new TreeMap<Integer, FieldColumnBinding>(
			new TreeMapComarator());

	public BindingClass(String target, String packet, String classname) {
		this.tagetType = target;
		this.packetName = packet;
		this.className = classname;
	}

	public class TreeMapComarator implements Comparator<Integer>, Serializable {

		@Override
		public int compare(Integer lhs, Integer rhs) {
			// TODO Auto-generated method stub
			// 降序排序
			return lhs.compareTo(rhs);
		}

	}

	/*
	 * @Override public String toString() { // TODO Auto-generated method stub
	 * return "{'tagetType':" + tagetType + ",'packetName':" + packetName +
	 * ",'className':" + className + ",'fieldIndex':" + fieldIndex +
	 * ",'idbind':" + idbind.toString(); }
	 */

	public void addFieldColumn(int sort, FieldColumnBinding bindings) {
		fieldColumnBindings.put(sort, bindings);
	}

	public StringBuilder getStringBuilder() {
		return forignbuilder;
	}

	public void addIdColumnBinding(IdColumnBinding binding) {
		idbind = binding;
	}

	// public void setIdMaps(HashMap<Class, Integer> map){
	// autoIdMaps=map;
	// }
	public IdColumnBinding getIdColumnBinding() {
		return idbind;
	}

	public Map<Integer, FieldColumnBinding> getFieldColumnBinding() {
		return fieldColumnBindings;
	}

	public List<ForignColumnBinding> getForignColumnBinding() {
		return forignColumnBindings;
	}

	public void setForignColumnBinding(List<ForignColumnBinding> list) {
		forignColumnBindings = list;
	}

	public void addClassTable(ClassTableBinding bindings) {
		classBinding = bindings;
	}

	public ClassTableBinding getClassTable() {
		return classBinding;
	}

	public void addForignColumn(ForignColumnBinding bindings) {
		forignColumnBindings.add(bindings);
	}

	public String getPacketName() {
		return packetName;
	}

	public void setPacketName(String packetName) {
		this.packetName = packetName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public StringBuilder brewJava() {
		StringBuilder builder = new StringBuilder();
		builder.append("create table " + className);
		builder.append("(");
		if (idbind != null) {
			emitIdColumnBinding(fieldIndex, builder, idbind);
			fieldIndex++;
		}
		// sortMap(fieldColumnBindings);
		for (Map.Entry<Integer, FieldColumnBinding> fieldColumn : fieldColumnBindings
				.entrySet()) {
			emitFieldColumnBinding(fieldIndex, builder, fieldColumn.getValue());
			fieldIndex++;
		}
//		for (int i = 0; i < forignColumnBindings.size(); i++) {
//			emitForignKeyColumnBinding(fieldIndex, forignColumnBindings.get(i));
//			fieldIndex++;
//		}
		if (forignbuilder.length() > 0) {
			builder.append(forignbuilder.toString());
		}
		fieldIndex = 0;
		builder.append(")");
		return builder;

	}

	private void emitIdColumnBinding(int index, StringBuilder builder,
			IdColumnBinding binding) {
		String columnName = binding.getName();
		String fieldType = binding.getType();
		Strategy idStrategy = binding.getStratery();
		String columnTyp = convertFieldToColumnType(fieldType);
		if (index > 0) {
			builder.append(",");
		}
		builder.append(columnName).append(" ");
		if (idStrategy == Strategy.AUTO) {
			builder.append(columnTyp);
			builder.append("  primary key ");
			builder.append("autoincrement ");
		} else if (idStrategy == Strategy.UUID) {
			builder.append(columnTyp);
			builder.append("primary key ");
		}

	}

	private void emitFieldColumnBinding(int index, StringBuilder builder,
			FieldColumnBinding binding) {
		String columnName = binding.getColumnName();
		String fieldType = binding.getType();
		String columnType = convertFieldToColumnType(fieldType);
		if (index > 0) {
			builder.append(",");
		}
		builder.append(columnName).append(" ");
		builder.append(columnType);
	}

	

	public void sortMap(Map<Integer, FieldColumnBinding> orignMap) {

	}

	public Object getIdValue(String IdName, String type, Strategy strategy,
			Object entity) {
		Object value = null;
		if (strategy == Strategy.AUTO) {
			if (autoIdMaps.containsKey(entity.getClass().getSimpleName())) {
				Integer cacheId = autoIdMaps.get(entity.getClass().getSimpleName());
				value = cacheId + 1;
			} else {
				Integer initialId = 0;
				autoIdMaps.put(entity.getClass().getSimpleName(), initialId);
				value = initialId;
			}
		} else if (strategy == Strategy.UUID) {
			Method getMethod = Utils.getColumnGetMethod(entity.getClass(),
					type, IdName);
			try {
				value = getMethod.invoke(entity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return value;
	}

	private String convertFieldToColumnType(String type) {
		String columnType = "";
		if (type.equals("java.lang.String")) {
			columnType = "TEXT";
		} else if (type.equals("int") || type.equals("short")
				|| type.equals("double") || type.equals("long")) {
			columnType = "INTEGER";
		} else if (type.equals("float")) {
			columnType = "REAL";
		} else if (type.equals("null")) {
			columnType = "NULL";
		}

		return columnType;
	}
}
