package com.dagger.operator;

public class NoEqual extends Where {

	public NoEqual(String key, String value) {
		super(key, value);
		// TODO Auto-generated constructor stub
	}

	@Override
	public StringBuilder operator(StringBuilder sb) {
		// TODO Auto-generated method stub
		return sb.append(key + "<>?");
	}

}
