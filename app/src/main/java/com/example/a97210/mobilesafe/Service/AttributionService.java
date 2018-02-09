package com.example.a97210.mobilesafe.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.a97210.mobilesafe.Engine.AddressDao;
import com.example.a97210.mobilesafe.R;

import static android.content.ContentValues.TAG;


/**
 *
 * Created by 97210 on 2018/2/8.
 */

public class AttributionService extends Service {

    private TelephonyManager mTM;
    private MyPhoneStateListener myPhoneStateListener;
    private  final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private WindowManager mWM;
    private View mViewToast;
    private String address;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (address == null) {
                tv_toast.setText("未知号码");
            } else {
                tv_toast.setText(address);
            }
        }
    };
    private TextView tv_toast;
    private InnerOutCallReceiver mInnerOutCallReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //创建电话管理者对象
        mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //监听电话状态
        myPhoneStateListener = new MyPhoneStateListener();
        mTM.listen(myPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        //监听播出电话的广播过滤条件
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);

        //创建广播接收者
        mInnerOutCallReceiver = new InnerOutCallReceiver();
        registerReceiver(mInnerOutCallReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTM != null && myPhoneStateListener !=null) {
            //取消监听
            mTM.listen(myPhoneStateListener,PhoneStateListener.LISTEN_NONE);

        }
        if (mInnerOutCallReceiver != null) {
            unregisterReceiver(mInnerOutCallReceiver);
        }
    }
    //创建接受去电广播的广播接收者
    class InnerOutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String phone = getResultData();
            showTosat(phone);
        }
    }


    class MyPhoneStateListener extends PhoneStateListener{
        @Override
        //当电话监听状态发生改变调用此方法
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mWM != null && mViewToast != null) {
                        mWM.removeView(mViewToast);
                    }
                    //Toast.makeText(getApplicationContext(),"空闲状态",Toast.LENGTH_SHORT).show();
                    //空闲状态

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //摘机状态
                    //Toast.makeText(getApplicationContext(),"摘机状态",Toast.LENGTH_SHORT).show();
                    //showTosat(incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //响铃状态
                    //Toast.makeText(getApplicationContext(),"响铃状态",Toast.LENGTH_SHORT).show();
                    showTosat(incomingNumber);
                    break;
            }
        }

    }
    public void showTosat(String incomingNumnber) {
        final WindowManager.LayoutParams params = mParams;
        //设置toast宽高
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;

        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE//不能获取焦点
                |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;//在屏幕开启时显示

        params.format = PixelFormat.TRANSLUCENT;
        //在响铃的时候进行吐司
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        //指定吐司的所在位置
        params.gravity = Gravity.CENTER_HORIZONTAL+Gravity.TOP;
        mViewToast = View.inflate(this, R.layout.toast_view, null);
        //设置移动监听
        mViewToast.setOnTouchListener(new View.OnTouchListener() {
            int startX = 0;
            int startY = 0;
            int endX;
            int endY;
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        Log.i(TAG, "onTouch: ACTION_DOWN执行了");
                        Log.i(TAG, "ACTION_DOWN: startX:"+startX);
                        Log.i(TAG, "ACTION_DOWN: startY:"+startY);
                        break;
                    case MotionEvent.ACTION_MOVE:

                        endX = (int) event.getX();
                        endY = (int) event.getY();

                        int disX = endX - startX;
                        int disY = endY - startY;
                        Log.i(TAG, "ACTION_MOVE: disX:"+disX);
                        Log.i(TAG, "ACTION_MOVE: disY:"+disY);
                        startX = (int) event.getX();
                        startY = (int) event.getY();

                        params.x = params.x + disX;
                        params.y = params.y +disY;
//                        if (params.x < 0) {
//                            params.x = 0;
//                        }
//                        if (params.y < 0) {
//                            params.y = 0;
//                        }
//                        if (params.x + mViewToast.getWidth()> mWM.getDefaultDisplay().getWidth()) {
//                            params.x = mWM.getDefaultDisplay().getWidth() - mViewToast.getWidth();
//                        }
//                        if (params.y + mViewToast.getHeight()> mWM.getDefaultDisplay().getHeight()) {
//                            params.y = mWM.getDefaultDisplay().getHeight() - mViewToast.getHeight();
//                        }
                        //更新窗体
                        mWM.updateViewLayout(mViewToast,params);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "onTouch: ACTION_Up执行了");
                        break;
                }
                return true;
            }
        });
        tv_toast = (TextView) mViewToast.findViewById(R.id.tv_toast);
        //查询归属地
        query(incomingNumnber);
        //在窗体上挂载一个吐司
        mWM.addView(mViewToast,params);
    }
    private void query(final String incomingNumnber) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                address = AddressDao.getAddress(incomingNumnber);
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }
}
