package com.hongguo.read.repertory.db;

import android.database.sqlite.SQLiteDatabase;

import com.hongguo.common.utils.DBUtils;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.repertory.db.base.BaseTableHelper;

/**
 * Created by losg on 17-12-19.
 */

public class DBChapterfHelper extends BaseTableHelper {

    public static final    String TB_NAME                 = "book_chapter";
    public static final String COL_ID                  = "id";
    public static final String COL_BOOK_ID             = "book_id";
    public static final String COL_BOOK_TYPE           = "book_type";
    public static final String COL_CHAPTER_ID          = "chapter_id";
    public static final String COL_CHAPTER_LIMIT_TYPE  = "chapter_limit_type";
    public static final String COL_CHAPTER_NAME        = "chapter_name";
    public static final String COL_CHAPTER_PAGE        = "chapter_page";
    public static final String COL_CHAPTER_UPDATE_TIME = "chapter_update_time";
    public static final String COL_IS_BUY              = "col_is_buy";
    public static final String COL_COIN                = "chapter_coin";

    protected void createDatabaseIfNotExit(SQLiteDatabase db) {
        if (DBUtils.tableExits(db, TB_NAME)) return;
        createDateBase(db);
    }

    private static void createDateBase(SQLiteDatabase db) {
        String sql = "create table " + TB_NAME + " ( " +
                COL_ID + " integer primary key autoincrement, " +
                COL_BOOK_ID + " varchar(32)  not null, " +
                COL_BOOK_TYPE + " integer default 0, " +
                COL_CHAPTER_LIMIT_TYPE + " integer default "+ Constants.BOOK_FREE_TYPE.BOOK_LIMIT_FREE+", " +
                COL_CHAPTER_ID + " varchar(32)  not null, " +
                COL_CHAPTER_NAME + " varchar(100) not null, " +
                COL_CHAPTER_UPDATE_TIME + " varchar(100), " +
                COL_CHAPTER_PAGE + " integer default 1, " +
                COL_IS_BUY + " integer default 0, " +
                COL_COIN + " integer default 0, " +
                "unique(" + COL_BOOK_ID + "," + COL_BOOK_TYPE + "," + COL_CHAPTER_ID + ")" +
                " )";
        db.execSQL(sql);
    }

}
