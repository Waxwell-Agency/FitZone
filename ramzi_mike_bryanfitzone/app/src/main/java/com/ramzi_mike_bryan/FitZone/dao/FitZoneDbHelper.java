package com.ramzi_mike_bryan.FitZone.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FitZoneDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "fitzone_local.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_QUIZ_RESULTS = "quiz_results";
    public static final String COL_ID = "id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_QUIZ_ID = "quiz_id";
    public static final String COL_SCORE = "score";
    public static final String COL_TOTAL = "total";
    public static final String COL_DATE = "date";

    private static FitZoneDbHelper instance;

    public static synchronized FitZoneDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new FitZoneDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    private FitZoneDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_QUIZ_RESULTS + " ("
                        + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COL_USER_ID + " TEXT NOT NULL, "
                        + COL_QUIZ_ID + " TEXT NOT NULL, "
                        + COL_SCORE + " INTEGER NOT NULL, "
                        + COL_TOTAL + " INTEGER NOT NULL, "
                        + COL_DATE + " TEXT NOT NULL, "
                        + "UNIQUE(" + COL_USER_ID + ", " + COL_QUIZ_ID + ") ON CONFLICT REPLACE"
                        + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_RESULTS);
        onCreate(db);
    }
}
