package com.app.tuskan.bakingapp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.app.tuskan.bakingapp.provider.RecipeContract.RecipeEntry.COLUMN_INGREDIENTS;
import static com.app.tuskan.bakingapp.provider.RecipeContract.RecipeEntry.COLUMN_NAME;
import static com.app.tuskan.bakingapp.provider.RecipeContract.RecipeEntry.TABLE_NAME;

/**
 * Created by Yakup on 27.7.2018.
 */

public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipes.db";

    private static final int DATABASE_VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY NOT NULL unique," +
                COLUMN_NAME + " TEXT NOT NULL unique," +
                COLUMN_INGREDIENTS + " TEXT NOT NULL unique)";
        db.execSQL(SQL_CREATE_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
