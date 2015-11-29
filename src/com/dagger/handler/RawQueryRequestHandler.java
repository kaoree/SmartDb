package com.dagger.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.dagger.interfaces.RequestHandler;
import com.dagger.operator.BaseBuilder;
import com.dagger.operator.QueryBuilder;
import com.dagger.util.DBHelper;
import com.dagger.util.Utils;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class RawQueryRequestHandler implements RequestHandler {
	private DBHelper mHelper;
	private SQLiteDatabase db;
    private String[] destArgs;
	public RawQueryRequestHandler() {
		db = DBHelper.Instance().open();
	}

	public Object initializeEntity(String name) throws Exception {
		Class entityClass = Class.forName(name);
		return entityClass.newInstance();
	}

	@Override
	public List<Object> handler(BaseBuilder statementBuilder) {
		// TODO Auto-generated method stub
		List<Object> resultList = new ArrayList<Object>();
		String sql = statementBuilder.sqlBuilder();
		List<Object> args = statementBuilder.getArguments();
		Object[] oriArgs = args.toArray();
		String[] bindArgs = new String[oriArgs.length];
		  for(int i = 0; i < oriArgs.length ; i++){
			  bindArgs[i] = (String)oriArgs[i];
		  }
		Cursor cursor = db.rawQuery(sql, bindArgs);

		while (cursor.moveToNext()) {
			try {
				Object entity = initializeEntity(((QueryBuilder) statementBuilder)
						.getTargetClass().getCanonicalName());
				Utils.bindCursorValue(cursor, entity);
				resultList.add(entity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cursor.close();
		return resultList;
	}

}
