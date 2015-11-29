package com.dagger.util;

import java.io.File;

import com.dagger.interfaces.ITableCreator;
import com.example.dagger.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DBHelper {
	private static DBHelper helper;
	private SQLiteDatabase mDatabase = null;
	String mName = "db";
	private int newVersion;
	private Context mContext;

	public static DBHelper Instance() {
		if (helper == null) {
			helper = new DBHelper();
		}
		return helper;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

	public void setVersion(int version) {
		this.newVersion = version;
	}

	public SQLiteDatabase open(String name) {
		setDbName(name);
		return open();
	}

	public void setDbName(String name) {
		this.mName = name;
	}

	public SQLiteDatabase open() {
		if (mDatabase != null && mDatabase.isOpen()) {

			return mDatabase;
		}
		SQLiteDatabase db = null;
		if (mName == null) {
			db = SQLiteDatabase.create(null);
		} else {
			String path = getDatabasePath(mName).getPath();
			db = SQLiteDatabase.openOrCreateDatabase(path, null);
		}
		int version = db.getVersion();
		ITableCreator creator=this.getTableCreator();
		if (version != newVersion) {
			try {
				db.beginTransaction();
				if (version == 0) {
					creator.onCreate(db);
				} else {
					creator.onUpgrade(db);
				}
				db.setVersion(newVersion);
				db.setTransactionSuccessful();
			} finally {
				// TODO Auto-generated catch block
				db.endTransaction();
			}
		}
		return db;
	}

	public ITableCreator getTableCreator() {
		try {
			Class<?> objClass = Class
					.forName("com.example.dagger.TableCreator");
			ITableCreator creator = (ITableCreator) objClass.newInstance();
			return creator;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public File getDatabasePath(String name) {
		String EXTERN_PATH = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) == true) {
			String dbPath = "/dagger/database/";
			EXTERN_PATH = android.os.Environment.getExternalStorageDirectory()
					.getAbsolutePath() + dbPath;
			File f = new File(EXTERN_PATH);
			if (!f.exists()) {
				f.mkdirs();
			}
		}
		return new File(EXTERN_PATH + name);
	}

}
