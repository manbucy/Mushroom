package com.manbu.mushroom.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.manbu.mushroom.R;
import com.manbu.mushroom.ui.citypicter.CityPickerDialog;
import com.manbu.mushroom.ui.citypicter.CityPickerDialog.OnCityPikerListener;
import com.manbu.mushroom.ui.citypicter.Tools;
import com.manbu.mushroom.common.BaseActivity;
import com.manbu.mushroom.bean.Product;
import com.redare.imagepicker.widget.ImagePickerView;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;


public class SellAddActivity extends BaseActivity {

    private TextView mTextLeft;
    private TextView mTextTitle;
    private Button mBtnAdd;
    private TextView mTextAddress;
    private ImagePickerView imagePicker;

    private EditText mEditProdName;
    private EditText mEditProdSpec;
    private EditText mEditProdPrice;
    private EditText mEditProdNum;
    private EditText mEditProdInfo;
    private EditText mEditProdSupport;
    private EditText mEditPhone;
    private EditText mEditRemark;
    private String[] imgPaths;
    private ProgressDialog progress;

    private final String TAG = "manbu";


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sell_add_btn_send:
                if (0 != imagePicker.getImageList().size()) {
                    progress = new ProgressDialog(SellAddActivity.this);
                    progress.setMessage("正在上货,请稍等...");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                upProd();

                            } catch (Exception e) {
                                Message message = new Message();
                                message.what = -1;
                                handler.sendMessage(message);
                                throw e;
                            }
                        }
                    }.start();
                } else {
                    showToastL("请至少选择一张货物图片");
                }

                break;
            case R.id.tv_left:
                finish();
                break;
            case R.id.sell_add_prodAddress:
                if (!Tools.isFastDoubleClick()) {

                    new CityPickerDialog(SellAddActivity.this, new OnCityPikerListener() {
                        @Override
                        public void onCityPicker(String province, String city, String district) {
                            mTextAddress.setText(province + city + district);
                        }
                    }).show();
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void setContentView() {
        Fresco.initialize(this);
        setContentView(R.layout.sell_add_activity);
    }

    @Override
    public void initViews() {
        mTextLeft = (TextView) findViewById(R.id.tv_left);
        mTextTitle = (TextView) findViewById(R.id.tv_title);
        mBtnAdd = (Button) findViewById(R.id.sell_add_btn_send);
        mTextAddress = (TextView) findViewById(R.id.sell_add_prodAddress);

        mEditProdName = (EditText) findViewById(R.id.sell_add_prodName);
        mEditProdSpec = (EditText) findViewById(R.id.sell_add_prodSpec);
        mEditProdPrice = (EditText) findViewById(R.id.sell_add_prodPrice);
        mEditProdNum = (EditText) findViewById(R.id.sell_add_Num);
        mEditRemark = (EditText) findViewById(R.id.sell_add_remark);
        mEditProdInfo = (EditText) findViewById(R.id.sell_add_prodInfo);
        mEditProdSupport = (EditText) findViewById(R.id.sell_add_Support);
        mEditPhone = (EditText) findViewById(R.id.sell_add_phone);

        imagePicker = (ImagePickerView) findViewById(R.id.imagePicker);
        imagePicker.setNoImgResource(R.layout.add_img);
        imagePicker.setColumnNumber(5);

        mTextTitle.setText("发布货品");
        mTextLeft.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListeners() {
        mBtnAdd.setOnClickListener(this);
        mTextLeft.setOnClickListener(this);
        mTextAddress.setOnClickListener(this);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imagePicker.onActivityResult(requestCode, resultCode, data);
    }


    private Product getProdText() {
        Product mProduct = new Product();
        mProduct.setProdName(mEditProdName.getText().toString());
        mProduct.setProdSpec(mEditProdSpec.getText().toString());
        mProduct.setProdPrice(Float.valueOf(mEditProdPrice.getText().toString()));
        mProduct.setProdNum(Integer.valueOf(mEditProdNum.getText().toString()));
        mProduct.setRemark(mEditRemark.getText().toString());
        mProduct.setProdInfo(mEditProdInfo.getText().toString());
        mProduct.setProdSupport(mEditProdSupport.getText().toString());
        mProduct.setProdAddress(mTextAddress.getText().toString());
        mProduct.setPhone(mEditPhone.getText().toString());
        mProduct.setProdCreater(getUser());
        mProduct.setProdCreaterId(getUser().getObjectId());

        return mProduct;
    }

    private void upProd() {
        ImgCacheManager manager = new ImgCacheManager();
        manager.setFileDirPath(getExternalCacheDir().getPath());
        manager.setImg(imagePicker);
        imgPaths = manager.getPaths();

        BmobFile.uploadBatch(getApplicationContext(), imgPaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                Log.d(TAG, "onSuccess: 成功上传" + list1.size() + "张图片");
                if (list1.size() == imgPaths.length) {
                    progress.setMessage("即将完成...");
                    Product product = getProdText();
                    product.setProdImgs(list1);
                    product.save(getApplicationContext(), new SaveListener() {
                        @Override
                        public void onSuccess() {
                            progress.setMessage("已完成");
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            Log.d(TAG, "onSuccess: 上传货物成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.e(TAG, "onFailure: 上传货物失败" + s);
                            Message message = new Message();
                            message.what = -1;
                            handler.sendMessage(message);
                        }
                    });
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
                progress.setMessage("正在上传图片，已完成" + i3 + "%,请稍等...");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: 上传图片失败" + i + "---" + s);
            }
        });

    }

    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    progress.dismiss();
                    break;
                case -1:
                    progress.dismiss();
                    showToastL("发布货物失败-_-!!");
                    break;
            }
        }
    };

}
