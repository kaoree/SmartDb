package com.dagger.operator;

import java.util.ArrayList;
import java.util.List;

public class Between extends Where {
	private String key;
	private Object low, upper;

	public Between(String key, String low, String upper) {
		super(key, null);
		this.key = key;
		this.low = low;
		this.upper = upper;
		this.indice = 5;
	}

	public void setLow(Object low) {
		this.low = low;
	}

	public void setUpper(Object upper) {
		this.upper = upper;
	}

	@Override
	public StringBuilder operator(StringBuilder sb) {
		// TODO Auto-generated method stub
		return sb.append(key + "between ?and ?");
	}

	@Override
	public void prepareBindArguments(List<Object> list) {
		// TODO Auto-generated method stub
		list.add(low);
		list.add(upper);

	}

}
