package com.niamh.sailing3app.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.niamh.sailing3app.Utils.Config;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper;

    // All Static variables
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = Config.DATABASE_NAME;

    // Constructor
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if(databaseHelper==null){
            synchronized (DatabaseHelper.class) {
                if(databaseHelper==null)
                    databaseHelper = new DatabaseHelper(context);
            }
        }
        return databaseHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //create tables SQL execution
        String CREATE_SAFETY_TABLE = "CREATE TABLE " + Config.TABLE_SAFETY + "("
                + Config.COLUMN_SAFETY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_SAFETY_TYPE + " TEXT NOT NULL, "
                + Config.COLUMN_SAFETY_DESCRIPTION + " TEXT, "
                + Config.COLUMN_SAFETY_AVAILABLE + " TEXT, "
                + Config.COLUMN_SAFETY_AVAILUSER + " TEXT, "
                + Config.COLUMN_SAFETY_FAULT + " TEXT, "
                + Config.COLUMN_SAFETY_FAULTUSER + " TEXT, "
                + Config.COLUMN_SAFETY_FAULTDES + " TEXT, "
                + Config.COLUMN_SAFETY_IMAGE + " BLOB "
                + ")";

        db.execSQL(CREATE_SAFETY_TABLE);

        //Tag so i can find execution in my log code
        Log.d("***NIAMH_IS4447_DBH***","DB created!");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_SAFETY);

        // Create tables again
        onCreate(db);
    }

    //opening the database
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        //enable foreign key constraints like ON UPDATE CASCADE, ON DELETE CASCADE
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

}
