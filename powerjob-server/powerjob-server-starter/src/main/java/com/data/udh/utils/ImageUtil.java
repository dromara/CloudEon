package com.data.udh.utils;

import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    /**
     * 读取图片转base64
     * @param imgFile
     * @return
     */
    public static String GetImageStr(String imgFile) {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        //读取图片字节数组
        try (InputStream in = new FileInputStream(imgFile);) {
            data = new byte[in.available()];
            in.read(data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }

    public static void main(String[] args) {
        System.out.println(GetImageStr("/Volumes/Samsung_T5/opensource/e-mapreduce/stack/UDH-1.0.0/hdfs/icons/danger1.png"));
    }

}
