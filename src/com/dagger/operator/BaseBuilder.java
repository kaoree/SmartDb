package com.dagger.operator;

import java.util.List;

import com.dagger.interfaces.RequestHandler;
import com.dagger.util.DataContainer;
import com.example.dagger.BindingClass;
import com.example.dagger.KeyValue;

public abstract class BaseBuilder {
	protected BindingClass bindClass = null;
	protected String tableName;
	protected RequestHandler builderHandler;

	public BaseBuilder(Class<?> entityClass) {
		bindClass = DataContainer.Instance().getBindclassMap()
				.get(entityClass.getSimpleName());
		tableName = bindClass.getClassTable().getTableName();
	}
    public void putArgment(KeyValue keyValue){
    	getArguments().add(keyValue);
    }
	public abstract void appendOperation(StringBuilder sb);
	public abstract List<Object> getArguments();
	public abstract String sqlBuilder();

}
