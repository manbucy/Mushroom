package com.manbu.mushroom.util;

import java.util.UUID;

/**
 * Created by ManBu
 * com.qiangyu.test.imagepickerviewdemo
 * 2016/9/5 10:42
 */
public class UUIDGenerator {

    private UUIDGenerator() {
    }

    /**
     * 获取一个UUID
     * @return
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
        return temp;
    }

    /**
     * 获取指定数量的UUID
     * @param number
     * @return
     */
    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = getUUID();
        }
        return ss;
    }

}
