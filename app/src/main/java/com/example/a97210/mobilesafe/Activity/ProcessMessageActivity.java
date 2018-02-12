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

import com.example.a97210.mobilesafe.Adapter.ProcessAdapter;
import com.example.a97210.mobilesafe.DataBase.Domain.ProcessInfo;
import com.example.a97210.mobilesafe.Engine.ProcessInfoProvider;
import com.example.a97210.mobilesafe.R;

import java.util.List;

/**
 *
 * Created by 97210 on 2018/2/11.
 */
public class ProcessMessageActivity extends Activity{

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
        List<ProcessInfo> processInfo = ProcessInfoProvider.getProcessInfo(mContext);
        //设置数据适配器
        lv_process_list.setAdapter(new ProcessAdapter(mContext,processInfo));
        //设置事件监听
        lv_process_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb_box = (CheckBox) view.findViewById(R.id.cb_box);
                cb_box.setChecked(!cb_box.isChecked());
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
    }
}
