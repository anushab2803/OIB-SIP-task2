package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todoApp.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_TASKS = "tasks";

    private static final String USER_ID = "id";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static final String TASK_ID = "id";
    private static final String TASK_TITLE = "title";
    private static final String TASK_DESCRIPTION = "description";
    private static final String USER_ID_FK = "user_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERNAME + " TEXT, " +
                PASSWORD + " TEXT)";
        String createTasksTable = "CREATE TABLE " + TABLE_TASKS + " (" +
                TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TASK_TITLE + " TEXT, " +
                TASK_DESCRIPTION + " TEXT, " +
                USER_ID_FK + " INTEGER, " +
                "FOREIGN KEY(" + USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + USER_ID + "))";

        db.execSQL(createUsersTable);
        db.execSQL(createTasksTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{USER_ID}, USERNAME + "=? AND " + PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME, username);
        values.put(PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public Cursor getTasks(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TASKS, null, USER_ID_FK + "=?", new String[]{String.valueOf(userId)},
                null, null, null);
    }

    public boolean addTask(String title, String description, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TASK_TITLE, title);
        values.put(TASK_DESCRIPTION, description);
        values.put(USER_ID_FK, userId);
        long result = db.insert(TABLE_TASKS, null, values);
        return result != -1;
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, TASK_ID + "=?", new String[]{String.valueOf(taskId)});
    }
}
