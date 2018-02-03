package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.a97210.mobilesafe.R;
import com.example.a97210.mobilesafe.Utils.ConstantValue;
import com.example.a97210.mobilesafe.Utils.SharePreferenceUtil;
import com.example.a97210.mobilesafe.View.SettingItemView;

/**
 *
 * Created by 97210 on 2018/2/2.
 */
public class SettingActivity extends Activity{

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext = this;
        //找到控件
        final SettingItemView siv_updata = (SettingItemView) findViewById(R.id.siv_updata);
        //设置状态
        siv_updata.setCheck(SharePreferenceUtil.getBoolean(mContext, ConstantValue.AUTOUPDATASTATUS,false));
        siv_updata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siv_updata.changeCheckBoxStatus();
                SharePreferenceUtil.putBoolean(mContext,ConstantValue.AUTOUPDATASTATUS,siv_updata.isCheck());
            }
        });
    }
}
