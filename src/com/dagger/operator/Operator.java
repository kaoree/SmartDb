package com.dagger.operator;

import java.util.List;
import java.util.Map;
import com.dagger.util.DataContainer;
import com.example.dagger.BindingClass;

public abstract class Operator {
	protected int indice; // operator在list集合中的顺序
	protected Operator p, c;// p为parent,c 为children;
	protected Map<String, BindingClass> bindMap = null;

	public abstract void prepareBindArguments(List<Object> list);

	//spublic abstract void createPrefix(StringBuilder sb);

	public abstract StringBuilder operator(StringBuilder sb);

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}
	

}
