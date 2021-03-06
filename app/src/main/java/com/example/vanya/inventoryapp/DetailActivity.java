package com.example.vanya.inventoryapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vanya.inventoryapp.data.ProductContract;
import com.example.vanya.inventoryapp.data.ProductContract.ProductEntry;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mNameDetailText;
    private TextView mPriceDetailText;
    private TextView mQuantityDetailText;
    private TextView mSupplierDetailText;
    private TextView mPhoneNumberDetailText;
    private Button mEditButton;
    private Button mDeleteButton;
    private Button mOrderButton;
    private Button mPlusButton;
    private Button mMinusButton;
    private Uri mCurrentUri;
    private Cursor mCursor;
    private ContentResolver mContentResolver;
    private int mQuantity;
    private Intent receivedIntent;
    private static final int PRODUCT_LOADER = 0;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE};
    String[] mProjection = {
            ProductEntry._ID,
            ProductEntry.COLUMN_PRODUCT_NAME,
            ProductEntry.COLUMN_PRODUCT_PRICE,
            ProductEntry.COLUMN_PRODUCT_QUAN,
            ProductEntry.COLUMN_PRODUCT_SUPPLIER,
            ProductEntry.COLUMN_PRODUCT_PHONENUMBER};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mNameDetailText = findViewById(R.id.detail_name);
        mPriceDetailText = findViewById(R.id.detail_price);
        mQuantityDetailText = findViewById(R.id.detail_quantity);
        mSupplierDetailText = findViewById(R.id.detail_supplier);
        mPhoneNumberDetailText = findViewById(R.id.detail_phone_number);
        mEditButton = findViewById(R.id.detail_edit_button);
        mDeleteButton = findViewById(R.id.detail_delete_button);
        mOrderButton = findViewById(R.id.detail_order);
        mPlusButton = findViewById(R.id.detail_plus_button);
        mMinusButton = findViewById(R.id.detail_minus_button);
        receivedIntent = getIntent();
        mCurrentUri = receivedIntent.getData();
        mContentResolver = getContentResolver();
        mCursor = mContentResolver.query(mCurrentUri,mProjection,null,null);
        mCursor.moveToFirst();
        mQuantity = mCursor.getInt(mCursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUAN));
        setTitle("Details");
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, EditorActivity.class);
                intent.setData(mCurrentUri);
                startActivity(intent);
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mPhoneNumberDetailText.getText()));
                if (!hasPermissions(DetailActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(DetailActivity.this, PERMISSIONS, PERMISSION_ALL);
                    if (ActivityCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            }
        });

    }





    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUAN,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER,
                ProductEntry.COLUMN_PRODUCT_PHONENUMBER};

        return new CursorLoader(this,
                mCurrentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {

            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUAN);
            int supplierColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER);
            int phoneNumberColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PHONENUMBER);

            String name = cursor.getString(nameColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            String phoneNumber = cursor.getString(phoneNumberColumnIndex);

            if(mCurrentUri!=null){
                mNameDetailText.setText(name);
                mPriceDetailText.setText(price);
                mQuantityDetailText.setText(quantity);
                mSupplierDetailText.setText(supplier);
                mPhoneNumberDetailText.setText(phoneNumber);
            }

        }

        mPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int newValue=0;
                int id = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry._ID));
                mQuantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUAN));
                newValue = mQuantity + 1;
                final Uri uri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI,id);
                ContentValues values = new ContentValues();

                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                        cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME)));
                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                        cursor.getDouble(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE)));
                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUAN, newValue);
                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER,
                        cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER)));
                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONENUMBER,
                        cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONENUMBER)));

                mContentResolver.update(uri
                        , values
                        , null
                        , null);

                mQuantityDetailText.setText(Integer.toString(newValue));

            }
        });

        mMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newValue=0;
                int id = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry._ID));
                mQuantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUAN));
                if(mQuantity>0) {

                    newValue = mQuantity - 1;
                    final Uri uri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);
                    ContentValues values = new ContentValues();

                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                            cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME)));
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                            cursor.getDouble(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE)));
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUAN, newValue);
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER,
                            cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER)));
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONENUMBER,
                            cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONENUMBER)));

                    mContentResolver.update(uri
                            , values
                            , null
                            , null);

                    mQuantityDetailText.setText(Integer.toString(newValue));
                }
            }
        });




    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameDetailText.setText("");
        mPriceDetailText.setText("");
        mQuantityDetailText.setText("");
        mSupplierDetailText.setText("");
        mPhoneNumberDetailText.setText("");
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {

        if (mCurrentUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
