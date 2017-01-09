package com.manbu.mushroom.common;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.manbu.mushroom.bean.MyUser;

import cn.bmob.v3.BmobUser;

/**
 * Created by ManBu
 * com.manbu.mushroom.util
 * 2016/9/7 14:06
 */
public class MyApplication extends Application {
    private static Context context;
    public static Toast mToast;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }


    /**
     *
     * @Title: ShowToastS
     * @Description: TODO 展示Short Toast
     * @param @param text  需要展示的内容
     * @return void
     * @throws
     */
    public static void showToastS(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(context, text,Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    /**
     *
     * @Title: ShowToastL
     * @Description: TODO 展示Long Toast
     * @param @param text 需要展示的内容
     * @return void
     * @throws
     */
    public static void showToastL(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(context, text,Toast.LENGTH_LONG);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    /**
     *
     * @return
     */
    public static MyUser getUser() {

        return BmobUser.getCurrentUser(context, MyUser.class);
    }

}
