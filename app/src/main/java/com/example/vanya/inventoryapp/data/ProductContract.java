package com.example.vanya.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ProductContract {

    //Default Constructor **just in case**
    private ProductContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.vanya.inventoryapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PRODUCT = "products";

    public static final class ProductEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT);

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

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY +
                        "/" + PATH_PRODUCT;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY +
                        "/" + PATH_PRODUCT;
    }



}
