package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.a97210.mobilesafe.R;
import com.example.a97210.mobilesafe.Utils.ConstantValue;
import com.example.a97210.mobilesafe.Utils.SharePreferenceUtil;
import com.example.a97210.mobilesafe.View.SettingItemView;

/**
 * Created by 97210 on 2018/2/4.
 */
public class Setup2Activity extends Activity{
    private Context mContext;
    private static final String TAG = "Setup2Activity";
    private SettingItemView siv_sim_bound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        mContext = this;
        initUI();
    }

    private void initUI() {
        //找到控件
        siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);

        String sim_number = SharePreferenceUtil.getString(mContext, ConstantValue.SIM_NUMBER, "");
        //判断是否存储过sim号码 并初始化CheckBox状态
        if (TextUtils.isEmpty(sim_number)) {
            siv_sim_bound.setCheck(false);
        } else {
            siv_sim_bound.setCheck(true);
        }
        //设置监听
        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果已经保存了SIM卡信息 ，则删除节点
                if (siv_sim_bound.isCheck()) {
                    SharePreferenceUtil.remove(mContext,ConstantValue.SIM_NUMBER);

                } else {
                    //否则存储SIM信息
                    TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    //获取sim序列号
                    String simSerialNumber = manager.getSimSerialNumber();

                    Log.i(TAG, "onClick:simSerialNumber: "+simSerialNumber);
                    SharePreferenceUtil.putString(mContext,ConstantValue.SIM_NUMBER,simSerialNumber);
                }

                siv_sim_bound.changeCheckBoxStatus();

            }
        });
    }
    public void prePage(View view) {
        Intent intent = new Intent(mContext, Setup1Activity.class);
        startActivity(intent);
        finish();
    }
    public void nextPage(View view) {
        if (siv_sim_bound.isCheck()) {
            Intent intent = new Intent(mContext, Setup3Activity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(mContext,"SIM未绑定，请先绑定",Toast.LENGTH_LONG).show();
        }
    }
}
