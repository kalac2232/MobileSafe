package com.example.a97210.mobilesafe.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a97210.mobilesafe.R;

/**
 * Created by 97210 on 2018/2/2.
 */

public class GridViewAdapter extends BaseAdapter {
    String[] titleStr;
    int[] drawableIds;
    Context context;
    public GridViewAdapter(Context context,String[]titleStr, int[] drawableIds) {
        this.titleStr = titleStr;
        this.drawableIds = drawableIds;
        this.context = context;
    }

    @Override
    public int getCount() {
        return titleStr.length;
    }

    @Override
    public Object getItem(int position) {
        return titleStr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.gridview_item, null);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(titleStr[position]);
        iv_icon.setBackgroundResource(drawableIds[position]);

        return view;
    }
}
