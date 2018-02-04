package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.a97210.mobilesafe.R;
import com.example.a97210.mobilesafe.Utils.ConstantValue;
import com.example.a97210.mobilesafe.Utils.SharePreferenceUtil;

/**
 *
 * Created by 97210 on 2018/2/4.
 */
public class Setup4Activity extends Activity{
    private Context mContext;
    private CheckBox cb_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        mContext = this;
        init();
    }

    private void init() {
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        boolean status = SharePreferenceUtil.getBoolean(mContext, ConstantValue.SECURITYSTATUS, false);
        //设置CheckBox的状态
        cb_box.setChecked(status);
        if (status) {
            cb_box.setText("已开启防盗功能");
        } else {
            cb_box.setText("尚未开启防盗功能");
        }
        cb_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cb_box.isChecked()) {
                    cb_box.setText("已开启防盗功能");
                } else {
                    cb_box.setText("尚未开启防盗功能");
                }

                //向sp中存储当前状态
                SharePreferenceUtil.putBoolean(mContext,ConstantValue.SECURITYSTATUS,cb_box.isChecked());
            }
        });

    }

    public void prePage(View view) {
        Intent intent = new Intent(mContext, Setup3Activity.class);
        startActivity(intent);
        finish();
    }
    public void nextPage(View view) {

        if(cb_box.isChecked()) {
            Intent intent = new Intent(mContext, SetupOverActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(mContext,"未开启防盗模式",Toast.LENGTH_LONG).show();
        }
    }
}
