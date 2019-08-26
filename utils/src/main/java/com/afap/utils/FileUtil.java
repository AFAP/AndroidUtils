package com.afap.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * 文件操作工具类
 */
public class FileUtil {


    public String getStringFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            String result = "";
            while ((line = bufReader.readLine()) != null)
                result += line;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串写入文件
     *
     * @param file    文件
     * @param content 内容
     * @param append  是否追加在文件末
     * @return true:成功;false:失败
     */
    public static boolean writeStringToFile(File file, String content, boolean append) {
        if (file == null || !file.exists() || !file.isFile() || content == null) {
            return false;
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, append));
            bw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 格式化文件大小
     *
     * @param fileSize 字节
     * @return 文件大小
     */
    public static String FormatFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#0.00");
        String s = "";
        if (fileSize < 1024) {
            s = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            s = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            s = df.format((double) fileSize / 1048576) + "M";
        } else {
            s = df.format((double) fileSize / 1073741824) + "G";
        }
        return s;
    }

    /**
     * 校验文件名合法性
     *
     * @param fileName 文件名
     * @return 是否合法
     */
    public static boolean isFileNameValidate(String fileName) {
        if (TextUtils.isEmpty(fileName) || fileName.length() > 255) {
            return false;
        } else {
            return fileName
                    .matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])" +
                            "*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
        }
    }
}
