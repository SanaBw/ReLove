package com.sanida.relove.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {



    public DatabaseHelper(@Nullable Context context) {
        super(context, "items.db", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement="CREATE TABLE CART_TABLE ( ID INTEGER PRIMARY KEY AUTOINCREMENT, ITEM_NAME TEXT, ITEM_PRICE TEXT)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addToCart(Shop shop){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ITEM_NAME", shop.getName());
        cv.put("ITEM_PRICE", shop.getPrice());

        long insert = db.insert("CART_TABLE", null, cv);

        if (insert==-1){
            return false;
        } else {
            return true;
        }

    }

    public ArrayList<Shop> getItems(){
        ArrayList<Shop> shops = new ArrayList<>();


        String query = "SELECT * FROM 'CART_TABLE' ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
           do {
               String itemName = cursor.getString(1);
               String itemPrice = cursor.getString(2);
               Shop shop = new Shop();
               shop.setName(itemName);
               shop.setPrice(itemPrice);

               shops.add(shop);
           } while (cursor.moveToNext());
        }


        return shops;
    }
}
