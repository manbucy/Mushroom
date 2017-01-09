package com.manbu.mushroom.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.manbu.mushroom.R;
import com.manbu.mushroom.activity.fragment.BuyFragment;
import com.manbu.mushroom.bean.Favorites;
import com.manbu.mushroom.bean.Product;
import com.manbu.mushroom.bean.ProductImg;
import com.manbu.mushroom.common.MyApplication;
import com.manbu.mushroom.ui.citypicter.MyListView;
import com.manbu.mushroom.util.FileUtil;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by ManBu
 * com.manbu.mushroom.activity
 * 2016/9/11 0:11
 */
public class ProductInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "ProductInfoActivity";
    public static String KEY = "ProductInfoActivity";
    private TextView mProdName;
    private Product mProduct;
    private TextView mTvLeft;
    private TextView mTvTile;
    private ImageView mTvCollection;
    private TextView mProdNum;
    private TextView mProdAddress;
    private TextView mProdSupport;
    private TextView mProdPrice;
    private TextView mProdRemark;
    private TextView mProdSpec;
    private TextView mProdInfo;
    private ImageView mImagView;
    private CommonAdapter<ProductImg> mAdapter;
    private MyListView mListView;
    private List<ProductImg> mProductImgs = new ArrayList<>();
    private TextView mContact;
    private TextView mBuy;
    private Boolean isCollection = false;

    /**
     * 在退出这个界面的时候，对是否“收藏”进行判断。
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (isCollection != getIntent().getBooleanExtra("isCollection", false)) {
            if (getIntent().getBooleanExtra("isCollection", false)) {
                Favorites fa = (Favorites) getIntent().getSerializableExtra("favorite");
                Favorites favorites = new Favorites();
                favorites.setObjectId(fa.getObjectId());
                favorites.delete(getApplicationContext(), new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: 删除成功");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.d(TAG, "onFailure: 删除失败" + s);
                    }
                });
            } else {
                Favorites favorites = new Favorites();
                favorites.setUserId(MyApplication.getUser().getObjectId());
                favorites.setProdId(mProduct.getObjectId());
                favorites.save(getApplicationContext(), new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: 创建成功");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.d(TAG, "onFailure: 创建失败" + s);
                    }
                });
            }
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info);
        mProduct = (Product) getIntent().getSerializableExtra(BuyFragment.KEY);
        isCollection = getIntent().getBooleanExtra("isCollection", false);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        mContact.setOnClickListener(this);
        mBuy.setOnClickListener(this);
        mTvCollection.setOnClickListener(this);
        mTvLeft.setOnClickListener(this);


    }

    private void initView() {

        mProdName = (TextView) findViewById(R.id.product_prodName);
        mTvLeft = (TextView) findViewById(R.id.product_tv_left);
        mTvTile = (TextView) findViewById(R.id.product_tv_title);
        mTvCollection = (ImageView) findViewById(R.id.product_iv_collection);

        if (isCollection)
            mTvCollection.setImageResource(R.drawable.star_full);
        mImagView = (ImageView) findViewById(R.id.product_img_zero);


        mListView = (MyListView) findViewById(R.id.product_listView);

        mProdNum = (TextView) findViewById(R.id.product_prodNum);
        mProdAddress = (TextView) findViewById(R.id.product_prodAddress);
        mProdSupport = (TextView) findViewById(R.id.product_prodSupport);
        mProdPrice = (TextView) findViewById(R.id.product_prodPrice);
        mProdRemark = (TextView) findViewById(R.id.product_prodRemark);
        mProdSpec = (TextView) findViewById(R.id.product_prodSpec);
        mProdInfo = (TextView) findViewById(R.id.product_prodInfo);

        mContact = (TextView) findViewById(R.id.product_bottom_contact);
        mBuy = (TextView) findViewById(R.id.product_bottom_buy);


        mAdapter = new CommonAdapter<ProductImg>(getApplicationContext(),
                R.layout.product_item_img, mProductImgs) {
            @Override
            protected void convert(ViewHolder viewHolder, ProductImg item, int position) {
                viewHolder.setImageBitmap(R.id.list_item_img, item.getBitmap());
            }
        };
        mListView.setAdapter(mAdapter);
    }

    private void initData() {
        mProdName.setText(mProduct.getProdName());
        mTvTile.setText(mProduct.getProdName());
        mProdNum.setText(mProduct.getProdNum().toString());
        mProdAddress.setText(mProduct.getProdAddress());
        mProdSupport.setText(mProduct.getProdSupport());
        mProdPrice.setText(mProduct.getProdPrice().toString());
        mProdRemark.setText(mProduct.getRemark());
        mProdSpec.setText(mProduct.getProdSpec());
        mProdInfo.setText(mProduct.getProdInfo());

        for (int i = 1; i < mProduct.getProdImgs().size(); i++) {
            final String fileUrl = mProduct.getProdImgs().get(i);
            final int flag = i;
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    loadImg(fileUrl, flag);
                }
            }.start();
        }

    }

    private void loadImg(String fileUrl, int flag) {
        final ProductImg mImg = new ProductImg();
        final int i = flag;
        String fileName = FileUtil.getFileNameSuffix(fileUrl);
        BmobFile bmobFile = new BmobFile(fileName, "", fileUrl);
        File saveFile = new File(MyApplication.getContext().getExternalCacheDir().getPath(),
                bmobFile.getFilename());
        if (saveFile.exists()) {
            mImg.setBitmap(BitmapFactory.decodeFile(saveFile.getPath()));
            Message message = new Message();
            message.what = 1;
            message.obj = mImg;
            message.arg1 = i;
            handler.sendMessage(message);
        } else {
            bmobFile.download(MyApplication.getContext(), saveFile, new DownloadFileListener() {
                @Override
                public void onSuccess(String s) {
                    mImg.setBitmap(BitmapFactory.decodeFile(s));
                    if (i == 1) {
                        mImagView.setImageBitmap(mImg.getBitmap());
                    }
                    Message message = new Message();
                    message.what = 1;
                    message.obj = mImg;
                    message.arg1 = i;
                    handler.sendMessage(message);
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.d(TAG + "downImg", i + "onFailure: " + s);
                }
            });
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mProductImgs.add((ProductImg) msg.obj);
                    mAdapter.notifyDataSetChanged();
                    if (msg.arg1 == 1) {
                        mImagView.setImageBitmap(((ProductImg) msg.obj).getBitmap());
                    }
                    break;

            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_bottom_contact:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mProduct.getPhone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.product_bottom_buy:
                Intent mIntent = new Intent(ProductInfoActivity.this, OrderConfirmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY,mProduct);
                mIntent.putExtras(bundle);
                startActivity(mIntent);
                break;
            case R.id.product_tv_left:
                finish();
                break;
            case R.id.product_iv_collection:
                if (isCollection) {
                    mTvCollection.setImageResource(R.drawable.star_empty);
                    isCollection = false;
                    Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: 取消收藏");
                } else {
                    mTvCollection.setImageResource(R.drawable.star_full);
                    isCollection = true;
                    Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: 收藏成功");
                }
                break;
        }

    }

}
