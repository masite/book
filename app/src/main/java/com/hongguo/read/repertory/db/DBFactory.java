package com.hongguo.read.repertory.db;

import android.database.sqlite.SQLiteDatabase;

import com.hongguo.read.repertory.db.version.VersionRepertory;

import java.io.File;

/**
 * Created by losg on 17-12-19.
 */

public class DBFactory {

    private static DBFactory      sDBFactory;
    private static SQLiteDatabase sSqLiteDatabase;

    public static DBFactory getInstance() {
        synchronized (DBFactory.class) {
            if (sDBFactory == null) {
                sDBFactory = new DBFactory();
            }
        }
        return sDBFactory;
    }

    public void loadDb(String path, UpdateDBListener updateDBListener) {
        if (sSqLiteDatabase != null) {
            sSqLiteDatabase.close();
        }
        sSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(new File(path), null);
        initDatabase(updateDBListener);
    }

    public static SQLiteDatabase getDB() {
        return sSqLiteDatabase;
    }

    private void initDatabase(UpdateDBListener updateDBListener) {

        new DBVersionHelper().createDatabaseIfNotExit(sSqLiteDatabase);
        new DBBookReaderHelper().createDatabaseIfNotExit(sSqLiteDatabase);
        new DBChapterfHelper().createDatabaseIfNotExit(sSqLiteDatabase);
        VersionRepertory.Update(updateDBListener);
    }

    public interface UpdateDBListener{

        void startUpdate();
        void updateFinish();
    }

}
