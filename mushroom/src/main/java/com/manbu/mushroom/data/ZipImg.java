package com.manbu.mushroom.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by ManBu
 * com.qiangyu.test.imagepickerviewdemo
 * 2016/9/5 12:38
 */
public class ZipImg {
    private ZipImg() {

    }

    /**
     * 先把图片进行 比例压缩 然后再进行质量压缩
     * @param srcPath 图片文件路径
     * @param aimPath 压缩好后的存储路径
     * @param hres 压缩的 高
     * @param wRes 压缩的 宽
     */
    public static void getImg(String srcPath, String aimPath, Float hres, Float wRes) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts); //此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为
        float hh = hres;//这里设置高度为1280f
        float ww = wRes;//这里设置宽度为720f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        compressImage(bitmap, aimPath);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 把 bitmap 格式的图片进行质量压缩
     * @param image
     * @param aimPath 压缩好后的存储路径
     */
    public static void compressImage(Bitmap image, String aimPath) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;

        //循环判断如果压缩后图片是否大于200kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 200) {

            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10

            //这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);

        }
        File file = new File(aimPath);
        if (!file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
