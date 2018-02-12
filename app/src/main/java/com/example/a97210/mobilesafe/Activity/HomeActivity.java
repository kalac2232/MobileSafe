package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.a97210.mobilesafe.Adapter.GridViewAdapter;
import com.example.a97210.mobilesafe.R;
import com.example.a97210.mobilesafe.Utils.ConstantValue;
import com.example.a97210.mobilesafe.Utils.SharePreferenceUtil;

/**
 *
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
                        showDialog();
                        break;
                    case 1:
                        //跳转到黑名单页
                        startActivity(new Intent(mContext,BlackNumberActivity.class));
                        break;
                    case 2:
                        break;
                    case 3:
                        //跳转到进程管理
                        startActivity(new Intent(mContext,ProcessMessageActivity.class));
                        break;
                    case 4:

                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        //跳转到工具页
                        startActivity(new Intent(mContext,ToolsActivity.class));

                        break;
                    case 8:
                        //跳转到设置页面
                        startActivity(new Intent(mContext,SettingActivity.class));
                        break;
                }
            }
        });
    }
    private void showDialog(){
        String psw = SharePreferenceUtil.getString(mContext, ConstantValue.MOBILE_SAFE_PSW, "");
        if (TextUtils.isEmpty(psw)) {
            showSetPswDialog();
        } else {
            showConfirmPswDialog();
        }
    }

    /**
     * 确认密码的对话框
     */
    private void showConfirmPswDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //自行创建对话框xml文件 转成view 设置给对话框
        final AlertDialog alertDialog = builder.create();
        //将对应的xml文件转换成view
        final View view = View.inflate(mContext, R.layout.dialog_confirm_psw, null);
        //设置给对话框
        alertDialog.setView(view);
        alertDialog.show();
        //找控件

        Button bt_submit = (Button) view.findViewById(R.id.bt_enter_submit);
        //设置监听事件
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_enter_psd = (EditText) view.findViewById(R.id.et_enter_psd);
                String enter_psd = et_enter_psd.getText().toString();
                String safe_psw = SharePreferenceUtil.getString(mContext, ConstantValue.MOBILE_SAFE_PSW, "");
                if (TextUtils.isEmpty(enter_psd)||!safe_psw.equals(enter_psd)) {
                    Toast.makeText(mContext,"输入为空或错误，请重新输入",Toast.LENGTH_LONG).show();
                } else {
                    //进入手机防盗模块
                    Intent intent = new Intent(mContext, SetupOverActivity.class);
                    startActivity(intent);

                    //跳转后 关闭对话框
                    alertDialog.dismiss();
                }
            }
        });
        Button bt_cancel = (Button) view.findViewById(R.id.bt_enter_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * 设置初次进入的密码框
     */
    private void showSetPswDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //自行创建对话框xml文件 转成view 设置给对话框
        final AlertDialog alertDialog = builder.create();
        //将对应的xml文件转换成view
        final View view = View.inflate(mContext, R.layout.dialog_set_psw, null);
        //设置给对话框
        alertDialog.setView(view);
        alertDialog.show();
        //找控件

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        //设置监听事件
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);
                EditText et_set_psw = (EditText) view.findViewById(R.id.et_set_psw);
                String confirm_psd = et_confirm_psd.getText().toString();
                String set_psw = et_set_psw.getText().toString();
                if (TextUtils.isEmpty(confirm_psd)||TextUtils.isEmpty(set_psw)||!confirm_psd.equals(set_psw)) {
                    Toast.makeText(mContext,"输入为空或错误，请重新输入",Toast.LENGTH_LONG).show();
                } else {
                    //进入手机防盗模块
                    Intent intent = new Intent(mContext, SetupOverActivity.class);
                    startActivity(intent);
                    SharePreferenceUtil.putString(mContext,ConstantValue.MOBILE_SAFE_PSW,confirm_psd);
                    //跳转后 关闭对话框
                    alertDialog.dismiss();
                }
            }
        });
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }
}
