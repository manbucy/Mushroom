package com.manbu.mushroom.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.manbu.mushroom.R;
import com.manbu.mushroom.bean.Product;
import com.manbu.mushroom.common.Constants;
import com.manbu.mushroom.data.BmobUtil;
import com.manbu.mushroom.ui.citypicter.CityPickerDialog;
import com.manbu.mushroom.ui.citypicter.Tools;
import com.manbu.mushroom.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import c.b.BP;
import c.b.PListener;

/**
 * Created by yang on 2016/10/23.
 */

public class OrderConfirmActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView mImagePic;
    private TextView mTextName;
    private TextView mTextAddress;
    private TextView mTextSupport;
    private TextView mTextSpec;
    private TextView mTextPrice;
    private TextView mTextNums;
    private Product mProduct;

    private TextView mOrderBuyer;
    private TextView mOrderPhone;
    private TextView mOrderAddress1;
    private TextView mOrderAddress2;
    private TextView mOrderRemark;
    private TextView mOrderNum;
    private TextView mOrderPrice;
    private TextView mOrderPriceInfo;
    private Button mOrderSubmit;

    private ProgressDialog dialog;
    int PLUGINVERSION = 7;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_confirm);
        // 必须先初始化
        BP.init(this, Constants.BMOB_APPID);
        BP.ForceFree();
        initView();
        initData();
        int pluginVersion = BP.getPluginVersion();
        if (pluginVersion < PLUGINVERSION) {// 为0说明未安装支付插件, 否则就是支付插件的版本低于官方最新版
            Toast.makeText(
                    OrderConfirmActivity.this,
                    pluginVersion == 0 ? "监测到本机尚未安装支付插件,无法进行支付,请先安装插件(无流量消耗)"
                            : "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)",
                    Toast.LENGTH_SHORT).show();
            installBmobPayPlugin("bp.db");
        }

    }

    private void initData() {
        mProduct = (Product) getIntent().getSerializableExtra(ProductInfoActivity.KEY);
        Bitmap mHead = BmobUtil.downImg(mProduct.getProdImgs().get(0));
        mImagePic.setImageBitmap(mHead);
        mTextName.setText(mProduct.getProdName());
        mTextSpec.setText(mProduct.getProdSpec());
        mTextPrice.setText(mProduct.getProdPrice().toString());
        mTextAddress.setText(mProduct.getProdAddress());
        mTextSupport.setText(mProduct.getProdSupport());
    }

    private void initView() {
        mImagePic = (ImageView) findViewById(R.id.prod_img);
        mTextName = (TextView) findViewById(R.id.prod_name);
        mTextAddress = (TextView) findViewById(R.id.prod_address);
        mTextSupport = (TextView) findViewById(R.id.prod_support);
        mTextSpec = (TextView) findViewById(R.id.prod_spec);
        mTextPrice = (TextView) findViewById(R.id.prod_price);
        mTextNums = (TextView) findViewById(R.id.prod_time);

        mOrderBuyer = (TextView) findViewById(R.id.order_buyer);
        mOrderPhone = (TextView) findViewById(R.id.order_phone);
        mOrderAddress1 = (TextView) findViewById(R.id.order_address1);
        mOrderAddress1.setOnClickListener(this);
        mOrderAddress2 = (TextView) findViewById(R.id.order_address2);
        mOrderRemark = (TextView) findViewById(R.id.order_remark);
        mOrderNum = (TextView) findViewById(R.id.order_num);
        mOrderNum.setOnClickListener(this);
        mOrderPrice = (TextView) findViewById(R.id.order_prices);
        mOrderPriceInfo = (TextView) findViewById(R.id.order_price_info);
        mOrderSubmit = (Button) findViewById(R.id.order_submit);
        mOrderSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_num :
                 final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                new AlertDialog.Builder(this).setTitle("输入数量")
                        .setIcon(R.drawable.ic_launcher)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mOrderNum.setText(et.getText().toString());
                                Float prices ;
                                int num;
                                num = Integer.valueOf(et.getText().toString());
                                prices = mProduct.getProdPrice()*num;
                                mOrderPriceInfo.setVisibility(View.VISIBLE);
                                mOrderPrice.setText(prices.toString());
                                mTextNums.setText("×"+num);
                            }
                        })
                        .setNegativeButton("取消",null).show();
                break;
            case R.id.order_address1 :
                if (!Tools.isFastDoubleClick()) {
                    new CityPickerDialog(OrderConfirmActivity.this, new CityPickerDialog.OnCityPikerListener() {
                        @Override
                        public void onCityPicker(String province, String city, String district) {
                            mOrderAddress1.setText(province + city + district);
                        }
                    }).show();
                }
                break;
            case R.id.order_submit ://提交订单 开启支付业务
