package com.example.vanya.inventoryapp;

import android.content.Context;
import android.database.Cursor;
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
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        TextView productName = view.findViewById(R.id.list_item_name);
        TextView productQuantity = view.findViewById(R.id.list_item_quantity);

        String nameString = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
        String quantityString = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUAN));

        productName.setText(nameString);
        productQuantity.setText(quantityString);
    }
}
