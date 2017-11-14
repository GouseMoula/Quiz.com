package com.quizcom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by compindia-fujitsu on 25-10-2017.
 */

public class MyDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dat";
    final String TABLE_NAME = "tab";
    public static final int DB_VERSION = 1;
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_Q = "question";
    public static final String COLUMN_OPT1 = "opt1";
    public static final String COLUMN_OPT2 = "opt2";
    public static final String COLUMN_OPT3 = "opt3";
    public static final String COLUMN_OPT4 = "opt4";
    public static final String COLUMN_ANS = "ans";

    public MyDB(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER," +
                COLUMN_Q + " varchar," + COLUMN_ANS + " INTEGER," + COLUMN_OPT1 + " varchar," + COLUMN_OPT2 + " varchar," + COLUMN_OPT3 +
                " varchar," + COLUMN_OPT4 + " varchar)";
        db.execSQL(CREATE_TABLE);
        Log.e("MyData", "User Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean addNewQuestion(int qNumber, String question, int answer, String opt1, String opt2, String opt3, String opt4) {
        if (qNumber > 0 && qNumber <= 5) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_ID, qNumber);
            contentValues.put(COLUMN_Q, question);
            contentValues.put(COLUMN_ANS, answer);
            contentValues.put(COLUMN_OPT1, opt1);
            contentValues.put(COLUMN_OPT2, opt2);
            contentValues.put(COLUMN_OPT3, opt3);
            contentValues.put(COLUMN_OPT4, opt4);
            long l = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            sqLiteDatabase.close();
            if (l != -1) {
                Log.e("MyData", "Question added successfully");
                return true;
            }
        }
        return false;
    }

    public int getTotalQuestions() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor2 = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);
        return cursor2.getCount();
    }

    public Cursor getNextQuestion(int qNumber) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] from = new String[]{COLUMN_ID, COLUMN_Q, COLUMN_ANS, COLUMN_OPT1, COLUMN_OPT2, COLUMN_OPT3, COLUMN_OPT4};
        String where = COLUMN_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(qNumber)};
        return sqLiteDatabase.query(TABLE_NAME, from, where, whereArgs, null, null, null);
    }

    public boolean deleteQuiz() {
        if (getTotalQuestions() > 0) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.execSQL("delete from " + TABLE_NAME);
            sqLiteDatabase.close();
            return true;
        }
        return false;
    }
}
