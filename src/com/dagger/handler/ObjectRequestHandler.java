package com.dagger.handler;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.dagger.interfaces.RequestHandler;
import com.dagger.operator.BaseBuilder;
import com.dagger.util.DBHelper;
import com.example.dagger.KeyValue;
import com.dagger.operator.InsertBuilder;

public class ObjectRequestHandler implements RequestHandler {
	private DBHelper mHelper;
	private SQLiteDatabase db;

	public ObjectRequestHandler() {
		db = DBHelper.Instance().open();
	}

	@Override
	public List<Object> handler(BaseBuilder statementBuilder) {
		// TODO Auto-generated method stub
		String sql = statementBuilder.sqlBuilder();
		List<BaseBuilder> referBuilder=((InsertBuilder)statementBuilder).getReferBuilder();
		List<Object> args = statementBuilder.getArguments();
		SQLiteStatement statement = db.compileStatement(sql);
		for(int i=0;i<args.size();i++){
			KeyValue keyValue=(KeyValue) args.get(i);
			parseStatement(i+1,statement,keyValue);
		}
		statement.executeInsert();
       for(int i=0;i<referBuilder.size();i++){
    	   handler(referBuilder.get(i));
       }
      return null;
	}
   public void parseStatement( int index,SQLiteStatement state,KeyValue kv){
	   Object value=kv.getValue();
	   if(value==null){
		   state.bindNull(index);
	   }else{
		   String type=kv.getType();
		   if(type.equals("java.lang.String")){
			   state.bindString(index,(String) value);
		   }else if(type.equals("int")){
			   int primaryValue=(Integer)value;
			   state.bindLong(index,primaryValue);
		   }
	   }
   }
}
