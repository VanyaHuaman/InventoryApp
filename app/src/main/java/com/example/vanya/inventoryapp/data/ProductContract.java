package com.example.vanya.inventoryapp.data;

import android.provider.BaseColumns;

public class ProductContract {

    //Default Constructor **just in case**
    private ProductContract(){}

    public static final class ProductEntry implements BaseColumns{

        public final static String TABLE_NAME = "products";
        //
        public final static String _ID = BaseColumns._ID;
        //
        public final static String COLUMN_PRODUCT_NAME = "name";
        //
        public final static String COLUMN_PRODUCT_PRICE = "price";
        //
        public final static String COLUMN_PRODUCT_QUAN = "quantity";
        //
        public final static String COLUMN_PRODUCT_SUPPLIER = "supplier";
        //
        public final static String COLUMN_PRODUCT_PHONENUMBER = "phonenumber";
    }



}
