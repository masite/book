package com.hongguo.read.repertory.db.chapter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.hongguo.common.utils.TimeUtils;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.mvp.model.book.chapter.ChapterBean;
import com.hongguo.read.repertory.db.DBChapterfHelper;
import com.hongguo.read.repertory.db.DBFactory;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.common.utils.FileUtils;
import com.hongguo.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by losg on 17-12-19.
 */

public class ChapterRepertory extends DBChapterfHelper {

    /**
     * 获取章节信息
     *
     * @param bookid
     * @param bookType
     * @return
     */
    public static List<Chapter> queryAllChapterByBookId(String bookid, int bookType) {
        SQLiteDatabase db = DBFactory.getDB();
        Cursor cursor = db.query(TB_NAME, null, COL_BOOK_ID + "= ? and " + COL_BOOK_TYPE + "=?", new String[]{bookid, bookType + ""}, null, null, COL_ID + " asc");
        List<Chapter> chapters = cursorParse(cursor);
        cursor.close();
        return chapters;
    }

    public static List<Chapter> queryAllPay(String bookid, int bookType) {
        SQLiteDatabase db = DBFactory.getDB();
        Cursor cursor = db.query(TB_NAME, null, COL_BOOK_ID + "= ? and " + COL_BOOK_TYPE + "=? and (" + COL_COIN + "<>0 and " + COL_IS_BUY + "=0)", new String[]{bookid, bookType + ""}, null, null, COL_ID + " asc");
        List<Chapter> chapters = cursorParse(cursor);
        cursor.close();
        return chapters;
    }

    public static List<Chapter> queryAllFree(String bookid, int bookType) {
        SQLiteDatabase db = DBFactory.getDB();
        Cursor cursor = db.query(TB_NAME, null, COL_BOOK_ID + "= ? and " + COL_BOOK_TYPE + "=? and (" + COL_COIN + "=0 or " + COL_IS_BUY + "=1)", new String[]{bookid, bookType + ""}, null, null, COL_ID + " asc");
        List<Chapter> chapters = cursorParse(cursor);
        cursor.close();
        return chapters;
    }

    public static List<Chapter> queryPayChapterFromChapter(String bookid, int bookType, String chapterId) {
        SQLiteDatabase db = DBFactory.getDB();
        String queryID = "select " + COL_ID + " from " + TB_NAME + " where " + COL_BOOK_ID + "=" + bookid + " and " + COL_BOOK_TYPE + "=" + bookType + " and " + COL_CHAPTER_ID + "=" + chapterId;
        Cursor cursor = db.query(TB_NAME, null, COL_BOOK_ID + "= ? and " + COL_BOOK_TYPE + "=? and " + COL_ID + " >= ( " + queryID + " ) and (" + COL_COIN + "<>0 and " + COL_IS_BUY + "=0)", new String[]{bookid, bookType + ""}, null, null, COL_ID + " asc");
        List<Chapter> chapters = cursorParse(cursor);
        cursor.close();
        return chapters;
    }

    public static void updateChapterTime(String bookid, int bookType, String time) {
        if (!TextUtils.isEmpty(time)) {
            if (time.startsWith("/Dat")) {
                time = TimeUtils.getCSharpTime(time);
            }
        }
        SQLiteDatabase db = DBFactory.getDB();
        ContentValues values = new ContentValues();
        values.put(COL_CHAPTER_UPDATE_TIME, time);
        db.update(TB_NAME, values, COL_BOOK_ID + "= ? and " + COL_BOOK_TYPE + "=?", new String[]{bookid, bookType + ""});

    }

    public static ChapterTotalInfo queryFree(String bookid, int bookType) {
        SQLiteDatabase db = DBFactory.getDB();
        ChapterTotalInfo chapterTotalInfo = new ChapterTotalInfo();
        Cursor cursor = db.query(TB_NAME, new String[]{"sum(" + COL_COIN + ") as total, count(*) as number"}, COL_BOOK_ID + "= ? and " + COL_BOOK_TYPE + "=? and (" + COL_COIN + "=0 or " + COL_IS_BUY + "=1)", new String[]{bookid, bookType + ""}, null, null, COL_ID + " asc");
        while (cursor.moveToNext()) {
            chapterTotalInfo.totalNumber = cursor.getInt(1);
            chapterTotalInfo.totalPrice = cursor.getInt(0);
        }
        cursor.close();
        return chapterTotalInfo;
    }

