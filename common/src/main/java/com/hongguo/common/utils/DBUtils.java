package com.hongguo.common.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by losg on 17-12-19.
 */

public class DBUtils {

    /**
     * 查询数据库中是否存在该张表
     *
     * @param db
     * @param tableName
     * @return
     */
    public static boolean tableExits(SQLiteDatabase db, String tableName) {
        boolean exits = false;
        String sql = "select * from sqlite_master where name=" + "'" + tableName + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            exits = true;
        }
        cursor.close();
        return exits;
    }
}
