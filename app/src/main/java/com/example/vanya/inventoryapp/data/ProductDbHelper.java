package com.example.vanya.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.vanya.inventoryapp.data.ProductContract.ProductEntry;

public class ProductDbHelper  extends SQLiteOpenHelper{

    public static final String LOG_TAG = ProductDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "inventory.db";

    private static final int DATABASE_VERSION = 1;


    //Constructor
    public ProductDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE "+ ProductEntry.TABLE_NAME + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + ProductEntry.COLUMN_PRODUCT_QUAN + " TEXT NOT NULL DEFAULT 0, "
                + ProductEntry.COLUMN_PRODUCT_SUPPLIER + " TEXT NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_PHONENUMBER + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
        Log.v(LOG_TAG,"Create Table Ran");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            deleteTable(db,ProductEntry.TABLE_NAME);
            onCreate(db);
            Log.v(LOG_TAG,"on upgrade ran");
        }
    }

    public void deleteTable(SQLiteDatabase db, String table){
        String SQL_DELETE_ENTRIES = "DROP TABLE "+table;
        db.execSQL(SQL_DELETE_ENTRIES);
        Log.v(LOG_TAG, "Delete Tables Ran");
    }


}


