package com.example.a97210.mobilesafe.Engine;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;

/**
 *
 * Created by 97210 on 2018/2/5.
 */

public class AddressDao {
    Context context;
    private static final String TAG = "AddressDao";

    static String path = "data/data/com.example.a97210.mobilesafe/files/address.db";
    private static String address;

    public static String getAddress(String phone) {

        //截取前7位
        if (phone.length()>=7) {
            phone = phone.substring(0,7);
            SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = database.query("data1", new String[]{"outkey"}, "id = ?", new String[]{phone}, null, null, null);
            if (cursor.moveToNext()) {
                String outkey = cursor.getString(0);

                Cursor indexcursor = database.query("data2", new String[]{"location"}, "id = ?", new String[]{outkey}, null, null, null);
                if (indexcursor.moveToNext()) {
                    address = indexcursor.getString(0);
                    Log.i(TAG, "getAddress: address:"+ address);
                }
            }
            return address;
        }

        return null;
    }
}
