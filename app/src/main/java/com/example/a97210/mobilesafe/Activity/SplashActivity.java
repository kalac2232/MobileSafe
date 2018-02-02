package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a97210.mobilesafe.R;
import com.example.a97210.mobilesafe.utils.StreamUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";
    /**
     * 更新新版本状态码
     */
    private static final int UPDATE_VERSION = 100;
    /**
     * 进入应用程序主界面状态码
     */
    private static final int ENTER_HOME = 101;
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int JSON_ERROR = 104;
    private Context mContext;
    private String mVersionDes;
    private String mDownloadUrl;
    private TextView tv_verson_name;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    //弹出对话框，提示更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    //无需更新 进入主程序
                    enterHome();
                    break;
                case URL_ERROR:
                    Toast.makeText(mContext,"URL异常",Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case IO_ERROR:
                    Toast.makeText(mContext,"与服务器连接失败",Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case JSON_ERROR:
                    Toast.makeText(mContext,"JSON解析异常",Toast.LENGTH_LONG).show();
                    enterHome();

                    break;
            }
        }
    };


    /**
     *显示更新对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.drawable.home_safe);
        builder.setTitle("版本更新");
        //设置对话框内容
        builder.setMessage(mVersionDes);
        //设置按钮
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadAPK();
            }

        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框，跳转到主界面
                enterHome();
            }
        });
        //用户点击回退键的相应事件
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        builder.show();
    }

    private void downloadAPK() {
        //判断sd卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //获取存储路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MobileSafe.apk";
            Log.i(TAG,"下载path:"+path);
            //使用xUtiles架包下载apk
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {

                private ProgressDialog dialog;

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功
                    //file为下载成功后的文件
                    File file = responseInfo.result;
                    //关闭对话框
                    dialog.dismiss();
                    installAPK(file);
                    Log.i(TAG,"下载成功");
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    //下载失败
                    String message = e.getMessage();
                    Log.i(TAG,"下载失败"+message);
                }

                @Override
                public void onStart() {
                    //刚开始下载
                    super.onStart();
                    Log.i(TAG,"开始下载");
                    dialog = new ProgressDialog(mContext);
                    dialog.setTitle("下载进度");
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.show();
                }

                @Override
                public void onLoading(long total, final long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    //下载中
                    Log.i(TAG,"下载中"+isUploading);
                    //final int current1 = (int)current;

                    //设置进度条的最大值
                    dialog.setMax((int)total);
                    //设置当前进度
                    dialog.setProgress((int)current);

                }
            });
        }
    }

    /**
     * 安装对应apk
     *
     * @param file 安装文件
     */
    private void installAPK(File file) {
        //用隐式意图启动系统的安装程序
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
//        intent.setData(Uri.fromFile(file));
//        intent.setType("application/vnd.android.package-archive");
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除activity头部的一种方式
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        mContext = this;
        initUI();
        getNewVerson();

    }
    /**
     * 进入程序主界面
     */
    private void enterHome() {
        Intent intent = new Intent(mContext, HomeActivity.class);
        //startActivity(intent);
        startActivityForResult(intent,0);
        //开启主界面后 结束导航界面
        finish();
    }
    //当用户进入安装程序后退出安装
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void getNewVerson() {
        final int localVersionCode = getVersionCode();//本地版本号
        new Thread(){
            @Override
            public void run() {
                //创建消息对象
                Message obtain = Message.obtain();
                long startTime = System.currentTimeMillis();
                //发送请求获取数据(记得网络权限)
                try {
                    //封装url地址
                    URL url = new URL("http://10.0.2.2:8080/updata.json");

                    //开启一个链接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //设置请求头
                    connection.setConnectTimeout(1000*10);//设置请求超时时限
                    connection.setReadTimeout(1000);//设置读取超时时限
                    //如果请求方式为get方式，则不用写

                    //获取请求成功响应吗
                    if (connection.getResponseCode() == 200) {
                        //以流的方式，将数据获取下来
                        InputStream is = connection.getInputStream();
                        //将流转换成字符串（工具类封装）
                        String json = StreamUtil.streamToString(is);
                        //Log.i(TAG,json);
                        //解析json
                        JSONObject jsonObject = new JSONObject(json);
                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        if (localVersionCode<Integer.parseInt(versionCode)) {
                            //如果有新版本
                            obtain.what =  UPDATE_VERSION;
                        }else {
                            //否则进入主界面
                            obtain.what = ENTER_HOME;
                        }

                    }

                } catch (MalformedURLException e) {
                    obtain.what = URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    obtain.what = IO_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    obtain.what = JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    //发送消息
                    long endTime = System.currentTimeMillis();
                    //如果程序运行时间小于4秒 则补足4秒的导航页停留时间
                    if ((endTime - startTime) < 4000) {
                        try {
                            Thread.sleep(4000 - (endTime - startTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //发送消息
                    mHandler.sendMessage(obtain);
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
