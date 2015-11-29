package com.dagger.operator;

import java.util.List;

public class GroupBy extends Operator {
	private String key;

	public GroupBy(String key) {
		this.key = key;
		this.indice = 6;
	}

	@Override
	public void prepareBindArguments(List<Object> list) {
		// TODO Auto-generated method stub

	}
	@Override
	public StringBuilder operator(StringBuilder sb) {
		// TODO Auto-generated method stub
		return sb.append(key);
	}

}