    public static ChapterTotalInfo queryPay(String bookid, int bookType) {
        SQLiteDatabase db = DBFactory.getDB();
        ChapterTotalInfo chapterTotalInfo = new ChapterTotalInfo();
        Cursor cursor = db.query(TB_NAME, new String[]{"sum(" + COL_COIN + ") as total, count(*) as number"}, COL_BOOK_ID + "= ? and " + COL_BOOK_TYPE + "=? and (" + COL_COIN + "<>0 and " + COL_IS_BUY + "=0)", new String[]{bookid, bookType + ""}, null, null, COL_ID + " asc");
        while (cursor.moveToNext()) {
            chapterTotalInfo.totalNumber = cursor.getInt(1);
            chapterTotalInfo.totalPrice = cursor.getInt(0);
        }
        cursor.close();
        return chapterTotalInfo;
    }

    public static void setChapterLimitType(String bookid, int bookType, int limitType) {
        SQLiteDatabase db = DBFactory.getDB();
        ContentValues values = new ContentValues();
        values.put(COL_CHAPTER_LIMIT_TYPE, limitType);
        db.update(TB_NAME, values, COL_BOOK_ID + "= ? and " + COL_BOOK_TYPE + "=?", new String[]{bookid, bookType + ""});
    }

    public static void setChapterIsBuy(String bookid, String chapterId, int bookType) {
        SQLiteDatabase db = DBFactory.getDB();
        ContentValues values = new ContentValues();
        values.put(COL_IS_BUY, 1);
        db.update(TB_NAME, values, COL_BOOK_ID + "= ? and " + COL_BOOK_TYPE + "=? and " + COL_CHAPTER_ID + "=?", new String[]{bookid, bookType + "", chapterId});
    }

    /**
     * 插入章节
     *
     * @param chapters
     * @param bookid
     * @param bookType
     */
    public static void insertChapters(List<Chapter> chapters, String bookid, int bookType) {
        deleteChatper(bookid, bookType);

        SQLiteDatabase db = DBFactory.getDB();
        db.beginTransaction();
        for (Chapter chapter : chapters) {
            try {
                ContentValues values = new ContentValues();
                values.put(COL_CHAPTER_ID, chapter.chapterId);
                values.put(COL_CHAPTER_NAME, chapter.chapterName);
                values.put(COL_IS_BUY, chapter.isbuy ? 1 : 0);
                values.put(COL_COIN, chapter.coin);
                values.put(COL_CHAPTER_UPDATE_TIME, chapter.updateTime);
                values.put(COL_BOOK_ID, bookid);
                values.put(COL_CHAPTER_LIMIT_TYPE, chapter.limitType);
                values.put(COL_BOOK_TYPE, bookType);
                db.insert(TB_NAME, null, values);
            } catch (Exception e) {
                LogUtils.log("insertChapters" + e);
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    public static void deleteChatper(String bookid, int bookType) {
        SQLiteDatabase db = DBFactory.getDB();
        db.delete(TB_NAME, COL_BOOK_ID + "= ? and " + COL_BOOK_TYPE + "=?", new String[]{bookid, bookType + ""});
    }

    private static List<Chapter> cursorParse(Cursor cursor) {
        List<Chapter> chapters = new ArrayList<>();
        while (cursor.moveToNext()) {
            Chapter chapter = new Chapter();
            chapter.id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            chapter.chapterId = cursor.getString(cursor.getColumnIndex(COL_CHAPTER_ID));
            chapter.chapterName = cursor.getString(cursor.getColumnIndex(COL_CHAPTER_NAME));
            chapter.updateTime = cursor.getString(cursor.getColumnIndex(COL_CHAPTER_UPDATE_TIME));
            chapter.isbuy = cursor.getInt(cursor.getColumnIndex(COL_IS_BUY)) == 1;
            chapter.coin = cursor.getInt(cursor.getColumnIndex(COL_COIN));
            chapter.limitType = cursor.getInt(cursor.getColumnIndex(COL_CHAPTER_LIMIT_TYPE));
            chapters.add(chapter);
        }
        return chapters;
    }


    public static List<ChapterBean.Chapters> parse(List<Chapter> chapters, String bookid, int bookType) {
        List<ChapterBean.Chapters> chaptersList = new ArrayList<>();
        for (Chapter chapter : chapters) {
            ChapterBean.Chapters chapters1 = new ChapterBean.Chapters();
            chapters1.chapterId = chapter.chapterId;
            chapters1.chapterName = chapter.chapterName;
            chapters1.coin = chapter.coin;
            chapters1.updateTime = chapter.updateTime;
            chapters1.isBuy = chapter.isbuy ? 1 : 0;
            chapters1.limitType = chapter.limitType;
            chapters1.hasDownTotal = FileUtils.fileExist(FileManager.getBookChapterDownPath(UserRepertory.getUserID(), bookType, bookid, chapter.chapterId));
            chaptersList.add(chapters1);
        }
        return chaptersList;
    }

    public static class ChapterTotalInfo {
        public int totalNumber;
        public int totalPrice;
    }

}
