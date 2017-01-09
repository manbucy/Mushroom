package com.manbu.mushroom.activity.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.manbu.mushroom.R;
import com.manbu.mushroom.activity.ProductInfoActivity;
import com.manbu.mushroom.bean.Favorites;
import com.manbu.mushroom.bean.Product;
import com.manbu.mushroom.common.MyApplication;
import com.manbu.mushroom.data.BmobUtil;
import com.manbu.mushroom.util.DateUtils;
import com.manbu.mushroom.util.StringUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class BuyFragment extends Fragment implements OnClickListener {
    private static String TAG = "BuyFragment";
    public static final String KEY = "BUYFRAGMENT";
    private ListView mlistView;
    private List<Product> mProducts = new ArrayList<>();
    private CommonAdapter<Product> mAdapter;
    private PtrClassicFrameLayout mPtrFrame;
    private String startTime;
    private String endTime;
    private EditText mEditSearch;
    private TextView mBtnSearch;
    private Boolean isSearch=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy, container, false);
        mlistView = (ListView) view.findViewById(R.id.buy_prod_listView);
        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.buy_ptr_layout);

        mEditSearch = (EditText) getActivity().findViewById(R.id.top_etxt_search);
        mBtnSearch = (TextView) getActivity().findViewById(R.id.top_btn_search);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBtnSearch.setOnClickListener(this);

        //开启线程 从后端查询数据
        new Thread() {
            @Override
            public void run() {
                super.run();
                queryData();
            }
        }.start();

        //listView的点击事件
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Product product = mProducts.get(i);
                Log.d(TAG, "onItemClick: " + product.getProdName());

                final Intent mItent = new Intent(getActivity(), ProductInfoActivity.class);
                // 查询是否有收藏该商品
                BmobQuery<Favorites> bmobQuery = new BmobQuery<Favorites>();
                bmobQuery.addWhereEqualTo("userId",MyApplication.getUser().getObjectId());
                bmobQuery.addWhereEqualTo("prodId",product.getObjectId());
                bmobQuery.findObjects(getContext(), new FindListener<Favorites>() {
                    @Override
                    public void onSuccess(List<Favorites> list) {
                        if(0!=list.size()){
                            Favorites favorites= list.get(0);
                            Bundle mBundle = new Bundle();
                            mBundle.putSerializable(KEY,product);
                            mBundle.putSerializable("favorite",favorites);
                            mItent.putExtra("isCollection",true);
                            mItent.putExtras(mBundle);
                            startActivity(mItent);

                        }else{
                            Bundle mBundle = new Bundle();
                            mBundle.putSerializable(KEY,product);
                            mItent.putExtras(mBundle);
                            mItent.putExtra("isCollection",false);
                            startActivity(mItent);
                        }
                    }
                    @Override
                    public void onError(int i, String s) {
                        Log.d(TAG,i+ "onError: "+s);
                    }
                });
            }
        });

        //注册下拉刷新 上拉加载
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            //上拉加载
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                onlodaData();
            }
            //下拉刷新
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if(StringUtils.isEmpty(mEditSearch.getText().toString()) && isSearch){
                    queryData();
                    isSearch=false;
                }else {
                    onfreshData();
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, content, header);
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return super.checkCanDoLoadMore(frame, content, footer);
            }

        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f); // you can also set foot and header separately
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(1000);  // you can also set foot and header separately
        // default is false
        mPtrFrame.setPullToRefresh(false);

        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                queryData();
            }
        }, 1000);

    }
    @Override // 点击事件
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_btn_search: //搜索
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        queryData();
                        isSearch=true;
                    }
                }.start();
                break;
        }
    }

    //下拉刷新 从 后端查询数据
    private void onfreshData() {
        List<BmobQuery<Product>> and = new ArrayList<BmobQuery<Product>>();
        BmobQuery<Product> q1 = new BmobQuery<Product>();
        BmobQuery<Product> q2 = new BmobQuery<Product>();
        Date statrDate = new Date();
        try {
            statrDate = DateUtils.stringToDate(startTime, DateUtils.PATTEN_HMS);
        } catch (Exception e) {
            Log.d(TAG, "onfreshData: " + e);
        }

        Date currDate = new Date();
        q1.addWhereGreaterThan("createdAt", new BmobDate(DateUtils.addSecond(statrDate, 2)));
        q2.addWhereLessThan("createdAt", new BmobDate(currDate));
        and.add(q1);
        and.add(q2);

        BmobQuery<Product> bmobQuery = new BmobQuery<Product>();
        bmobQuery.and(and);
        if (!StringUtils.isEmpty(mEditSearch.getText().toString())) {
            //addWhereContains Bmob的模糊查询 在近期更新后 只针对付费用户开发放
//            bmobQuery.addWhereContains("prodName",mEditSearch.getText().toString());
            bmobQuery.addWhereEqualTo("prodName", mEditSearch.getText().toString());
        }
        bmobQuery.findObjects(MyApplication.getContext(), new FindListener<Product>() {
            @Override
            public void onSuccess(List<Product> list) {
                Message message = Message.obtain();
                message.obj = list;
                message.what = 2;
                handler.sendMessage(message);
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG + "freshProd", i + "onError: " + s);
            }
        });

    }

    //上拉加载 从后端查询数据
    private void onlodaData() {
        Date endDate = new Date();
        try {
            endDate = DateUtils.stringToDate(endTime, DateUtils.PATTEN_HMS);
        } catch (Exception e) {
            Log.d(TAG, "onfreshData: " + e);
        }
        BmobQuery<Product> bmobQuery = new BmobQuery<Product>();
        bmobQuery.addWhereLessThan("createdAt", new BmobDate(endDate));
        if (!StringUtils.isEmpty(mEditSearch.getText().toString())) {
            //addWhereContains Bmob的模糊查询 在近期更新后 只针对付费用户开发放
//            bmobQuery.addWhereContains("prodName",mEditSearch.getText().toString());
            bmobQuery.addWhereEqualTo("prodName", mEditSearch.getText().toString());
        }
        Log.d(TAG, "onlodaData: " + DateUtils.formatString(endDate, DateUtils.PATTEN_HMS));
        bmobQuery.order("-createdAt");
        bmobQuery.setLimit(10);
        bmobQuery.findObjects(MyApplication.getContext(), new FindListener<Product>() {
            @Override
            public void onSuccess(List<Product> list) {
                Message message = Message.obtain();
                message.obj = list;
                message.what = 3;
                handler.sendMessage(message);
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG + "freshProd", i + "onError: " + s);
            }
        });

    }

    //从后端查询数据
    private void queryData() {
        BmobQuery<Product> bmobQuery = new BmobQuery<Product>();
        bmobQuery.setLimit(10);
        bmobQuery.order("-createdAt");
        if (!StringUtils.isEmpty(mEditSearch.getText().toString())) {
            //addWhereContains Bmob的模糊查询 在近期更新后 只针对付费用户开发放
//            bmobQuery.addWhereContains("prodName",mEditSearch.getText().toString());
            bmobQuery.addWhereEqualTo("prodName", mEditSearch.getText().toString());
        }
        bmobQuery.findObjects(MyApplication.getContext(), new FindListener<Product>() {
            @Override
            public void onSuccess(List<Product> list) {
                Log.d(TAG, "onSuccess:1 " + list.size());
                Message message = Message.obtain();
                message.obj = list;
                message.what = 1;
                handler.sendMessage(message);
            }
            @Override
            public void onError(int i, String s) {
                Log.d(TAG + "queryProd", i + "onError: " + s);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initAdapter((List<Product>) msg.obj);
                    break;
                case 2:
                    initFreshData((List<Product>) msg.obj);
                    break;
                case 3:
                    initLoadData((List<Product>) msg.obj);
                    break;
            }

        }
    };

    //上拉加载 后 更新UI
    private void initLoadData(List<Product> obj) {
        for (Product prod : obj) {
            mProducts.add(prod);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.refreshComplete();
                mAdapter.notifyDataSetChanged();
                try {
                    startTime = mProducts.get(0).getCreatedAt();
                    Log.d(TAG, "initAdapter: " + startTime);
                    endTime = mProducts.get(mProducts.size() - 1).getCreatedAt();
                    Log.d(TAG, "initAdapter: " + endTime);

                }catch (Exception e){
                    Toast.makeText(getContext(),"没有搜索到",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "initLoadData run: "+e);
                }
            }
        }, 4000);
    }

    //下拉刷新后更新 UI
    private void initFreshData(List<Product> obj) {
        for (Product prod : obj) {
            mProducts.add(0, prod);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.refreshComplete();
                mAdapter.notifyDataSetChanged();

                try {
                    startTime = mProducts.get(0).getCreatedAt();
                    Log.d(TAG, "initAdapter: " + startTime);
                    endTime = mProducts.get(mProducts.size() - 1).getCreatedAt();
                    Log.d(TAG, "initAdapter: " + endTime);

                }catch (Exception e){
                    Toast.makeText(getContext(),"没有搜索到",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "initFreshData run: "+e);
                }
            }
        }, 1500);
    }

    //装载数据至 listView
    private void initAdapter(List<Product> list) {
        mProducts = list;
        mAdapter = new CommonAdapter<Product>(MyApplication.getContext(), R.layout.product_item, mProducts) {
            @Override
            protected void convert(ViewHolder viewHolder, Product item, int position) {
                Log.d(TAG, "convert: " + item.getProdName());
                Bitmap mHead = BmobUtil.downImg(item.getProdImgs().get(0));
                viewHolder.setText(R.id.prod_name, item.getProdName());
                viewHolder.setText(R.id.prod_address, item.getProdAddress());
                viewHolder.setText(R.id.prod_support, item.getProdSupport());
                viewHolder.setText(R.id.prod_spec, item.getProdSpec());
                viewHolder.setText(R.id.prod_price, item.getProdPrice().toString());
                try {
                    viewHolder.setText(R.id.prod_time, DateUtils.formationDate(item.getCreatedAt()));
                } catch (Exception e) {
                    Log.d(TAG, "convert: " + e);
                }
                viewHolder.setImageBitmap(R.id.prod_img, mHead);
            }
        };
        mPtrFrame.refreshComplete();
        mlistView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        try {
            startTime = mProducts.get(0).getCreatedAt();
            Log.d(TAG, "initAdapter: " + startTime);
            endTime = mProducts.get(mProducts.size() - 1).getCreatedAt();
            Log.d(TAG, "initAdapter: " + endTime);

        }catch (Exception e){
            Toast.makeText(getContext(),"没有搜索到",Toast.LENGTH_SHORT).show();
            Log.d(TAG, "initAdapter run: "+e);
        }

    }

}
