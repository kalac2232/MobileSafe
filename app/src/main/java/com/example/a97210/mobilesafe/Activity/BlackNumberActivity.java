package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a97210.mobilesafe.DataBase.Domain.BlackNumberInfo;
import com.example.a97210.mobilesafe.DataBase.dao.BlackNumberDao;
import com.example.a97210.mobilesafe.R;

import java.util.ArrayList;

import java.util.logging.LogRecord;

/**
 *
 * Created by 97210 on 2018/2/9.
 */
public class BlackNumberActivity extends Activity {

    private Context mContext;
    private Button bt_add;
    private ListView lv_blacknumber;
    private BlackNumberDao mBlackNumberDao;
    private ArrayList<BlackNumberInfo> blackNumberInfoArrayList;
    int mode = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //设置数据适配器
            myAdapter = new MyAdapter();
            lv_blacknumber.setAdapter(myAdapter);
        }
    };
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacknumber);
        mContext = this;
        init();
        initData();
    }

    private void initData() {
        new Thread() {

            public void run() {
                mBlackNumberDao = BlackNumberDao.getInstance(mContext);
                blackNumberInfoArrayList = mBlackNumberDao.findAll();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void init() {
        bt_add = (Button) findViewById(R.id.bt_add);
        lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }
    //显示添加对话框
    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(mContext, R.layout.dialog_add_blacknumber, null);
        //输入的电话号
        final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
        //单选框
        RadioGroup rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
        //对单选框的改变进行监听
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_sms:
                        mode = 1;
                        break;
                    case R.id.rb_phone:
                        mode = 2;
                        break;
                    case R.id.rb_all:
                        mode = 3;
                        break;
                }
            }
        });
        //确定和关闭按钮
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        //确认的点击事件
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    //插入数据
                    mBlackNumberDao.insert(phone,mode);
                    //向listview中插入新的数据
                    BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                    blackNumberInfo.phone = phone;
                    blackNumberInfo.mode = mode;
                    //插入到listview的顶部
                    blackNumberInfoArrayList.add(0,blackNumberInfo);
                    //刷新数据适配器
                    if (myAdapter != null) {
                        myAdapter.notifyDataSetChanged();
                    }
                    //隐藏对话框
                    dialog.dismiss();
                } else {
                    Toast.makeText(mContext,"请输入拦截号码",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //取消对话框
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(view,0,0,0,0);
        dialog.show();
    }

    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return blackNumberInfoArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return blackNumberInfoArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;
            //复用converView优化listview 简单来说就是新出来的View使用消失的View的空间 不再重新申请空间
            if (convertView != null) {
                view = convertView;
            }else {
                //可以选择一个xml文件当做View进行显示
                view = View.inflate(mContext, R.layout.listview_blacknumber_item, null);
            }
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode);
            ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);

            tv_phone.setText(blackNumberInfoArrayList.get(position).phone);
            //tv_mode.setText(blackNumberInfoArrayList.get(position).mode);
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBlackNumberDao.delete(blackNumberInfoArrayList.get(position).phone);
                    //移除列表中的对象
                    blackNumberInfoArrayList.remove(position);
                    //刷新数据适配器
                    if (myAdapter != null) {
                        myAdapter.notifyDataSetChanged();
                    }
                }
            });

            switch (blackNumberInfoArrayList.get(position).mode) {
                case 1:
                    tv_mode.setText("拦截短信");
                    break;
                case 2:
                    tv_mode.setText("拦截电话");
                    break;
                case 3:
                    tv_mode.setText("拦截所有");
                    break;
            }

            return view;
        }
    }
}
