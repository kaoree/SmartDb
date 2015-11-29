package com.dagger.operator;

public class More extends Where {
	private String key;
	private Object value;

	public More(String key, String value) {
		super(key,value);
		this.key = key;
		this.value = value;
		
	}

	@Override
	public StringBuilder operator(StringBuilder sb) {
		// TODO Auto-generated method stub
		return sb.append(key+">?");
	}

	

}
