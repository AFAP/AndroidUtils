package com.afap.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件操作工具类
 */
public class FileUtil {

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
}