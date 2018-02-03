package com.example.a97210.mobilesafe.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a97210.mobilesafe.R;
import com.example.a97210.mobilesafe.Utils.ConstantValue;

import static android.content.ContentValues.TAG;

/**
 *
 * Created by 97210 on 2018/2/2.
 */

public class SettingItemView extends RelativeLayout {

    private CheckBox cb_box;
    private TextView tv_des;
    private String title;
    private String desoff;
    private String deson;
    private TextView tv_title;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //将设置界面的一个条目转换成View对象，直接添加到了当前的SettingItemView对应的View中
        View.inflate(context, R.layout.setting_item_view,this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        cb_box.setClickable(false);
        initAttrs(attrs);

        setText();

    }

    /**
     * 设置文本文字
     */
    private void setText() {
        tv_title.setText(title);
        if (cb_box.isChecked()) {
            tv_des.setText(deson);
        }else {
            tv_des.setText(desoff);
        }
    }

    /**
     * 取得属性值
     * @param attrs 属性集合
     */
    private void initAttrs(AttributeSet attrs) {
        title = attrs.getAttributeValue(ConstantValue.NAMESPACE, "title");
        desoff = attrs.getAttributeValue(ConstantValue.NAMESPACE, "desoff");
        deson = attrs.getAttributeValue(ConstantValue.NAMESPACE, "deson");
    }

    /**
     * 获取CheckBox状态
     * @return 当前CheckBox状态
     */
    public boolean isCheck() {
        return cb_box.isChecked();
    }
    public void setCheck(Boolean isCheck) {
        cb_box.setChecked(isCheck);
        setText();
    }
    /**
     * 更改选中后的状态
     */
    public void changeCheckBoxStatus (){
        cb_box.setChecked(!cb_box.isChecked());
        setText();
    }
}
