package com.hongguo.read.repertory.db.read;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hongguo.common.utils.FileUtils;
import com.hongguo.common.utils.TimeUtils;
import com.hongguo.common.utils.rxjava.RxJavaUtils;
import com.hongguo.common.utils.rxjava.SubscriberImp;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.repertory.db.DBBookReaderHelper;
import com.hongguo.read.repertory.db.DBFactory;
import com.hongguo.read.repertory.db.chapter.ChapterRepertory;
import com.hongguo.read.repertory.share.UserRepertory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by losg on 17-12-19.
 */

public class BookReaderRepertory extends DBBookReaderHelper {

    /**
     * 阅读历史最大存储数量
     */
    private static final int MAX_READ_BOOK_NUMBER = 100;

    public static List<BookReader> createDefaultBooks() {

        String currentTime = TimeUtils.parseTime(new Date());

        List<BookReader> bookShelves = new ArrayList<>();

        BookReader bookShelf = new BookReader();
        bookShelf.bookId = "200798";
        bookShelf.bookName = "美女如戏";
        bookShelf.bookType = 0;
        bookShelf.coverPicture = "http://i-1.hgread.com/2017/5/17/725b4a57-c032-40c7-9139-ae2aed8f3ef1.jpg";
        bookShelf.lastUpdateTime = currentTime;
        bookShelves.add(bookShelf);
        insertOrUpdateBookShelf(bookShelf);

        bookShelf = new BookReader();
        bookShelf.bookId = "200729";
        bookShelf.bookName = "桃运村支书";
        bookShelf.bookType = 0;
        bookShelf.coverPicture = "http://i-1.hgread.com/2017/5/18/aaf4882d-bc34-4ae3-b783-171802968f0c.jpg";
        bookShelf.lastUpdateTime = currentTime;
        bookShelves.add(bookShelf);
        insertOrUpdateBookShelf(bookShelf);

        bookShelf = new BookReader();
        bookShelf.bookId = "200759";
        bookShelf.bookName = "日久生情";
        bookShelf.bookType = 0;
        bookShelf.coverPicture = "http://i-1.hgread.com/2017/5/16/f3802d6a-e830-4af8-98e8-3cfdebb8c070.png";
        bookShelf.lastUpdateTime = currentTime;
        bookShelves.add(bookShelf);
        insertOrUpdateBookShelf(bookShelf);

        bookShelf = new BookReader();
        bookShelf.bookId = "200816";
        bookShelf.bookName = "豪门宠婚：总裁，求放过";
        bookShelf.bookType = 0;
        bookShelf.coverPicture = "http://i-1.hgread.com/2017/5/19/85fa4379-ede6-486b-9bf6-bc46dc00bfe1.jpg";
        bookShelf.lastUpdateTime = currentTime;
        bookShelves.add(bookShelf);
        insertOrUpdateBookShelf(bookShelf);

        return bookShelves;
    }

    public static List<BookReader> queryAllBookShelf() {
        return queryReaders(true);
    }


    public static BookReader queryBookById(String bookid, int bookType) {
        SQLiteDatabase db = DBFactory.getDB();
        Cursor cursor = db.query(TB_NAME, null, COL_BOOK_ID + "=? and " + COL_BOOK_TYPE + "=?", new String[]{bookid, bookType + ""}, null, null, COL_READ_TIME + " desc");
        List<BookReader> bookShelves = cursorParse(cursor);
        cursor.close();
        return bookShelves.size() == 0 ? null : bookShelves.get(0);
    }

    public static BookReader queryBookShelfById(String bookid, String bookType) {
        SQLiteDatabase db = DBFactory.getDB();
        Cursor cursor = db.query(TB_NAME, null, COL_BOOK_ID + "=? and " + COL_BOOK_TYPE + "=? and " + COL_IS_IN_SHELF + "=1", new String[]{bookid, bookType + ""}, null, null, COL_READ_TIME + " desc");
        List<BookReader> bookShelves = cursorParse(cursor);
        cursor.close();
        return bookShelves.size() == 0 ? null : bookShelves.get(0);
    }

    public static List<BookReader> queryReaders(boolean isShelf) {
        SQLiteDatabase db = DBFactory.getDB();
        Cursor cursor = db.query(TB_NAME, null, COL_IS_IN_SHELF + "=?", new String[]{isShelf ? "1" : "0"}, null, null, COL_READ_TIME + " desc");
        List<BookReader> bookShelves = cursorParse(cursor);
        cursor.close();
        return bookShelves;
    }

    /******************************插入 start***************************/
    public static void synInsertBook(BookReader bookShelf) {
        List<BookReader> bookShelves = new ArrayList<>();
        bookShelves.add(bookShelf);
        synInsertBook(bookShelves);
    }

