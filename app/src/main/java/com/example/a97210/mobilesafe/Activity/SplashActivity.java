package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;


import android.content.Context;

import android.content.Intent;

import android.os.Bundle;



import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.a97210.mobilesafe.R;

import com.example.a97210.mobilesafe.Utils.ConstantValue;
import com.example.a97210.mobilesafe.Utils.SharePreferenceUtil;
import com.example.a97210.mobilesafe.Utils.VersonUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 *
 */
public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";

    private Context mContext;

    private TextView tv_verson_name;

    private RelativeLayout splash;
    private VersonUtil versonUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除activity头部的一种方式
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        mContext = this;
        initUI();
        initDB();
        initAnimation();
        //判断自动更新功能是否打开
        if (SharePreferenceUtil.getBoolean(mContext, ConstantValue.AUTOUPDATASTATUS,false)) {
            versonUtil.checkNewVerson();

        } else {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    enterHome();
                }
            }).start();
        }
    }

    private void initDB() {
        initAddressDB("address.db");
    }

    private void initAddressDB(String dbName) {
        File filesDir = getFilesDir();
        File file = new File(filesDir,dbName);
        //如果数据库文件已近存在
        if (file.exists()) {
            return;
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = getAssets().open(dbName);
            fos = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int temp = -1;
            while ((temp = is.read(bytes)) != -1) {
                fos.write(bytes,0,temp);
            }
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            //如果没有异常 则关闭流
            if (is != null &&fos != null) {
                try {

                    is.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initAnimation() {
        //创建动画对象
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        //设置动画时长
        alphaAnimation.setDuration(1000*2);
        splash.startAnimation(alphaAnimation);
    }


   
    /**
     * 进入程序主界面
     */
    private void enterHome() {
        Intent intent = new Intent(mContext, HomeActivity.class);
        //startActivity(intent);
        startActivity(intent);
        //开启主界面后 结束导航界面
        finish();
    }
    //当用户进入安装程序后退出安装




    /**
     * 初始化UI
     */
    private void initUI() {
        tv_verson_name = (TextView) findViewById(R.id.tv_verson_name);
        versonUtil = new VersonUtil(mContext);
        tv_verson_name.setText(versonUtil.getVersionName());
        splash = (RelativeLayout) findViewById(R.id.activity_splash);
    }



}
