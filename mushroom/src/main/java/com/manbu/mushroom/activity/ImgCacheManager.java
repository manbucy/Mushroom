package com.manbu.mushroom.activity;

import com.manbu.mushroom.util.MD5;
import com.manbu.mushroom.data.ZipImg;
import com.redare.imagepicker.widget.ImagePickerView;

import java.io.File;

/**
 * Created by ManBu
 * com.qiangyu.test.imagepickerviewdemo
 * 2016/9/5 13:51
 */
public class ImgCacheManager {
    private static String TAG = ImgCacheManager.class.getSimpleName();
    private String[] paths;
    private String fileDirPath;
    private String srcPath;
    private String fileName;
    private String aimPath;

    public void setFileDirPath(String fileDirPath) {
        this.fileDirPath = fileDirPath;
    }

    public void setImg(ImagePickerView imagePicker) {
        int n = imagePicker.getImageList().size();
        paths = new String[n + 1];

        srcPath = imagePicker.getImageList().get(0);
        fileName = MD5.getMD5For16(srcPath);
        aimPath = fileDirPath + "/" + fileName;
        File fileHead = new File(aimPath);
        if (!fileHead.exists()) {
            ZipImg.getImg(srcPath, aimPath, 240f, 240f);
        }
        paths[0] = aimPath;

        for (int i = 1; i <=n; i++) {
            srcPath = imagePicker.getImageList().get(i - 1);
            fileName = MD5.getMD5(srcPath);
            aimPath = fileDirPath + "/" + fileName;
            File file = new File(aimPath);
            if (!file.exists()) {
                ZipImg.getImg(srcPath, aimPath, 1080f, 720f);
            }
            paths[i] = aimPath;
        }

    }

    public String[] getPaths() {
        return paths;
    }


}
