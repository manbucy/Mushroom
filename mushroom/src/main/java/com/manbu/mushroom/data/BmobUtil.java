package com.manbu.mushroom.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.manbu.mushroom.activity.fragment.BuyFragment;
import com.manbu.mushroom.bean.Product;
import com.manbu.mushroom.common.MyApplication;
import com.manbu.mushroom.util.DateUtils;
import com.manbu.mushroom.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by ManBu
 * com.manbu.mushroom.util
 * 2016/9/7 13:16
 */
public class BmobUtil {
    private static Bitmap bitmap;
    private static List<Product> productList = new ArrayList<Product>();
    private static String TAG = "BmobUtil---";

    /**
     * 根据文件的URL下载文件
     *
     * @param fileUrl
     * @return bitmap
     */
    public static Bitmap downImg(String fileUrl) {
        String fileName = FileUtil.getFileNameSuffix(fileUrl);
        BmobFile bmobFile = new BmobFile(fileName, "", fileUrl);
        File savaFile = new File(MyApplication.getContext().getExternalCacheDir().getPath(),
                bmobFile.getFilename());
        if (savaFile.exists())
            return BitmapFactory.decodeFile(savaFile.getPath());
        bmobFile.download(MyApplication.getContext(), savaFile, new DownloadFileListener() {
            @Override
            public void onSuccess(String s) {
                bitmap = BitmapFactory.decodeFile(s);
            }

            @Override
            public void onFailure(int i, String s) {
                Log.d(TAG + "downImg", i + "onFailure: " + s);
            }
        });
        return bitmap;
    }

    /**
     * 查询20条最新的数据
     *
     * @return
     */
    public static List<Product> queryProd() {

        BmobQuery<Product> bmobQuery = new BmobQuery<Product>();
        bmobQuery.setLimit(20);
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(MyApplication.getContext(), new FindListener<Product>() {
            @Override
            public void onSuccess(List<Product> list) {
                productList = list;
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
                Log.d(TAG, "onSuccess:1 " + list.size());
                Log.d(TAG, "onSuccess:2 " + productList.size());
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG + "queryProd", i + "onError: " + s);
            }
        });
        Log.d(TAG, "onSuccess:3 " + productList.size());
        return productList;
    }

    /**
     * 查询指定时间到当前时间之间的数据
     *
     * @param startTime
     * @return
     * @throws Exception
     */
    public static List<Product> freshProd(String startTime) throws Exception {
        List<BmobQuery<Product>> and = new ArrayList<BmobQuery<Product>>();
        BmobQuery<Product> q1 = new BmobQuery<Product>();
        BmobQuery<Product> q2 = new BmobQuery<Product>();

        Date statrDate = DateUtils.stringToDate(startTime, DateUtils.PATTEN_HMS);
        Date currDate = new Date();
        q1.addWhereGreaterThan("createdAt", new BmobDate(statrDate));
        q2.addWhereLessThan("createdAt", new BmobDate(currDate));
        and.add(q1);
        and.add(q2);

        BmobQuery<Product> bmobQuery = new BmobQuery<Product>();
        bmobQuery.and(and);
        bmobQuery.findObjects(MyApplication.getContext(), new FindListener<Product>() {
            @Override
            public void onSuccess(List<Product> list) {
                productList = list;
//                Message message = new Message();
//                message.what = 2;
//                handler.sendMessage(message);
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG + "freshProd", i + "onError: " + s);
            }
        });

        return productList;
    }

    /**
     * 查询指定时间之前的20条数据
     *
     * @param endTime
     * @return
     * @throws Exception
     */
    public static List<Product> loadProd(String endTime) throws Exception {
        Date endDate = DateUtils.stringToDate(endTime, DateUtils.PATTEN_HMS);
        BmobQuery<Product> bmobQuery = new BmobQuery<Product>();
        bmobQuery.addWhereLessThan("createdAt", new BmobDate(endDate));
        bmobQuery.setLimit(20);
        bmobQuery.findObjects(MyApplication.getContext(), new FindListener<Product>() {
            @Override
            public void onSuccess(List<Product> list) {
                productList = list;
//                Message message = new Message();
//                message.what = 3;
//                handler.sendMessage(message);
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG + "freshProd", i + "onError: " + s);
            }
        });

        return productList;
    }

    public static List<Product> getProdResult() {

        return productList;
    }

//    public static android.os.Handler handler = new android.os.Handler() {
//        BuyFragment buyFragment = new BuyFragment();
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    buyFragment.queryData();
//                    break;
//                case 2:
//                    buyFragment.onfreshData();
//                    break;
//                case 3:
//                    buyFragment.onlodaData();
//                    break;
//            }
//        }
//    };



}
