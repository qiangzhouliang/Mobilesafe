package com.qzl.shoujiweishi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by Q on 2016-06-03.
 */
public class StreamUtil {
    /**
     * 将流信息转化成字符串
     * @return
     */
    public static String parserStreamUtil(InputStream in) throws IOException {
        //字符流，读取流
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        //写入流
        StringWriter sw = new StringWriter();
        //读写操作
        //创建一个数据缓冲区
        String str = null;
        while ((str = br.readLine()) != null){
            //写入操作
            sw.write(str);
        }
        //关流
        sw.close();
        br.close();
        //直接获取写入的信息
        return sw.toString();
    }
}
