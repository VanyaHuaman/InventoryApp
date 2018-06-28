package com.example.vanya.inventoryapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.vanya.inventoryapp.data.ProductContract;

public class ProductCursorAdapter extends CursorAdapter {

    private Context mContext;


    public  ProductCursorAdapter(Context context, Cursor cursor){
        super(context,cursor,0);
        mContext = context;
    }


    public static class ProductViewHolder {
        public final TextView mProductName;
        public final TextView mProductPrice;
        public final TextView mProductQuantity;
        public final TextView mSaleButton;

        public ProductViewHolder(View view) {
            mProductName = view.findViewById(R.id.list_item_name);
            mProductPrice = view.findViewById(R.id.list_item_price);
            mProductQuantity = view.findViewById(R.id.list_item_quantity);
            mSaleButton = view.findViewById(R.id.sale_button);
        }
    }
    

    @Override
    public View newView(Context context,Cursor cursor, ViewGroup parent){
        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        ProductViewHolder productViewHolder = new ProductViewHolder(view);
        view.setTag(productViewHolder);
        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor){
        final Cursor mCursor = cursor;
        int id = cursor.getInt(mCursor.getColumnIndex(ProductContract.ProductEntry._ID));
        final Uri uri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI,id);
        final ProductViewHolder productViewHolder = (ProductViewHolder) view.getTag();

        String nameString = mCursor.getString(mCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
        double priceString = mCursor.getDouble(mCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE));
        final int quantityString = mCursor.getInt(mCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUAN));



        productViewHolder.mProductName.setText(nameString);
        productViewHolder.mProductPrice.setText(Double.toString(priceString));
        productViewHolder.mProductQuantity.setText(Integer.toString(quantityString));


        productViewHolder.mSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newValue;

                if(quantityString==0){
                    return;
                }else{
                    newValue = quantityString-1;
                    Log.e("BUILDING: ","newValue = "+Integer.toString(newValue));


                    ContentResolver cr = mContext.getContentResolver();
                    ContentValues values = new ContentValues();

                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                            mCursor.getString(mCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME)));
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                            mCursor.getDouble(mCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE)));
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUAN,newValue);
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER,
                            mCursor.getString(mCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER)));
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONENUMBER,
                            mCursor.getString(mCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PHONENUMBER)));


                    String[] arg = new String[]{mCursor.getString(mCursor.getColumnIndex(ProductContract.ProductEntry._ID))};

                    Long id = mCursor.getLong(mCursor.getColumnIndex(ProductContract.ProductEntry._ID));

                    cr.update(uri
                            ,values
                            ,mCursor.getString(mCursor.getColumnIndex(ProductContract.ProductEntry._ID))
                            , null);

                    productViewHolder.mProductQuantity.setText(Integer.toString(newValue));
                }
            }
        });
    }


}
