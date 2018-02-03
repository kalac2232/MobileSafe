package com.example.a97210.mobilesafe.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 97210 on 2018/2/1.
 */
public class StreamUtil {


    /**
     * 流转换成字符串
     * @param is 流对象
     * @return 流转换成的字符串 返回null表示异常
     */
    public static String streamToString(InputStream is) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bufffer = new byte[1024];
        //记录读取内容的临时变量
        int temp = -1;
        try {
            while ((temp = is.read(bufffer))!=-1) {
                bos.write(bufffer,0,temp);
            }
            return bos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
