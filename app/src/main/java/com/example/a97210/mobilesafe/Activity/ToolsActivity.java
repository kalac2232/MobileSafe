package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.a97210.mobilesafe.Engine.AddressDao;
import com.example.a97210.mobilesafe.R;



/**
 *
 * Created by 97210 on 2018/2/5.
 */

public class ToolsActivity extends Activity {

    private Context mContext;
    private EditText et_queryphone;
    private Button bt_query;
    private TextView phone_address;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (address == null) {
                phone_address.setText("未知号码");
            } else {
                phone_address.setText(address);
            }
        }
    };
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        mContext = this;
        init();


    }

    private void init() {
        et_queryphone = (EditText) findViewById(R.id.et_queryphone);
        bt_query = (Button) findViewById(R.id.bt_query);
        phone_address = (TextView) findViewById(R.id.tv_query_phone_address);
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_queryphone.getText().toString();
                query(phone);

            }
        });
        et_queryphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = et_queryphone.getText().toString();
                query(phone);
            }
        });

    }

    private void query(final String phone) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                address = AddressDao.getAddress(phone);
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }
}
