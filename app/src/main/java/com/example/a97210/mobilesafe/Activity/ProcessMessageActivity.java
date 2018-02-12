package com.example.a97210.mobilesafe.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a97210.mobilesafe.Adapter.ProcessAdapter;
import com.example.a97210.mobilesafe.DataBase.Domain.ProcessInfo;
import com.example.a97210.mobilesafe.Engine.ProcessInfoProvider;
import com.example.a97210.mobilesafe.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by 97210 on 2018/2/11.
 */
public class ProcessMessageActivity extends Activity implements View.OnClickListener{

    private Context mContext;
    private TextView tv_process_count;
    private TextView tv_memory_info;
    private Button bt_select_all;
    private Button bt_select_reverse;
    private Button bt_clear;
    private Button bt_setting;
    private int mProcessCount;
    private long mAvailSpace;
    private String mStrTotalSpace;
    private ListView lv_process_list;
    private List<ProcessInfo> processInfo;
    private ProcessAdapter processAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processmessage);
        mContext = this;
        initUI();
        initTitleData();
        initListView();
    }

    private void initListView() {
        processInfo = ProcessInfoProvider.getProcessInfo(mContext);
        //设置数据适配器
        processAdapter = new ProcessAdapter(mContext, processInfo);
        lv_process_list.setAdapter(processAdapter);
        //设置事件监听
        lv_process_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb_box = (CheckBox) view.findViewById(R.id.cb_box);
                cb_box.setChecked(!cb_box.isChecked());
                processInfo.get(position).isCheck = cb_box.isChecked();
            }
        });
    }

    private void initTitleData() {
        mProcessCount = ProcessInfoProvider.getProcessCount(this);
        tv_process_count.setText("进程总数:"+mProcessCount);

        //获取可用内存大小,并且格式化
        mAvailSpace = ProcessInfoProvider.getAvailSpace(this);
        String strAvailSpace = Formatter.formatFileSize(this, mAvailSpace);

        //总运行内存大小,并且格式化
        long totalSpace = ProcessInfoProvider.getTotalSpace(this);
        mStrTotalSpace = Formatter.formatFileSize(this, totalSpace);

        tv_memory_info.setText("剩余/总共:"+strAvailSpace+"/"+mStrTotalSpace);
    }

    private void initUI() {
        tv_process_count = (TextView) findViewById(R.id.tv_process_count);
        tv_memory_info = (TextView) findViewById(R.id.tv_memory_info);
        bt_select_all = (Button) findViewById(R.id.bt_select_all);
        bt_select_reverse = (Button) findViewById(R.id.bt_select_reverse);
        bt_clear = (Button) findViewById(R.id.bt_clear);
        bt_setting = (Button) findViewById(R.id.bt_setting);
        lv_process_list = (ListView) findViewById(R.id.lv_process_list);
        //添加监听
        bt_select_all.setOnClickListener(this);
        bt_select_reverse.setOnClickListener(this);
        bt_clear.setOnClickListener(this);
        bt_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //全选
            case R.id.bt_select_all:
                for(int i = 0;i<processInfo.size();i++) {
                    if (processInfo.get(i) != null) {
                        processInfo.get(i).isCheck = true;
                        processAdapter.notifyDataSetChanged();
                    }
                }
                break;
            //反选
            case R.id.bt_select_reverse:
                for(int i = 0;i<processInfo.size();i++) {
                    if (processInfo.get(i) != null) {
                        processInfo.get(i).isCheck = !processInfo.get(i).isCheck;
                        processAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case R.id.bt_clear:
                cleanAll();
                break;
            case R.id.bt_setting:
                break;
        }
    }

    private void cleanAll() {
        ArrayList<ProcessInfo> killList = new ArrayList<>();
        for (int i = 0;i<processInfo.size();i++) {
            if (processInfo.get(i) != null && processInfo.get(i).isCheck) {
                killList.add(processInfo.get(i));
            }
        }
        for (int i = 0; i < killList.size(); i++) {
            if (processInfo.contains(killList.get(i))) {
                processInfo.remove(killList.get(i));
                ProcessInfoProvider.killProcess(mContext,killList.get(i));
            }
        }
        //刷新适配器
        processAdapter.notifyDataSetChanged();
        //更新界面
        initTitleData();

        initListView();
        Toast.makeText(mContext,"杀死了"+killList.size()+"个进程",Toast.LENGTH_SHORT).show();
    }
}
