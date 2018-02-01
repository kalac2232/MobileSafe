package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.a97210.mobilesafe.R;
import com.example.a97210.mobilesafe.utils.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 */
public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";
    private TextView tv_verson_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除activity头部的一种方式
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        initUI();
        getNewVerson();

    }

    private void getNewVerson() {
        int localVersionCode = getVersionCode();//本地版本号
        new Thread(){
            @Override
            public void run() {
                //发送请求获取数据(记得网络权限)
                try {
                    //封装url地址
                    URL url = new URL("http://10.0.2.2:8080/updata.json");
                    //开启一个链接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //设置请求头
                    connection.setConnectTimeout(1000*10);//设置请求超时时限
                    connection.setReadTimeout(1000*10);//设置读取超时时限
                    //如果请求方式为get方式，则不用写

                    //获取请求成功响应吗
                    if (connection.getResponseCode() == 200) {
                        //以流的方式，将数据获取下来
                        InputStream is = connection.getInputStream();
                        //将流转换成字符串（工具类封装）
                        String json = StreamUtil.streamToString(is);
                        Log.i(TAG,json);
                        
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 获取当前应用版本号
     * @return 返回int 类型 ，0为获取失败
     */
    private int getVersionCode() {
        //创建包管理者
        PackageManager packageManager = getPackageManager();
        //从包的管理者对象中可以获取版本号
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            //获取版本名称
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 从清单文件中获取版本名称
     * @return String类型的版本名称 返回null代表异常
     */
    private String getVersionName() {
        //创建包管理者
        PackageManager packageManager = getPackageManager();
        //从包的管理者对象中可以获取版本号
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            //获取版本名称
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化UI
     */

    private void initUI() {
        tv_verson_name = (TextView) findViewById(R.id.tv_verson_name);
        tv_verson_name.setText(getVersionName());
    }
}
