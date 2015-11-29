package com.dagger.operator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.dagger.util.DataContainer;
import com.example.dagger.BindingClass;
import com.example.dagger.Id.Strategy;
import com.example.dagger.IdColumnBinding;

//bindMap = DataContainer.Instance().getBindclassMap();
//if (bindMap.containsKey(key)) {
//	BindingClass bindClass = bindMap.get(key);
//	IdColumnBinding idBind = bindClass.getIdColumnBinding();
//	if (idBind.getName().equals(key)) {
//		if (idBind.getStratery() == Strategy.AUTO) {
//			throw new IllegalArgumentException(
//					"the id is auto generated,couldn't set value to it");
//		} else {
//
//		}
//	}
//}
public class Where extends Operator {
	private Where next;

	protected StringBuilder sql = new StringBuilder();
	protected String key;
	protected Object value;

	public Where(String key, String value) {
		this.key = key;
		this.value = value;
		this.indice=1;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public StringBuilder operator(StringBuilder sb) {
		// TODO Auto-generated method stub

		return sb.append(key + "=?");
	}

	@Override
	public void prepareBindArguments(List<Object> list) {
		// TODO Auto-generated method stub
	    list.add(value+"");
	}



}
