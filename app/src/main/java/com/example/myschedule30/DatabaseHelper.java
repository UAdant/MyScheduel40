package com.example.myschedule30;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "items";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_IS_DONE = "is_done"; // Нове поле для позначення виконання завдання

    // SQL-запит для створення таблиці
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_IS_DONE + " INTEGER DEFAULT 0)"; // Значення за замовчуванням - невиконане завдання

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Логіка оновлення бази даних при зміні версії
        switch (oldVersion) {
            case 1:
                // Оновлення з версії 1 до версії 2
                db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_IS_DONE + " INTEGER DEFAULT 0");
                break;
            default:
                throw new IllegalStateException("Невідома версія бази даних: " + oldVersion);
        }
    }

    // Додавання нового елемента до бази даних
    public long addItem(String name, String type, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_DATE, date);
        return db.insert(TABLE_NAME, null, values);
    }

    // Отримання всіх елементів з бази даних
    public List<ScheduleItem> getAllItems() {
        List<ScheduleItem> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                int isDone = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_DONE)); // Отримання статусу виконання завдання
                boolean done = isDone == 1; // Перевірка, чи завдання виконане
                itemList.add(new ScheduleItem(id, name, type, date, done));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemList;
    }

    // Оновлення стану виконання завдання
    public void updateItemStatus(int itemId, boolean isDone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_DONE, isDone ? 1 : 0); // Збереження значення в базі даних (1 - виконане, 0 - невиконане)
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(itemId)});
    }

    // Видалення елемента з бази даних
    public void deleteItem(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(itemId)});
    }

    public List<ScheduleItem> getCompletedTasks() {
        List<ScheduleItem> completedTasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_TYPE,
                COLUMN_DATE,
                COLUMN_IS_DONE
        };
        String selection = COLUMN_IS_DONE + " = ?";
        String[] selectionArgs = {"1"}; // 1 означає завдання, які виконані
        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String itemType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE));
            String itemDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
            boolean isDone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_DONE)) == 1;
            ScheduleItem task = new ScheduleItem(id, itemName, itemType, itemDate, isDone);
            completedTasks.add(task);
        }
        cursor.close();
        return completedTasks;
    }

}
