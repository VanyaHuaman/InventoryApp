package com.example.vanya.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.vanya.inventoryapp.data.ProductContract.ProductEntry;
import com.example.vanya.inventoryapp.data.ProductDbHelper;

public class MainActivity extends AppCompatActivity {

    private ProductDbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new ProductDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }


    private void displayDatabaseInfo(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
             ProductEntry._ID,
             ProductEntry.COLUMN_PRODUCT_NAME,
             ProductEntry.COLUMN_PRODUCT_PRICE,
             ProductEntry.COLUMN_PRODUCT_QUAN,
             ProductEntry.COLUMN_PRODUCT_SUPPLIER,
             ProductEntry.COLUMN_PRODUCT_PHONENUMBER};

        Cursor cursor = db.query(
                ProductEntry.TABLE_NAME,
                projection,null,null,null,null,null);

        TextView displayView = findViewById(R.id.text_view_product);

        try {
            displayView.setText("The product table contains " + cursor.getCount() + " items.\n\n");
            displayView.append(ProductEntry._ID + " - " +
                    ProductEntry.COLUMN_PRODUCT_NAME + " - " +
                    ProductEntry.COLUMN_PRODUCT_PRICE + " - " +
                    ProductEntry.COLUMN_PRODUCT_QUAN+ " - " +
                    ProductEntry.COLUMN_PRODUCT_SUPPLIER + " - " +
                    ProductEntry.COLUMN_PRODUCT_PHONENUMBER + "\n");


            int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUAN);
            int supplierColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER);
            int phoneNumberColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PHONENUMBER);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentPrice = cursor.getString(priceColumnIndex);
                String currentQuantity = cursor.getString(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                String currentPhoneNumber = cursor.getString(phoneNumberColumnIndex);

                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplier + " - " +
                        currentPhoneNumber));
            }
        } finally {
            cursor.close();
        }
    }//end of displayDatabase


    private void insertProduct() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "32pc Knife Set");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, "99.99");
        values.put(ProductEntry.COLUMN_PRODUCT_QUAN, "75");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER, "Cut co");
        values.put(ProductEntry.COLUMN_PRODUCT_PHONENUMBER, "2149855555");

        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);

        Log.v("Main Activity","New row ID"+newRowId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertProduct();
                displayDatabaseInfo();
                return true;
            case R.id.action_delete_all_entries:
                mDbHelper.deleteTable(mDbHelper.getWritableDatabase(),ProductEntry.TABLE_NAME);
                mDbHelper.onCreate(mDbHelper.getWritableDatabase());
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
