package com.example.a97210.mobilesafe.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.a97210.mobilesafe.Utils.ConstantValue;
import com.example.a97210.mobilesafe.Utils.SharePreferenceUtil;

/**
 *创建广播接受者
 * Created by 97210 on 2018/2/4.
 */

public class BootReceiver extends BroadcastReceiver {
   //当接收到广播会调用该方法
    @Override
    public void onReceive(Context context, Intent intent) {

        boolean status = SharePreferenceUtil.getBoolean(context, ConstantValue.SECURITYSTATUS, false);
        //如果防盗功能开启的话，则判断sim卡是否更换
        if (status) {
            String sim_storage_number = SharePreferenceUtil.getString(context, ConstantValue.SIM_NUMBER, "");
            //获取sim序列号
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = manager.getSimSerialNumber();

            //判断sim序列号是否和出错的相同
            if (!sim_storage_number.equals(simSerialNumber)) {
                //如果不相同，则向安全号码发送短信
                //取回安全号码
                String phone = SharePreferenceUtil.getString(context, ConstantValue.SAFE_PHONENUMBER, "");
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone,null,"手机被偷啦！！！！",null,null);
            }
        }

    }
}
