package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.a97210.mobilesafe.R;
import com.example.a97210.mobilesafe.Service.AttributionService;
import com.example.a97210.mobilesafe.Utils.ConstantValue;
import com.example.a97210.mobilesafe.Utils.ServiceUtil;
import com.example.a97210.mobilesafe.Utils.SharePreferenceUtil;
import com.example.a97210.mobilesafe.Utils.VersonUtil;
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
        init();


    }

    private void init() {
        //找到自动更新控件
        final SettingItemView siv_updata = (SettingItemView) findViewById(R.id.siv_updata);
        //由sp文件中的AUTOUPDATASTATUS状态设置开关状态状态
        siv_updata.setCheck(SharePreferenceUtil.getBoolean(mContext, ConstantValue.AUTOUPDATASTATUS,false));
        siv_updata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siv_updata.changeCheckBoxStatus();
                //保存当前状态
                SharePreferenceUtil.putBoolean(mContext,ConstantValue.AUTOUPDATASTATUS,siv_updata.isCheck());
            }
        });

        final SettingItemView siv_address = (SettingItemView) findViewById(R.id.siv_address);
        //来电归属地控件
        siv_address.setCheck(ServiceUtil.isRunning(mContext,"com.example.a97210.mobilesafe.Service.AttributionService"));
        siv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siv_address.changeCheckBoxStatus();
                Intent intent = new Intent(mContext,AttributionService.class);
                if (siv_address.isCheck()) {
                    //启动服务
                    startService(intent);
                } else {
                    stopService(intent);
                }
                //保存当前状态
                SharePreferenceUtil.putBoolean(mContext,ConstantValue.ADDRESSSTATUS,siv_address.isCheck());
            }
        });

        //手动更新按钮
        Button bt_checkupdata = (Button) findViewById(R.id.bt_checkupdata);
        bt_checkupdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VersonUtil versonUtil = new VersonUtil(mContext);
                versonUtil.checkNewVerson();
            }
        });
    }
}
