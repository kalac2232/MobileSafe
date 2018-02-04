package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.a97210.mobilesafe.R;
import com.example.a97210.mobilesafe.Utils.ConstantValue;
import com.example.a97210.mobilesafe.Utils.SharePreferenceUtil;

/**
 *
 * Created by 97210 on 2018/2/4.
 */
public class SetupOverActivity extends Activity{

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (SharePreferenceUtil.getBoolean(mContext, ConstantValue.SECURITYSTATUS,false)) {
            setContentView(R.layout.activity_setup_over);
        } else {
            Intent intent = new Intent(mContext, Setup1Activity.class);
            startActivity(intent);
            finish();
        }

    }
}
