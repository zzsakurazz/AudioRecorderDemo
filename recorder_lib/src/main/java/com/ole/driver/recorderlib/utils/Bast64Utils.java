package com.ole.driver.recorderlib.utils;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author zhangzheng
 * @Date 2020-02-26 18:20
 * @ClassName Utils
 * <p>
 * Desc :
 */
public class Bast64Utils {
    public static String fileToBase64(File file) {
        if (file != null && file.exists() && file.isFile()
                && file.length() < Integer.MAX_VALUE) {
            try {
                ByteArrayOutputStream os1 = new ByteArrayOutputStream();
                InputStream file1 = new FileInputStream(file);
                byte[] byteBuf = new byte[3 * 1024 * 1024];
                byte[] base64ByteBuf;
                byte[] copy;
                int count1; //每次从文件中读取到的有效字节数
                while ((count1 = file1.read(byteBuf)) != -1) {
                    //如果有效字节数不为3*1000，则说明文件已经读到尾了，不够填充满byteBuf了
                    if (count1 != byteBuf.length) {
                        //从byteBuf中截取包含有效字节数的字节段
                        copy = Arrays.copyOf(byteBuf, count1);
                        //对有效字节段进行编码
                        base64ByteBuf = Base64.encode(copy, Base64.DEFAULT);
                    } else {
                        base64ByteBuf = Base64.encode(byteBuf, Base64.DEFAULT);
                    }
                    os1.write(base64ByteBuf, 0, base64ByteBuf.length);
                    os1.flush();
                }
                String out = os1.toString();
                file1.close();
                os1.close();
                return out;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
