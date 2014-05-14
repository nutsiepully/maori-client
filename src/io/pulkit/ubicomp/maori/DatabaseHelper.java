package io.pulkit.ubicomp.maori;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 6;
    private static final String DB_NAME = "maori.db";
    private static final String TAG = "maori:db_helper:";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating DB");
        String dbString =
            "CREATE TABLE models ( " +
                "id integer primary key autoincrement, " +
                "name varchar(30), " +
                "version int, " +
                "active int, " +
                "payload blob " +
            ");";

        db.execSQL(dbString);
        db.setVersion(DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading DB");
        db.execSQL("DROP TABLE IF EXISTS models");
        onCreate(db);
    }
}
