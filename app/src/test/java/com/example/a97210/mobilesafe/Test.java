package com.example.a97210.mobilesafe;

import android.test.AndroidTestCase;

import com.example.a97210.mobilesafe.DataBase.dao.BlackNumberDao;

/**
 * Created by 97210 on 2018/2/9.
 */

public class Test extends AndroidTestCase {

    public void testinsert() {
        BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
        dao.findAll();
    }


}
