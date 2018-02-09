package com.example.a97210.mobilesafe.Utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 *
 * Created by 97210 on 2018/2/8.
 */

public class ServiceUtil {

    private static ActivityManager mAM;

    public static boolean isRunning(Context context, String serviceName) {
        //获取ActivityManager对象，获取手机正在运行的服务
        mAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取手机正在运行的服务集合
        List<ActivityManager.RunningServiceInfo> runningServices = mAM.getRunningServices(100);
        //遍历列表
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            if (serviceName.equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
