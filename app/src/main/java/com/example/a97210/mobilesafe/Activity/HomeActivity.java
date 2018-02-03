package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.a97210.mobilesafe.Adapter.GridViewAdapter;
import com.example.a97210.mobilesafe.R;

/**
 * Created by 97210 on 2018/2/1.
 */
public class HomeActivity extends Activity{

    private GridView gv_home;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        initUI();
        initData();
    }

    /**
     * 准备数据
     */
    private void initData() {
        String[] titleStr = {
                "手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"
        };
        int[] drawableIds = {
            R.drawable.home_safe,R.drawable.home_callmsgsafe, R.drawable.home_apps,
                R.drawable.home_taskmanager, R.drawable.home_netmanager,
                R.drawable.home_trojan, R.drawable.home_sysoptimize,R.drawable.home_tools,
                R.drawable.home_settings
        };
        //九宫格控件设置数据适配器
        gv_home.setAdapter(new GridViewAdapter(mContext,titleStr,drawableIds));
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        //跳转到设置页面
                        Intent intent = new Intent(mContext, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }
}
