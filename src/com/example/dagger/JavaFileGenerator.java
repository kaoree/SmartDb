package com.example.dagger;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

public class JavaFileGenerator {
	private HashMap<String, BindingClass> targetClassMap = new LinkedHashMap<String, BindingClass>();

	public JavaFileGenerator() {

	}

	public String getFunc() {
		return "com.example.dagger.TableCreator";
	}

	public StringBuilder brewJava(HashMap<String, BindingClass> bindMap) {
		targetClassMap = bindMap;
		// File dir = new File("C://" + bindMap.size() + "df.txt");
		// if (!dir.exists()) {
		// dir.mkdir();
		// }
		StringBuilder builder = new StringBuilder();
		builder.append("// Generated code from Butter Knife. Do not modify!\n");
		builder.append("package com.example.dagger ").append(";\n\n");
		builder.append("import android.content.Context;\n");
		builder.append("import android.database.sqlite.SQLiteDatabase;\n");
		builder.append("import android.database.sqlite.SQLiteDatabase.CursorFactory;\n");
		builder.append("import android.database.sqlite.SQLiteDatabase;\n");
		builder.append("import com.dagger.interfaces.ITableCreator;\n");
		builder.append('\n');
		builder.append("public class TableCreator implements ITableCreator ");
		builder.append(" {\n");
		emitConstructMethod(builder);
		emitCreateMethod(builder);
		emitUpgradeMethod(builder);
		builder.append("}\n");
		return builder;
	}

	public void emitConstructMethod(StringBuilder builder) {
		builder.append("public  TableCreator() {\n\n");
		builder.append("}\n");
	}

	public void emitCreateMethod(StringBuilder builder) {
		builder.append("  @Override\n").append(
				"public void onCreate(SQLiteDatabase db) {\n");
		for (Map.Entry<String, BindingClass> binding : targetClassMap
				.entrySet()) {
			BindingClass bindingClass = binding.getValue();
			builder.append("db.execSQL(");
			builder.append('"');
			builder.append(bindingClass.brewJava().toString());
			builder.append('"');
			builder.append(");\n");
		}
		builder.append("}\n");
	}

	public void emitUpgradeMethod(StringBuilder builder) {
		builder.append("  @Override\n").append(
				"public void onUpgrade(SQLiteDatabase db) {\n");
		builder.append("}\n");
	}
}
