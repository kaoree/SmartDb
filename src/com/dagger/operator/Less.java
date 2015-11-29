package com.dagger.operator;

public class Less extends Where {
	

	public Less(String key, String value) {
		super(key, value);
		key = key;
		value = value;

	}

	@Override
	public StringBuilder operator(StringBuilder sb) {
		// TODO Auto-generated method stub
		return sb.append(key + "<?");
	}


}
