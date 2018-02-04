package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.a97210.mobilesafe.R;

/**
 *
 * Created by 97210 on 2018/2/4.
 */
public class Setup1Activity extends Activity{

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        mContext = this;
    }
    public void nextPage(View view) {
        Intent intent = new Intent(mContext, Setup2Activity.class);
        startActivity(intent);
        finish();
    }
}
