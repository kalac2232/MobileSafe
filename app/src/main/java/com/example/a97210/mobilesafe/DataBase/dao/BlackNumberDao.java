package com.example.a97210.mobilesafe.DataBase.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.a97210.mobilesafe.DataBase.BlackNumberOpenHelper;
import com.example.a97210.mobilesafe.DataBase.Domain.BlackNumberInfo;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 *
 *
 * Created by 97210 on 2018/2/9.
 */

public class BlackNumberDao {

    private BlackNumberOpenHelper blackNumberOpenHelper;
    //私有化构造函数
    private BlackNumberDao(Context context) {
        //创建数据库及其表结构
        blackNumberOpenHelper = new BlackNumberOpenHelper(context);
    }
    //申明一个当前类的对象
    private static BlackNumberDao blackNumberDao = null;
    //提供一个静态方法,如果当前类的对象为空，创建一个新的
    public static BlackNumberDao getInstance(Context context) {
        if (blackNumberDao == null) {
            blackNumberDao = new BlackNumberDao(context);
        }
        return blackNumberDao;
    }

    /**
     * 增加一个条目
     * @param phone 拦截的电话号码
     * @param mode 拦截类型（1：短信 2：电话 3：所有）
     */
    public void insert(String phone,int mode) {
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        if(db == null) {
            Log.i(TAG, "insert: 空db");
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone",phone);
        contentValues.put("mode",mode);

        db.insert("blacknumber",null,contentValues);
        //关闭数据库
        db.close();
    }

    /**
     * 从黑名单中删除电话号码
     * @param phone 删除的电话号码
     */
    public void delete(String phone) {
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        db.delete("blacknumber","phone = ?",new String[]{phone});
        db.close();
    }

    /**
     * 根据电话号码，更新拦截模式
     * @param phone 更新的电话号码
     * @param mode 要更新为的拦截模式
     */
    public void updata(String phone,String mode) {
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode",mode);

        db.update("blacknumber",contentValues,"phone = ?",new String[]{phone});
        db.close();
    }

    /**
     * @return 查询数据库中多有的号码以及拦截类型的集合
     */
    public ArrayList<BlackNumberInfo> findAll(){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"phone", "mode"}, null, null, null, null, "_id desc");
        ArrayList<BlackNumberInfo> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();

            blackNumberInfo.phone = cursor.getString(1);
            blackNumberInfo.mode = cursor.getInt(2);
            arrayList.add(blackNumberInfo);

        }
        cursor.close();
        db.close();
        return arrayList;
    }

    /**
     * 查询20条数据
     * @param index
     */
    public ArrayList<BlackNumberInfo> find(int index) {
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from blacknumber order by _id desc limit ?,20;",new String[] {index+""});
        ArrayList<BlackNumberInfo> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();

            blackNumberInfo.phone = cursor.getString(1);
            blackNumberInfo.mode = cursor.getInt(2);
            arrayList.add(blackNumberInfo);

        }
        cursor.close();
        db.close();
        return arrayList;
    }

    /**
     * 查询数据库中的总条数
     * @return 返回数据库的总条数
     */
    public int getCount() {
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber",null);
        int count = 0;
        if (cursor.moveToNext()) {

            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    /**
     * 查询传入的手机号拦截模式
     * @param phone 要查询的手机号
     * @return 1 短信 2 电话 3 所以 0 没查到
     */
    public int getMode(String phone) {
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        int mode = 0;
        Cursor cursor = db.query("blacknumber",new String[] {"mode"},"phone = ?",new String[]{phone},null,null,null);
        if (cursor.moveToNext()) {
            mode = cursor.getInt(0);
        }
        cursor.close();
        return mode;
    }
}
