package com.example.vanya.inventoryapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.vanya.inventoryapp.data.ProductContract;

public class ProductCursorAdapter extends CursorAdapter {

    public  ProductCursorAdapter(Context context, Cursor cursor){
        super(context,cursor,0);
    }

    @Override
    public View newView(final Context context,final Cursor cursor, final ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor){
        TextView productName = view.findViewById(R.id.list_item_name);
        TextView productPrice = view.findViewById(R.id.list_item_price);
        final TextView productQuantity = view.findViewById(R.id.list_item_quantity);
        TextView saleButton = view.findViewById(R.id.sale_button);


        String nameString = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
        String priceString = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE));
        final String quantityString = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUAN));

        productName.setText(nameString);
        productPrice.setText(priceString);
        productQuantity.setText(quantityString);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newValue;

                if(quantityString.equals(0)){
                    return;
                }else{
                    newValue = Integer.parseInt(quantityString)-1;
                    Log.e("BUILDING: ","newValue = "+Integer.toString(newValue));
                    productQuantity.setText(Integer.toString(newValue));

                    ContentResolver cr = context.getContentResolver();
                    ContentValues values = new ContentValues();

                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                            cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME)));
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                            cursor.getDouble(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE)));
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUAN,newValue);
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER,
                            cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER)));
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONENUMBER,
                            cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONENUMBER)));


                    Long id = cursor.getLong(cursor.getColumnIndex(ProductContract.ProductEntry._ID));
                    cr.update(ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI,id),values,null, null);
                }
            }
        });
    }



}
