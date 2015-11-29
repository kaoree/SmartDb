package com.dagger.operator;

import java.util.ArrayList;
import java.util.List;

public class OrderBy extends Operator {
	private String key;
	private Order order;

	public OrderBy(String key, Order order) {
		this.key = key;
		this.order = order;
		this.indice=7;
	}

	@Override
	public StringBuilder operator(StringBuilder sb) {
		// TODO Auto-generated method stub
		return sb.append(key+" "+order);
	}

	@Override
	public void prepareBindArguments(List<Object> list) {
		// TODO Auto-generated method stub
        
	}

	public enum Order {
		DESC, ASC
	}
}
