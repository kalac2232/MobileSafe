package com.example.a97210.mobilesafe.Engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;

import com.example.a97210.mobilesafe.DataBase.Domain.ProcessInfo;
import com.example.a97210.mobilesafe.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by 97210 on 2018/2/11.
 */

public class ProcessInfoProvider {
    public static int getProcessCount(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取正在运行的集合
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        //返回集合的总数
        return runningAppProcesses.size();
    }

    /**
     * 获取可用空间
     * @param context 上下文
     * @return 返回可用的内存数 bytes
     */
    public static long getAvailSpace(Context context) {
        //获取ActivityManager
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //构建存储可用内存的对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //给MemoryInfo对象赋可用内存值
        am.getMemoryInfo(memoryInfo);//memoryInfo已经拿到了值
        return memoryInfo.availMem;
    }
    /**
     * 获取总空间
     * @param context 上下文
     * @return 返回总内存数 bytes
     */
    public static long getTotalSpace(Context context) {
        //获取ActivityManager
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //构建存储可用内存的对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //给MemoryInfo对象赋可用内存值
        am.getMemoryInfo(memoryInfo);//memoryInfo已经拿到了值
        return memoryInfo.totalMem;
    }

    /**
     * 获取正在运行的应用进程
     * @param context 上下文
     * @return  返回获取到的应用List合集
     */
    public static List<ProcessInfo> getProcessInfo(Context context) {
        ArrayList<ProcessInfo> processInfos_user = new ArrayList<>();
        processInfos_user.add(null);
        ArrayList<ProcessInfo> processInfos_sys = new ArrayList<>();
        processInfos_sys.add(null);
        ArrayList<ProcessInfo> processInfos = new ArrayList<>();
        //获取进程相关信息
        //获取ActivityManager管理者对象和packageManager管理者对象
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        //获取正在运行的集合
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        //遍历集合，获取进程信息
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {
            ProcessInfo processInfo = new ProcessInfo();
            //获取进程的名称，也就是应用的包名
            processInfo.packName = info.processName;
            //获取进程占用的内存大小 传一个pids
            int pid = info.pid;
            Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{pid});
            //获取可用空间
            processInfo.memSize = processMemoryInfo[0].getTotalPrivateDirty()*1024;//返回的是kb 需要转换成bytes
            try {
                //获取应用的名称(包名，)
                ApplicationInfo applicationInfo = pm.getApplicationInfo(processInfo.packName, 0);

                processInfo.name = applicationInfo.loadLabel(pm).toString();
                //获取应用的图标
                processInfo.icon = applicationInfo.loadIcon(pm);
                //判断是否为系统进程
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    processInfo.isSystem = true;
                } else {
                    processInfo.isSystem = false;
                }
            } catch (PackageManager.NameNotFoundException e) {
                //如果包名未找到,则将程序包名作为名称显示
                processInfo.name = processInfo.packName;
                //使用默认图标
                processInfo.icon = context.getResources().getDrawable(R.mipmap.ic_launcher);
                //默认为系统应用
                processInfo.isSystem = true;
                e.printStackTrace();
            }
            if (!processInfo.isSystem) {
                //如果为当前应用则不添加到列表中
                if (!processInfo.packName.equals(context.getPackageName())) {
                    processInfos_user.add(processInfo);
                }

            } else {
                processInfos_sys.add(processInfo);
            }

        }
        processInfos.addAll(processInfos_user);
        processInfos.addAll(processInfos_sys);
        return processInfos;
    }
}
