package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a97210.mobilesafe.R;
import com.example.a97210.mobilesafe.Utils.ConstantValue;
import com.example.a97210.mobilesafe.Utils.SharePreferenceUtil;

/**
 * Created by 97210 on 2018/2/4.
 */
public class Setup3Activity extends Activity{
    private Context mContext;
    private EditText et_phone_number;
    private Button bt_select_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        mContext = this;
        initUI();
    }

    private void initUI() {
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        //获取上次的状态
        String phone = SharePreferenceUtil.getString(mContext, ConstantValue.SAFE_PHONENUMBER, "");
        if (phone != null) {
            et_phone_number.setText(phone);
        }
        bt_select_number = (Button) findViewById(R.id.bt_select_number);
        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ContactListActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            String phone = data.getStringExtra("phone");
            //去除无用的字符
            phone = phone.replace("-","").replace("(","").replace(")","").replace(" ","").trim();
            et_phone_number.setText(phone);
        }
    }

    public void prePage(View view) {
        Intent intent = new Intent(mContext, Setup2Activity.class);
        startActivity(intent);
        finish();
    }
    public void nextPage(View view) {
        //如果输入框不为空，才让跳转至下个界面
        if (!TextUtils.isEmpty(et_phone_number.getText().toString())) {
            //将安全号码存储至sp中,方便下次回显
            SharePreferenceUtil.putString(mContext, ConstantValue.SAFE_PHONENUMBER,et_phone_number.getText().toString());
            //跳转至下个界面
            Intent intent = new Intent(mContext, Setup4Activity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(mContext,"请输入安全号码",Toast.LENGTH_LONG).show();
        }

    }
}
