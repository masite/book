package com.hongguo.read.repertory.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.hongguo.common.utils.DBUtils;
import com.hongguo.read.repertory.db.base.BaseTableHelper;

/**
 * Created by losg on 2018/2/23.
 */

public class DBVersionHelper extends BaseTableHelper {

    protected static final int CURRENT_VERSION_CODE = 1;

    public static final    String TB_NAME          = "version";
    protected static final String COL_ID           = "id";
    protected static final String COL_VERSION_CODE = "version_code";

    protected void createDatabaseIfNotExit(SQLiteDatabase db) {
        if (DBUtils.tableExits(db, TB_NAME)) return;
        createDateBase(db);
    }

    private static void createDateBase(SQLiteDatabase db) {
        String sql = "create table " + TB_NAME + " ( " +
                COL_ID + " integer primary key autoincrement, " +
                COL_VERSION_CODE + " integer default 0" +
                ")";
        db.execSQL(sql);

        int currentVersion = 0;
        if (!DBUtils.tableExits(db, DBChapterfHelper.TB_NAME)) {
            currentVersion = CURRENT_VERSION_CODE;
        }

        //默认版本 0
        ContentValues values = new ContentValues();
        values.put(COL_VERSION_CODE, currentVersion);
        db.insert(TB_NAME, null, values);
        values = new ContentValues();
        values.put(COL_VERSION_CODE, currentVersion);
        db.insert(TB_NAME, null, values);
    }
}
