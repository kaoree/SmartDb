package com.dagger.operator;

import java.util.ArrayList;
import java.util.List;

import com.dagger.handler.RawQueryRequestHandler;
import com.dagger.operator.OrderBy.Order;

public class QueryBuilder extends BaseBuilder {

	public List<Operator> operatorlist = new ArrayList<Operator>();
	private Class entityClass;
	public StringBuilder sb = null;
	private static QueryBuilder builder;
	private List<Object> arguments = new ArrayList<Object>();
	private List<Integer> indiceList = new ArrayList<Integer>();

	private QueryBuilder(Class entityClass) {
		super(entityClass);
		this.entityClass = entityClass;
		sb = new StringBuilder();
		builderHandler = new RawQueryRequestHandler();

	}

	public static QueryBuilder builder(Class entityClass) {
		if (builder == null) {
			builder = new QueryBuilder(entityClass);
		}
		return builder;
	}
    public Class getTargetClass(){
    	return this.entityClass;
    }
	public QueryBuilder where(String columnName, String value) {
		Where where = new Where(columnName, value);
		addOperator(where);
		return this;
	}

	public QueryBuilder gt(String columnName, String value) {
		More more = new More(columnName, value);
		addOperator(more);
		return this;
	}

	public QueryBuilder lt(String columnName, String value) {
		Less more = new Less(columnName, value);
		addOperator(more);
		return this;
	}

	public QueryBuilder neq(String columnName, String value) {
		NoEqual neq = new NoEqual(columnName, value);
		addOperator(neq);
		return this;
	}

	public QueryBuilder orderBy(String columnName, Order order) {
		OrderBy orderby = new OrderBy(columnName, order);
		addOperator(orderby);
		return this;
	}

	public QueryBuilder in(String columnName, List<String> arguments) {
		In mIn = new In(columnName, arguments);
		addOperator(mIn);
		return this;
	}

	public QueryBuilder between(String columnName, String upper, String low) {
		Between be = new Between(columnName, low, upper);
		addOperator(be);
		return this;
	}

	public void addOperator(Operator op) {
		operatorlist.add(op);
	}

	@Override
	public void appendOperation(StringBuilder sb) {
		// TODO Auto-generated method stub
		sb.append("select *from");
		sb.append("  ");
	}

	@Override
	public String sqlBuilder() {
		// TODO Auto-generated method stub
		appendOperation(sb);
		sb.append(entityClass.getSimpleName().toString());
		sb.append("  ");
		for (int i = 0; i < operatorlist.size(); i++) {
			Operator op = operatorlist.get(i);
			if (op instanceof Where) {
				if (indiceList.contains(op.getIndice())) {
					sb.append(" and");
					op.operator(sb);
					op.prepareBindArguments(arguments);

				} else {
					indiceList.add(op.getIndice());
					sb.append("where ");
					op.operator(sb);
					op.prepareBindArguments(arguments);
				}
			} else if (op instanceof OrderBy || op instanceof GroupBy) {
				if (indiceList.contains(op.getIndice())) {
					sb.append(",");
					op.operator(sb);
				} else {
					indiceList.add(op.getIndice());
					sb.append("");
					op.operator(sb);
				}
			}
			// operatorlist.get(i).operator(sb);
		}
		return sb.toString();
	}

	public List<Object> query() {
		builder = null;
		return builderHandler.handler(this);
		
	}

	@Override
	public List<Object> getArguments() {
		return arguments;
	}

}
