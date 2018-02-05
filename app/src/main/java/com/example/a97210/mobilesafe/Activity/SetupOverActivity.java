package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        setContentView(R.layout.activity_setup_over);
        init();
        //判断防盗模式是否开启 如开启则进入设置完成界面
//        if (SharePreferenceUtil.getBoolean(mContext, ConstantValue.SECURITYSTATUS,false)) {
//
//        } else {
//            //如未开启,则进入设置界面
//            Intent intent = new Intent(mContext, Setup1Activity.class);
//            startActivity(intent);
//            finish();
//        }

    }

    private void init() {
        Button bt_reset_setup = (Button) findViewById(R.id.bt_reset_setup);
        TextView tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_phone.setText(SharePreferenceUtil.getString(mContext,ConstantValue.SAFE_PHONENUMBER,"未设置安全号码"));
        ImageView iv_lock = (ImageView) findViewById(R.id.iv_lock);
        if(SharePreferenceUtil.getBoolean(mContext,ConstantValue.SECURITYSTATUS,false)) {
            iv_lock.setBackgroundResource(R.drawable.lock);
        } else {
            iv_lock.setBackgroundResource(R.drawable.unlock);
        }
        bt_reset_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Setup1Activity.class);
                startActivity(intent);
                finish();
                //开启平移动画
                overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
            }
        });
    }
}
