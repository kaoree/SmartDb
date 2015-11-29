package com.dagger.interfaces;

import android.database.sqlite.SQLiteDatabase;

public interface ITableCreator {
    public void onCreate(SQLiteDatabase db);
    public void onUpgrade(SQLiteDatabase db);
}