//                Toast.makeText(getApplicationContext(),"zhifuchenggong",Toast.LENGTH_SHORT).show();
//                if(isNotEmpty()){
                    pay(false);
//                }

                break;
        }
    }

//    private Boolean isNotEmpty() {
//
//        if(StringUtils.isEmpty(mOrderBuyer.toString())){
//            showToastS("收货人姓名不能为空");
//            return false;
//        }
//        if(StringUtils.isEmpty(mOrderPhone.toString())){
//            showToastS("收货人电话不能为空");
//            return false;
//        }
//        if(StringUtils.isEmpty(mOrderAddress1.toString()) ||
//                StringUtils.isEmpty(mOrderAddress2.toString()) ){
//            showToastS("收货地址不能为空");
//            return false;
//        }
//        if(StringUtils.isEmpty(mOrderNum.toString())){
//            showToastS("数量不能为空");
//            return false;
//        }
//        return true;
//    }


    public Toast mToast;

    public void showToastS(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text,Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    /**
     * 调用支付
     *
     * @param alipayOrWechatPay
     *            支付类型，true为支付宝支付,false为微信支付
     */
    void pay(final boolean alipayOrWechatPay) {
        showDialog("正在获取订单...");
        final String name = getName();

        try{
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.bmob.app.sport","com.bmob.app.sport.wxapi.BmobActivity");
            intent.setComponent(cn);
            this.startActivity(intent);
        }catch(Throwable e){
            e.printStackTrace();
        }
        BP.pay(name, getBody(), getPrice(), alipayOrWechatPay, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(OrderConfirmActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
                        .show();
                hideDialog();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(OrderConfirmActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();

                hideDialog();
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
//                order.setText(orderId);
//                tv.append(name + "'s orderid is " + orderId + "\n\n");
                showDialog("获取订单成功!请等待跳转到支付页面~");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {

                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    Toast.makeText(
                            OrderConfirmActivity.this,
                            "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                            Toast.LENGTH_SHORT).show();
                    installBmobPayPlugin("bp.db");
                } else {
                    Toast.makeText(OrderConfirmActivity.this, "支付中断!", Toast.LENGTH_SHORT)
                            .show();
                }
//                tv.append(name + "'s pay status is fail, error code is \n"
//                        + code + " ,reason is " + reason + "\n\n");
                Log.i("----manbu",reason);
                hideDialog();
            }
        });
    }

    // 默认为0.02
    double getPrice() {
        double price = 0.02;
        try {
            price = Double.parseDouble(this.mOrderPrice.getText().toString());
        } catch (NumberFormatException e) {
            Log.i("----manbu",""+e);
        }
        Log.i("----manbu",""+price);
        return price;
    }

    // 商品详情(可不填)
    String getName() {
        return this.mTextName.getText().toString();
    }

    // 商品详情(可不填)
    String getBody() {
        return this.mTextSpec.getText().toString();
    }

//    // 支付订单号(查询时必填)
//    String getOrder() {
//        return this.order.getText().toString();
//    }

    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }

    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
