package com.manbu.mushroom.util;

import android.util.Log;

import java.io.File;

/**
 * 文件操作
 * Created by ManBu
 * com.qiangyu.test.imagepickerviewdemo
 * 2016/9/5 12:49
 */
public class FileUtil {
    public static String TAG="FileUtil";
    private FileUtil(){

    }

    /**
     * 根据文件路径获取文件的名称 含后缀名
     * @param filePath
     * @return
     */
    public static String getFileNameSuffix(String filePath) {

        int start = filePath.lastIndexOf("/");
        int end = filePath.length();
        if (start != -1 && end != -1) {
            return filePath.substring(start + 1, end);
        } else {
            return null;
        }

    }

    /**
     * 根据文件路径获取文件名 不含后缀
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {

        int start = filePath.lastIndexOf("/");
        int end = filePath.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return filePath.substring(start + 1, end);
        } else {
            return null;
        }

    }

    /**
     * 根据 路径 创建一个文件夹
     * @param fileDirPath
     */
    public static void createFileDir(String fileDirPath) {
        try {
            File dir = new File(fileDirPath);// 目录路径
            if (!dir.exists()) {
                Log.d(TAG, "createFileDir: dir is exists");
                if (dir.mkdirs()) {
                    Log.d(TAG, "createFileDir: create dir success");
                } else {
                    Log.d(TAG, "createFileDir: create dir failure");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
