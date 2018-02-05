package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.a97210.mobilesafe.R;

/**
 *
 * Created by 97210 on 2018/2/5.
 */
public class BaseSetupActivity extends Activity{

    private GestureDetector.SimpleOnGestureListener onGestureListener;
    private static final String TAG = "BaseSetupActivity";
    private GestureDetector gestureDetector;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onGestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getRawX()-e2.getRawX()>100) {

                    nextPage();
                }
                if (e2.getRawX()-e1.getRawX()>100) {
                    showPrePage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        };
        gestureDetector = new GestureDetector(this,onGestureListener);

    }
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    private void showPrePage() {
        Log.i(TAG, "showPrePage: 向右滑动");
    }

    private void nextPage() {

    }
}
