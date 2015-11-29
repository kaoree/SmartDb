package com.dagger.operator;

import java.util.ArrayList;
import java.util.List;

public class In extends Where {
	private String key;
	private List<String> values = new ArrayList<String>();

	public In(String key, List<String> values) {
		super(key, null);
		this.key = key;
		this.values = values;

	}

	@Override
	public StringBuilder operator(StringBuilder sb) {
		// TODO Auto-generated method stub
		sb.append(key + "in(");
		for (int i = 0; i < values.size(); i++) {
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return sb;
	}

	@Override
	public void prepareBindArguments(List<Object> list) {
		// TODO Auto-generated method stub
		list.addAll(values);

	}

}
