package com.example.a97210.mobilesafe;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.a97210.mobilesafe.DataBase.dao.BlackNumberDao;
import com.example.a97210.mobilesafe.Engine.ProcessInfoProvider;

import static android.content.ContentValues.TAG;

/**
 * Created by 97210 on 2018/2/9.
 */

public class Test extends AndroidTestCase {

    public void testinsert() {
        BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
        dao.findAll();
    }
    public void testProcess() {
        long availSpace = ProcessInfoProvider.getAvailSpace(getContext());
        Log.i(TAG, "testProcess:availSpace "+availSpace);
    }


}
