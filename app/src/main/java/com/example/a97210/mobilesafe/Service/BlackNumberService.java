package com.example.a97210.mobilesafe.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.a97210.mobilesafe.DataBase.Domain.BlackNumberInfo;
import com.example.a97210.mobilesafe.DataBase.dao.BlackNumberDao;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by 97210 on 2018/2/10.
 */

public class BlackNumberService extends Service {

    private InnerSmsRecevice mInnerSmsRecevice;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: 服务跑起来了");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        //设置接受广播的优先级
        intentFilter.setPriority(1000);

        mInnerSmsRecevice = new InnerSmsRecevice();
        registerReceiver(mInnerSmsRecevice,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消注册
        if (mInnerSmsRecevice != null) {
            unregisterReceiver(mInnerSmsRecevice);
        }
    }

    private class InnerSmsRecevice extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object pdu : pdus) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                //获取到接收到的短信的来信人
                String originatingAddress = sms.getOriginatingAddress();
                BlackNumberDao dao = BlackNumberDao.getInstance(context);
                int mode = dao.getMode(originatingAddress);
                if (mode == 1 || mode == 3) {
                    abortBroadcast();
                }
            }
        }
    }
}
