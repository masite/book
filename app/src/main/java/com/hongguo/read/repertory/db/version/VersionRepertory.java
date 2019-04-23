package com.hongguo.read.repertory.db.version;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hongguo.read.constants.Constants;
import com.hongguo.read.repertory.db.DBFactory;
import com.hongguo.read.repertory.db.DBVersionHelper;
import com.hongguo.read.repertory.db.chapter.ChapterRepertory;
import com.hongguo.read.repertory.db.read.BookReaderRepertory;
import com.hongguo.common.utils.rxjava.RxJavaUtils;

/**
 * Created by losg on 2018/2/23.
 */

public class VersionRepertory extends DBVersionHelper {

    public static int dbVersion() {
        SQLiteDatabase db = DBFactory.getDB();
        Cursor query = db.query(TB_NAME, new String[]{COL_VERSION_CODE}, null, null, null, null, null);
        int versionCode = -1;
        while (query.moveToNext()) {
            versionCode = query.getInt(query.getColumnIndex(COL_VERSION_CODE));
        }
        query.close();
        return versionCode;
    }

    public static void Update(DBFactory.UpdateDBListener updateDBListener) {
        int i = VersionRepertory.dbVersion();

        switch (i) {
            case 0:
                updateOne(updateDBListener);
        }
    }

    /**
     * 版本 1 修改进度的类型 sqlite 不支持修改字段类型，直接替换,针对  book_reader 表修改
     * 章节列表添加限免类型(限免的话，每次刷新章节列表，防止收费后出现问题(主要针对百度书籍))
     *
     * @param updateDBListener
     */
    private static void updateOne(DBFactory.UpdateDBListener updateDBListener) {
        updateDBListener.startUpdate();
        RxJavaUtils.backgroundDeal(new RxJavaUtils.BackgroundDealListener() {
            @Override
            public void run() {
                SQLiteDatabase db = DBFactory.getDB();
                String changeDb = "alter table " + BookReaderRepertory.TB_NAME + " rename to " + "temp_" + BookReaderRepertory.TB_NAME;
                db.execSQL(changeDb);
                BookReaderRepertory.createDateBase(db);
                String insertSql = "insert into " + BookReaderRepertory.TB_NAME + " select * from " + "temp_" + BookReaderRepertory.TB_NAME;
                db.execSQL(insertSql);
                String dropSql = "drop table " + "temp_" + BookReaderRepertory.TB_NAME;
                db.execSQL(dropSql);
                ContentValues values = new ContentValues();
                values.put(COL_VERSION_CODE, 1);

                db.execSQL("alter table " + ChapterRepertory.TB_NAME + " add column " + ChapterRepertory.COL_CHAPTER_LIMIT_TYPE + " integer default " + Constants.BOOK_FREE_TYPE.BOOK_LIMIT_FREE);

                db.update(TB_NAME, values, null, null);
            }

            @Override
            public void runSuccess() {
                super.runSuccess();
                updateDBListener.updateFinish();
            }
        });
    }
}
