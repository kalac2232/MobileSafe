package com.example.a97210.mobilesafe.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
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

        ViewHolder1 viewHolder1 = null;
        ViewHolder2 viewHolder2 = null;

        int type = getItemViewType(position);

        if (convertView != null) {
            //type 1为标题 0为内容
            switch (type) {
                case 1:
                    viewHolder2 = (ViewHolder2)convertView.getTag();
                    break;
                case 0:
                    viewHolder1 = (ViewHolder1)convertView.getTag();
                    break;
            }

        } else {
            switch (type) {
                case 1:
                    viewHolder2 = new ViewHolder2();
                    viewHolder2.textView = new TextView(mContext);
                    viewHolder2.textView.setWidth(30);
                    viewHolder2.textView.setTextSize(18);
                    viewHolder2.textView.setTextColor(Color.BLACK);
                    viewHolder2.textView.setFocusable(true);
                    convertView = viewHolder2.textView;
                    convertView.setTag(viewHolder2);
                    break;
                case 0:
                    convertView = View.inflate(mContext, R.layout.listview_process_item,null);
                    viewHolder1 = new ViewHolder1();
                    viewHolder1.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    viewHolder1.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    viewHolder1.tv_memory_info = (TextView) convertView.findViewById(R.id.tv_memory_info);
                    viewHolder1.cb_box = (CheckBox) convertView.findViewById(R.id.cb_box);

                    convertView.setTag(viewHolder1);
                    break;
            }


        }

        //设置数据
        switch (type) {
            case 1:
                if(processInfo.get(position) == null  && position == 0) {

                    viewHolder2.textView.setText("用户程序");

                } else if (processInfo.get(position) == null) {

                    viewHolder2.textView.setText("系统程序");

                }
                convertView = viewHolder2.textView;
                break;
            case 0:
                viewHolder1.iv_icon.setBackground(processInfo.get(position).icon);
                viewHolder1.tv_name.setText(processInfo.get(position).name);
                long memSize = processInfo.get(position).memSize;
                String Size = Formatter.formatFileSize(mContext, memSize);
                viewHolder1.tv_memory_info.setText(Size);

                break;
        }

        return convertView;
    }
    class ViewHolder1{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_memory_info;
        CheckBox cb_box ;
    }
    class ViewHolder2{
        TextView textView;
    }
}
