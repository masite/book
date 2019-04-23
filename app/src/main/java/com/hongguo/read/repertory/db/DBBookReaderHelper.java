package com.hongguo.read.repertory.db;

import android.database.sqlite.SQLiteDatabase;

import com.hongguo.common.utils.DBUtils;
import com.hongguo.read.repertory.db.base.BaseTableHelper;

/**
 * Created by losg on 17-12-19.
 */

public class DBBookReaderHelper extends BaseTableHelper {

    public static final    String TB_NAME              = "book_reader";
    protected static final String COL_BOOK_ID          = "book_id";
    protected static final String COL_BOOK_NAME        = "book_name";
    protected static final String COL_BOOK_COVER       = "book_cover";
    protected static final String COL_BOOK_TYPE        = "book_type";
    protected static final String COL_READ_PROGRESS    = "read_progress";
    protected static final String COL_READ_CHAPTER_ID  = "read_chapter_id";
    protected static final String COL_READ_PAGE        = "read_page";
    protected static final String COL_READ_TIME        = "read_time";
    protected static final String COL_IS_IN_SHELF      = "is_in_shelf";
    protected static final String COL_LAST_UPDATE_TIME = "last_update_time";

    protected void createDatabaseIfNotExit(SQLiteDatabase db) {
        if (DBUtils.tableExits(db, TB_NAME)) return;
        createDateBase(db);
    }

    public static void createDateBase(SQLiteDatabase db) {
        String sql = "create table " + TB_NAME + " ( " +
                COL_BOOK_ID + " varchar(32)  primary key, " +
                COL_BOOK_NAME + " varchar(100) not null, " +
                COL_BOOK_COVER + " varchar(200) not null, " +
                COL_BOOK_TYPE + " integer default 0, " +
                COL_IS_IN_SHELF + " integer default 0, " +
                COL_READ_PROGRESS + " float default 0, " +
                COL_READ_CHAPTER_ID + " varchar(32) default '-1'," +
                COL_READ_PAGE + " integer default 0," +
                COL_READ_TIME + " varchar(32)," +
                COL_LAST_UPDATE_TIME + " varchar(32)" +
                ")";
        db.execSQL(sql);
    }

}
