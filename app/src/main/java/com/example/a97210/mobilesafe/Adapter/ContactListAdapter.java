package com.example.a97210.mobilesafe.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a97210.mobilesafe.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 97210 on 2018/2/4.
 */

public class ContactListAdapter extends BaseAdapter {
    List<HashMap<String, String>> contactList;
    Context context;
    public ContactListAdapter(Context context, List<HashMap<String, String>> contactList) {
        this.contactList = contactList;
        this.context = context;
    }

    private static final String TAG = "ContactListAdapter";
    @Override
    public int getCount() {
        Log.i(TAG, "getCount: contactList.size()"+contactList.size());
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.listview_contact, null);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_name.setText(contactList.get(position).get("name"));
        tv_phone.setText(contactList.get(position).get("phone"));

        return view;
    }
}