    public static void synInsertBook(List<BookReader> bookShelves) {
        Observable.create((ObservableOnSubscribe<Boolean>) subscriber -> {
            for (BookReader bookShelf : bookShelves) {
                BookReaderRepertory.insertOrUpdateBookShelf(bookShelf);
            }
            subscriber.onNext(true);
        }).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<>());
    }

    public static void insertOrUpdateBookShelf(BookReader bookShelf) {
        insertOrUpdateBookShelf(bookShelf, null);
    }

    public static void insertOrUpdateBookShelf(BookReader bookShelf, Date date) {
        insertOrUpdateReader(bookShelf, date, true);
    }

    public static void insertOrUpdateReader(BookReader bookShelf) {
        insertOrUpdateReader(bookShelf, null, false);
    }

    public static void insertOrUpdateReader(BookReader bookShelf, Date date, boolean isShelf) {
        BookReader bookShelves = queryBookById(bookShelf.bookId, bookShelf.bookType);

        try {
            SQLiteDatabase db = DBFactory.getDB();
            //不存在添加
            ContentValues values = new ContentValues();
            values.put(COL_BOOK_ID, bookShelf.bookId);
            values.put(COL_BOOK_COVER, bookShelf.coverPicture);
            values.put(COL_BOOK_NAME, bookShelf.bookName);
            values.put(COL_READ_CHAPTER_ID, bookShelf.readChapterId);
            values.put(COL_BOOK_TYPE, bookShelf.bookType);
            values.put(COL_READ_PAGE, bookShelf.readPage);
            if (bookShelves != null && bookShelves.isInBookShelf) {
                isShelf = true;
            }
            values.put(COL_IS_IN_SHELF, isShelf ? 1 : 0);
            values.put(COL_READ_PROGRESS, bookShelf.readProgress);

            bookShelf.lastUpdateTime = TimeUtils.parseTime(new Date());
            values.put(COL_LAST_UPDATE_TIME, bookShelf.lastUpdateTime);
            values.put(COL_READ_TIME, date == null ? TimeUtils.parseTime(new Date()) : TimeUtils.parseTime(date));

            //不是书架
            if (!isShelf) {
                //先查询
                //查询到数据
                if (bookShelves != null) {
                    db.update(TB_NAME, values, COL_BOOK_ID + "=?", new String[]{bookShelf.bookId});
                    return;
                } else {
                    //未查询到数据,查询阅读记录
                    List<BookReader> bookReaders = queryReaders(false);
                    if (bookReaders.size() > MAX_READ_BOOK_NUMBER) {
                        //超出范围，删除最后一条数据
                        BookReader bookReader = bookReaders.get(bookReaders.size() - 1);
                        ChapterRepertory.deleteChatper(bookReader.bookId, bookReader.bookType);
                        deleteReader(bookReader);
                    }
                }
            }
            if (bookShelves != null) {
                db.update(TB_NAME, values, COL_BOOK_ID + "=? and " + COL_BOOK_TYPE + "=?", new String[]{bookShelf.bookId, bookShelf.bookType + ""});
            } else {
                db.insert(TB_NAME, null, values);
            }
        } catch (Exception e) {

        }
    }
    /******************************插入 end**************************/

    /*****************************删除 start************************/
    public static void synDeleteBook(BookReader bookShelf) {
        List<BookReader> bookShelves = new ArrayList<>();
        bookShelves.add(bookShelf);
        synDeleteBooks(bookShelves);
    }

    public static void synDeleteBooks(List<BookReader> bookShelves) {
        Observable.create((ObservableOnSubscribe<Boolean>) subscriber -> {
            for (BookReader bookShelf : bookShelves) {
                BookReaderRepertory.deleteReader(bookShelf);
            }
            subscriber.onNext(true);
        }).compose(RxJavaUtils.androidTranformer()).subscribe(new SubscriberImp<>());
    }

    public static void deleteReader(BookReader bookShelf) {
        SQLiteDatabase db = DBFactory.getDB();
        db.delete(TB_NAME, COL_BOOK_ID + " =? and " + COL_BOOK_TYPE + "=?", new String[]{bookShelf.bookId, bookShelf.bookType + ""});
        //删除章节信息
        ChapterRepertory.deleteChatper(bookShelf.bookId, bookShelf.bookType);
        //删除本地书籍缓存
        FileUtils.deleteDir(new File(FileManager.getBookDownPath(UserRepertory.getUserID(), bookShelf.bookType, bookShelf.bookId)));
        //删除描述信息信息
        FileUtils.deleteDir(new File(FileManager.getBookDownDescribeDirPath(bookShelf.bookType, bookShelf.bookId)));

    }

    /*****************************删除 end************************/

    private static List<BookReader> cursorParse(Cursor cursor) {
        List<BookReader> bookShelves = new ArrayList<>();
        while (cursor.moveToNext()) {
            BookReader bookShelf = new BookReader();
            bookShelf.bookId = cursor.getString(cursor.getColumnIndex(COL_BOOK_ID));
            bookShelf.coverPicture = cursor.getString(cursor.getColumnIndex(COL_BOOK_COVER));
            bookShelf.bookName = cursor.getString(cursor.getColumnIndex(COL_BOOK_NAME));
            bookShelf.readChapterId = cursor.getString(cursor.getColumnIndex(COL_READ_CHAPTER_ID));
            bookShelf.bookType = cursor.getInt(cursor.getColumnIndex(COL_BOOK_TYPE));
            bookShelf.readPage = cursor.getInt(cursor.getColumnIndex(COL_READ_PAGE));
            bookShelf.isInBookShelf = cursor.getInt(cursor.getColumnIndex(COL_IS_IN_SHELF)) == 1;
            bookShelf.lastUpdateTime = cursor.getString(cursor.getColumnIndex(COL_LAST_UPDATE_TIME));
            bookShelf.readProgress = cursor.getFloat(cursor.getColumnIndex(COL_READ_PROGRESS));
            bookShelves.add(bookShelf);
        }
        return bookShelves;
    }

}
