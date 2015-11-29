package com.dagger.operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.database.Cursor;

import com.dagger.handler.ObjectRequestHandler;
import com.dagger.util.DataContainer;
import com.dagger.util.Utils;
import com.example.dagger.BindingClass;
import com.example.dagger.Dagger;
import com.example.dagger.FieldColumnBinding;
import com.example.dagger.ForignColumnBinding;
import com.example.dagger.IdColumnBinding;
import com.example.dagger.KeyValue;
import com.example.dagger.Id.Strategy;

public class InsertBuilder extends BaseBuilder {
	private static InsertBuilder builder;
	private StringBuilder sb = null;
	private Object mappedEntity = null;

	private List<Object> mappedKeyValue = new ArrayList<Object>();
	private List<BaseBuilder> referBuilder = new ArrayList<BaseBuilder>();

	private InsertBuilder(Object entity) {
		super(entity.getClass());
		mappedEntity = entity;
		List<KeyValue> valueslist = entityToKeyValue(mappedEntity);
		builderHandler = new ObjectRequestHandler();
		mappedKeyValue.addAll(valueslist);
		sb = new StringBuilder();
	}

	public static InsertBuilder builder(Object entity) {
		if (builder == null) {
			builder = new InsertBuilder(entity);
		}
		return builder;
	}

	public List<BaseBuilder> getReferBuilder() {
		return this.referBuilder;
	}

	@Override
	public void appendOperation(StringBuilder sb) {
		// TODO Auto-generated method stub
		sb.append("insert into " + tableName);
	}

	public List<KeyValue> entityToKeyValue(Object entity) {
		List<KeyValue> list = new ArrayList<KeyValue>();
		Class<?> entityClass = entity.getClass();
		BindingClass bindClass = DataContainer.Instance().getBindingClass(
				entityClass);
		IdColumnBinding idBinding = bindClass.getIdColumnBinding();
		bindIdKeyValue(idBinding, bindClass, entity, list);
		Map<Integer, FieldColumnBinding> fieldColumnBindings = bindClass
				.getFieldColumnBinding();
		for (Integer key : fieldColumnBindings.keySet()) {
			FieldColumnBinding fieldBinding = fieldColumnBindings.get(key);
			bindColumnKeyValue(fieldBinding.getFieldname(), fieldBinding,
					entity, list);
		}
		List<ForignColumnBinding> forignColumnBindings = bindClass
				.getForignColumnBinding();
		for (int i = 0; i < forignColumnBindings.size(); i++) {
			ForignColumnBinding forignBinding = forignColumnBindings.get(i);
			bindForignKeyValue(forignBinding, bindClass, entity, list,
					idBinding.getValue());
		}

		return list;

	}

	public void bindIdKeyValue(IdColumnBinding idBind, BindingClass bind,
			Object entity, List<KeyValue> container) {
		if (idBind != null) {
			String IdName = idBind.getName();
			String IdType = idBind.getType();
			Strategy strategy = idBind.getStratery();
			Object IdValue = bind.getIdValue(IdName, IdType, strategy, entity);
			bind.getIdColumnBinding().setValue(IdValue);
			KeyValue pair = new KeyValue(IdName, IdValue, IdType);
			container.add(pair);
		}
	}

	public void bindColumnKeyValue(String key, FieldColumnBinding fieldBinding,
			Object entity, List<KeyValue> container) {
		if (fieldBinding != null) {
			String columnName = fieldBinding.getColumnName();
			String fieldType = fieldBinding.getType();
			Object fieldValue = Utils.getFieldValue(key, fieldType, entity);
			KeyValue pair = new KeyValue(columnName, fieldValue, fieldType);
			container.add(pair);
		}
	}

	public void bindForignKeyValue(ForignColumnBinding forignBinding,
			BindingClass bind, Object entity, List<KeyValue> conatainer,
			Object idValue) {
		if (forignBinding != null) {
			String forignName = forignBinding.getFieldName();
			Object forignEntity = getForignEntity(forignName,
					forignBinding.getFieldType(), entity);
			if (forignBinding.isCollection()) {
				List list = (List) forignEntity;
				for (int i = 0; i < list.size(); i++) {
					Object single = list.get(i);
//					Utils.setFieldValue(single,
//							forignBinding.getForignColumType(),
//							forignBinding.getFieldName(), idValue);
					BaseBuilder builderResult = new InsertBuilder(single);
					KeyValue keyValue=new KeyValue(forignBinding.getName(),idValue,forignBinding.getForignType());
					builderResult.putArgment(keyValue);
					referBuilder.add(builderResult);
				}

			} else {
//				Utils.setFieldValue(forignEntity,
//						forignBinding.getForignColumType(),
//						forignBinding.getFieldName(), idValue);
				BaseBuilder builderResult = new InsertBuilder(forignEntity);
				KeyValue keyValue=new KeyValue(forignBinding.getName(),idValue,forignBinding.getForignType());
				builderResult.putArgment(keyValue);
				referBuilder.add(builderResult);
				// buildInsertSql();
			}

		}
	}

	public Object getForignEntity(String forignFieldName, String type,
			Object entity) {
		Object value = Utils.getFieldValue(forignFieldName, type, entity);
		return value;
	}

	@Override
	public String sqlBuilder() {
		// TODO Auto-generated method stub
		appendOperation(sb);
		sb.append("(");
		for (int i = 0; i < mappedKeyValue.size(); i++) {
			KeyValue keyValue = (KeyValue) mappedKeyValue.get(i);
			sb.append(keyValue.getKey());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")values(");
		for (int j = 0; j < mappedKeyValue.size(); j++) {
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(");");
		return sb.toString();
	}

	public void insert() {
		builderHandler.handler(this);
	}

	@Override
	public List<Object> getArguments() {
		// TODO Auto-generated method stub
		return mappedKeyValue;
	}

}
