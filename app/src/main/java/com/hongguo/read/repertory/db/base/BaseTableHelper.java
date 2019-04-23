package com.hongguo.read.repertory.db.base;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by losg on 17-12-19.
 */

public abstract class BaseTableHelper {

    protected SQLiteDatabase mSQLiteDatabase;

    protected void createDatabaseIfNotExit(SQLiteDatabase db) {
        mSQLiteDatabase = db;
    }
}
