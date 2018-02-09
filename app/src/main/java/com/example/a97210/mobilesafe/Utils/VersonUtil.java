package com.example.a97210.mobilesafe.Utils;

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
import android.util.Log;
import android.widget.Toast;

import com.example.a97210.mobilesafe.Activity.HomeActivity;
import com.example.a97210.mobilesafe.Activity.SplashActivity;
import com.example.a97210.mobilesafe.R;
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

import static com.example.a97210.mobilesafe.Utils.ConstantValue.IO_ERROR;
import static com.example.a97210.mobilesafe.Utils.ConstantValue.JSON_ERROR;
import static com.example.a97210.mobilesafe.Utils.ConstantValue.NORMAL;
import static com.example.a97210.mobilesafe.Utils.ConstantValue.UPDATE_VERSION;
import static com.example.a97210.mobilesafe.Utils.ConstantValue.URL_ERROR;

/**
 * 用于版本的相关操作
 * Created by 97210 on 2018/2/3.
 */

public class VersonUtil {
    /**
     * 更新新版本状态码
     */

    private static final String TAG = "VersonUtil";

    private final Context context;



    public VersonUtil(Context context) {
        this.context = context;
    }
    //获取版本号
    private int getVersonCode() {
        PackageManager packageManager = context.getPackageManager();
        //从包的管理者对象中可以获取版本号
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            //获取版本名称
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * //获取版本名称
     * @return 返回版本名称 返回NULL代表异常
     */
    public String getVersionName() {
        //创建包管理者
        PackageManager packageManager = context.getPackageManager();
        //从包的管理者对象中可以获取版本号
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            //获取版本名称
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private String mVersionDes;
    private String mDownloadUrl;
    //检测新版本
    public void checkNewVerson() {

        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_VERSION:
                        //弹出对话框，提示更新
                        showUpdateDialog();
                        break;
                    case NORMAL:
                        //无需更新
                        //如果当前页面为SplashActivity则进行跳转
                        if (context instanceof SplashActivity) {

                            Activity activity = (Activity) context;
                            Intent intent = new Intent(activity, HomeActivity.class);
                            context.startActivity(intent);
                            //开启主界面后 结束导航界面
                            activity.finish();
                        }
                        break;
                    case URL_ERROR:
                        Toast.makeText(context,"URL异常",Toast.LENGTH_LONG).show();
                        //如果当前页面为SplashActivity则进行跳转
                        if (context instanceof SplashActivity) {

                            Activity activity = (Activity) context;
                            Intent intent = new Intent(activity, HomeActivity.class);
                            context.startActivity(intent);
                            //开启主界面后 结束导航界面
                            activity.finish();
                        }
                        break;
                    case IO_ERROR:
                        Toast.makeText(context,"与服务器连接失败",Toast.LENGTH_LONG).show();
                        //如果当前页面为SplashActivity则进行跳转
                        if (context instanceof SplashActivity) {

                            Activity activity = (Activity) context;
                            Intent intent = new Intent(activity, HomeActivity.class);
                            context.startActivity(intent);
                            //开启主界面后 结束导航界面
                            activity.finish();
                        }
                        break;
                    case JSON_ERROR:
                        Toast.makeText(context,"JSON解析异常",Toast.LENGTH_LONG).show();
                        //如果当前页面为SplashActivity则进行跳转
                        if (context instanceof SplashActivity) {

                            Activity activity = (Activity) context;
                            Intent intent = new Intent(activity, HomeActivity.class);
                            context.startActivity(intent);
                            //开启主界面后 结束导航界面
                            activity.finish();
                        }
                        break;
                }

            }
        };
        //因为请求网络为耗时操作，所以要放在子线程中
        new Thread(){
            @Override
            public void run() {
                //创建消息对象
                Message obtain = Message.obtain();
                //发送请求获取数据(记得网络权限)
                try {
                    //封装url地址
                    URL url = new URL("http://10.0.2.2:8080/updata.json");

                    //开启一个链接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //设置请求头
                    connection.setConnectTimeout(1000*3);//设置请求超时时限
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
                        //String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        if (getVersonCode()<Integer.parseInt(versionCode)) {
                            //如果有新版本
                            obtain.what =  UPDATE_VERSION;
                        }else {
                            //否则进入主界面
                            obtain.what = NORMAL;
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
                    mHandler.sendMessage(obtain);
                }
            }
        }.start();

    }
    /**
     *显示更新对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                //如果当前页面为SplashActivity则进行跳转
                if (context instanceof SplashActivity) {

                    Activity activity = (Activity) context;
                    Intent intent = new Intent(activity, HomeActivity.class);
                    context.startActivity(intent);
                    //开启主界面后 结束导航界面
                    activity.finish();
                }
            }
        });
        //用户点击回退键的相应事件
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (context instanceof SplashActivity) {

                    Activity activity = (Activity) context;
                    Intent intent = new Intent(activity, HomeActivity.class);
                    context.startActivity(intent);
                    //开启主界面后 结束导航界面
                    activity.finish();
                }
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
                    dialog = new ProgressDialog(context);
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
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
