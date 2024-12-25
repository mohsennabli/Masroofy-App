package com.example.masroofy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database constants
    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 2;

    // Expenses table
    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";

    // Categories table
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CATEGORY_NAME = "name";

    // SQL to create expenses table
    private static final String CREATE_TABLE_EXPENSES = "CREATE TABLE " +
            TABLE_EXPENSES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CATEGORY + " TEXT NOT NULL, " +
            COLUMN_AMOUNT + " REAL NOT NULL, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_DATE + " INTEGER)";

    // SQL to create categories table
    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " +
            TABLE_CATEGORIES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CATEGORY_NAME + " TEXT NOT NULL UNIQUE)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EXPENSES);
        db.execSQL(CREATE_TABLE_CATEGORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    // Insert a new category
    public long insertCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, categoryName);
        return db.insert(TABLE_CATEGORIES, null, values);
    }

    // Fetch all categories
    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORIES, null);
    }

    // Fetch expenses grouped by category
    public Cursor getExpensesByCategory() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_CATEGORY + ", SUM(" + COLUMN_AMOUNT + ") AS total " +
                "FROM " + TABLE_EXPENSES +
                " GROUP BY " + COLUMN_CATEGORY;
        return db.rawQuery(query, null);
    }

    // Fetch total expenses for the current month
    public Cursor getCurrentMonthExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") AS total FROM " +
                TABLE_EXPENSES +
                " WHERE strftime('%Y-%m', " + COLUMN_DATE + "/1000, 'unixepoch') = strftime('%Y-%m', 'now')";
        return db.rawQuery(query, null);
    }
    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EXPENSES, null);
    }
    public int deleteCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CATEGORIES, COLUMN_CATEGORY_NAME + " = ?", new String[]{categoryName});
    }

}
