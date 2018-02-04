package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.a97210.mobilesafe.Adapter.ContactListAdapter;
import com.example.a97210.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by 97210 on 2018/2/4.
 */
public class ContactListActivity extends Activity {

    private static final String TAG = "ContactListActivity";

    private ListView lv_contact;
    private Context mContext;
    private List<HashMap<String,String>> contactList = new ArrayList<HashMap<String,String>>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //填充数据适配器
            ContactListAdapter listAdapter = new ContactListAdapter(mContext, contactList);
            lv_contact.setAdapter(listAdapter);
            lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    String phone = contactList.get(position).get("phone");
                    intent.putExtra("phone",phone);
                    setResult(10,intent);
                    finish();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        mContext = this;
        initUI();
        initData();

    }
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //使用内容解析者
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"}, null, null, null);
                contactList.clear();
                //循环游标，直到没有数据为止
                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    //
                    Log.i(TAG, "id: "+id);
                    Cursor indexCursor = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1","mimetype"}, "contact_id = ?", new String[] {id}, null);
                    //循环获取每个联系人的电话号码和姓名

                    HashMap<String, String> hashMap = new HashMap<String, String>();

                    while (indexCursor.moveToNext()) {
                        String data = indexCursor.getString(0);
                        String type = indexCursor.getString(1);
                        if (type.equals("vnd.android.cursor.item/phone_v2")) {
                            //数据非空判断
                            if (!TextUtils.isEmpty(data))
                                hashMap.put("phone",data);
                        } else if (type.equals("vnd.android.cursor.item/name")){
                            if (!TextUtils.isEmpty(data))
                                hashMap.put("name",data);
                        }

                    }
                    indexCursor.close();
                    contactList.add(hashMap);
                    Log.i(TAG, "--------: "+contactList.size());

                }
                cursor.close();
                //告诉主线程数据准备好了
                //直接调用Handler发送一个空消息，只要发出去mHandler的方法就会被执行
                mHandler.sendEmptyMessage(0);
            }
        }).start();

    }

    private void initUI() {
        lv_contact = (ListView) findViewById(R.id.lv_contact);
    }
}
