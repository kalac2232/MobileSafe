package com.example.a97210.mobilesafe.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a97210.mobilesafe.DataBase.Domain.ProcessInfo;
import com.example.a97210.mobilesafe.R;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 *
 * Created by 97210 on 2018/2/11.
 */

public class ProcessAdapter extends BaseAdapter {
    Context mContext;
    List<ProcessInfo> processInfo;
    public ProcessAdapter(Context context, List<ProcessInfo> processInfo) {
        mContext = context;
        this.processInfo = processInfo;
        Log.i(TAG, "ProcessAdapter: 构造方法执行了");
    }
    @Override
    public int getCount() {
        return processInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return processInfo.get(position);
    }
    // 每个convert view都会调用此方法，获得当前所需要的view样式
    @Override
    public int getItemViewType(int position) {

        if (processInfo.get(position) == null)
            return 1;
        else
            return 0;
    }
    //获取list View中样式的种类
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position , View convertView, ViewGroup parent) {

        ViewHolder_Des viewHolder_des = null;
        ViewHolder_Title viewHolder_title = null;

        int type = getItemViewType(position);

        if (convertView != null) {
            //type 1为标题 0为内容
            switch (type) {
                case 1:
                    viewHolder_title = (ViewHolder_Title)convertView.getTag();
                    break;
                case 0:
                    viewHolder_des = (ViewHolder_Des)convertView.getTag();
                    viewHolder_des.cb_box.setChecked(processInfo.get(position).isCheck);
                    break;
            }

        } else {
            //判断类型
            switch (type) {
                case 1:
                    viewHolder_title = new ViewHolder_Title();
                    viewHolder_title.textView = new TextView(mContext);
                    viewHolder_title.textView.setWidth(30);
                    viewHolder_title.textView.setTextSize(18);
                    viewHolder_title.textView.setTextColor(Color.BLACK);
                    //设置为不能点击
                    viewHolder_title.textView.setFocusable(true);
                    convertView = viewHolder_title.textView;
                    convertView.setTag(viewHolder_title);
                    break;
                case 0:
                    convertView = View.inflate(mContext, R.layout.listview_process_item,null);
                    viewHolder_des = new ViewHolder_Des();
                    viewHolder_des.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    viewHolder_des.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    viewHolder_des.tv_memory_info = (TextView) convertView.findViewById(R.id.tv_memory_info);
                    viewHolder_des.cb_box = (CheckBox) convertView.findViewById(R.id.cb_box);

                    convertView.setTag(viewHolder_des);
                    break;
            }


        }

        //设置数据
        switch (type) {
            case 1:
                if(processInfo.get(position) == null  && position == 0) {

                    viewHolder_title.textView.setText("用户程序");

                } else if (processInfo.get(position) == null) {

                    viewHolder_title.textView.setText("系统程序");

                }
                convertView = viewHolder_title.textView;
                break;
            case 0:
                viewHolder_des.iv_icon.setBackground(processInfo.get(position).icon);
                viewHolder_des.tv_name.setText(processInfo.get(position).name);
                long memSize = processInfo.get(position).memSize;
                String Size = Formatter.formatFileSize(mContext, memSize);
                viewHolder_des.tv_memory_info.setText(Size);
                viewHolder_des.cb_box.setChecked(processInfo.get(position).isCheck);
                break;
        }

        return convertView;
    }
    class ViewHolder_Des {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_memory_info;
        CheckBox cb_box ;
    }
    class ViewHolder_Title {
        TextView textView;
    }
}
