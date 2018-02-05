package com.example.a97210.mobilesafe.Service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.a97210.mobilesafe.Utils.ConstantValue;
import com.example.a97210.mobilesafe.Utils.SharePreferenceUtil;

/**
 *
 * Created by 97210 on 2018/2/4.
 */

public class SecurityService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static final String TAG = "SecurityService";
    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: -------------------SecurityService-----------------------");
        super.onCreate();
        //获取位置管理者
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //设置最优的获取坐标的方法
        Criteria criteria = new Criteria();
        //允许使用收费的网络
        criteria.setCostAllowed(true);
        //设置精确度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = locationManager.getBestProvider(criteria, true);
        //if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {

       // locationManager.requestLocationUpdates(bestProvider, 0, 0, new MyLocationListener());
        //}
    }
    class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            //String phone = SharePreferenceUtil.getString(, ConstantValue.SAFE_PHONENUMBER, "");
            Log.i(TAG, "onLocationChanged: "+longitude+latitude);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("5556",null,"手机被偷啦！！！！"+longitude+latitude,null,null);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
