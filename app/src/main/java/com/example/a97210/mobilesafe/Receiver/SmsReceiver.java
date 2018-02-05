package com.example.a97210.mobilesafe.Receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.a97210.mobilesafe.R;
import com.example.a97210.mobilesafe.Service.SecurityService;
import com.example.a97210.mobilesafe.Utils.ConstantValue;
import com.example.a97210.mobilesafe.Utils.SharePreferenceUtil;

import java.io.IOException;

/**
 *
 * Created by 97210 on 2018/2/5.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";
    private ComponentName mDeviceAdminSample;
    private DevicePolicyManager mDPM;
    @Override
    public void onReceive(Context context, Intent intent) {
        //组件对象可以作为是否激活的判断标志
        mDeviceAdminSample = new ComponentName(context, MyDeviceAdminReceiver.class);
        //判断防盗功能是否开启
        mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (SharePreferenceUtil.getBoolean(context, ConstantValue.SECURITYSTATUS,false)) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            String format = intent.getStringExtra("format");
            for (Object pdu : pdus) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                String messageBody = sms.getMessageBody();
                //如果包含包含报警信息
                if (messageBody.contains("#*alarm*#")) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.xpg);
                    mediaPlayer.start();
                }
                if (messageBody.contains("#*location*#")) {
                    Intent intent1 = new Intent(context, SecurityService.class);
                    context.startService(intent1);
                }
                if (messageBody.contains("#*wipedata*#")) {
                    if(mDPM.isAdminActive(mDeviceAdminSample)){
                        mDPM.wipeData(0);//手机数据
					    mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//手机sd卡数据
                    }

                }
                if (messageBody.contains("#*lockscreen*#")) {
                    //是否开启的判断
                    if(mDPM.isAdminActive(mDeviceAdminSample)){
                        //激活--->锁屏
                        mDPM.lockNow();
                        //锁屏同时去设置密码
                        mDPM.resetPassword("123", 0);
                    }
                }
            }
        }
    }
}
