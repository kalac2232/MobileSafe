package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.example.a97210.mobilesafe.R;

/**
 * Created by 97210 on 2018/2/9.
 */
public class BlackNumberActivity extends Activity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacknumber);
        context = this;
    }
}
