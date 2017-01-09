package com.manbu.mushroom.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.manbu.mushroom.R;
import com.manbu.mushroom.activity.fragment.BuyFragment;
import com.manbu.mushroom.activity.fragment.FindFragment;
import com.manbu.mushroom.activity.fragment.ManageFragment;
import com.manbu.mushroom.activity.fragment.OriginFragment;
import com.manbu.mushroom.activity.fragment.SellFragment;
import com.manbu.mushroom.ui.MySpannableStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnClickListener {

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    //	private FragmentStatePagerAdapter mAdapter;
    private List<Fragment> mFragments;

    private LinearLayout mTabBuy;
    private LinearLayout mTabSell;
    private LinearLayout mTabOrigin;
    private LinearLayout mTabFind;
    private LinearLayout mTabManagment;

    private ImageView mImgBuy;
    private ImageView mImgSell;
    private ImageView mImgOrigin;
    private ImageView mImgFind;
    private ImageView mImgManagment;

    private TextView mTxtBuy;
    private TextView mTxtSell;
    private TextView mTxtOrigin;
    private TextView mTxtfind;
    private TextView mTxtManagment;

    private TextView top_title;
    private TextView mTextTitle;
    private RelativeLayout mTopView;
    private RelativeLayout mTopHead;
    private LinearLayout mTopSearch;

    private Long exitTime = 0L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListeners();

        setSelect(getIntent().getIntExtra("selectPage", 2));//默认选择第二页 溯源界面
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initListeners() {
        mTabBuy.setOnClickListener(this);
        mTabSell.setOnClickListener(this);
        mTabOrigin.setOnClickListener(this);
        mTabFind.setOnClickListener(this);
        mTabManagment.setOnClickListener(this);

    }

    private void initView() {

        mViewPager = (ViewPager) findViewById(R.id.fragment_viewpager);

        mTabBuy = (LinearLayout) findViewById(R.id.buttom_tab_buy);
        mTabSell = (LinearLayout) findViewById(R.id.buttom_tab_sell);
        mTabOrigin = (LinearLayout) findViewById(R.id.buttom_tab_origin);
        mTabFind = (LinearLayout) findViewById(R.id.buttom_tab_find);
        mTabManagment = (LinearLayout) findViewById(R.id.buttom_tab_managment);

        mImgBuy = (ImageView) findViewById(R.id.buttom_tab_img_buy);
        mImgSell = (ImageView) findViewById(R.id.buttom_tab_img_sell);
        mImgOrigin = (ImageView) findViewById(R.id.buttom_tab_img_origin);
        mImgFind = (ImageView) findViewById(R.id.buttom_tab_img_find);
        mImgManagment = (ImageView) findViewById(R.id.buttom_tab_img_managment);

        mTxtBuy = (TextView) findViewById(R.id.buttom_tab_txt_buy);
        mTxtSell = (TextView) findViewById(R.id.buttom_tab_txt_sell);
        mTxtOrigin = (TextView) findViewById(R.id.buttom_tab_txt_origin);
        mTxtfind = (TextView) findViewById(R.id.buttom_tab_txt_find);
        mTxtManagment = (TextView) findViewById(R.id.buttom_tab_txt_managment);


        top_title = (TextView) findViewById(R.id.top_search_title);
        mTextTitle = (TextView) findViewById(R.id.tv_title);
        mTopView = (RelativeLayout) findViewById(R.id.top_view);
        mTopHead = (RelativeLayout) findViewById(R.id.top_head);
        mTopSearch = (LinearLayout) findViewById(R.id.top_search);


        mFragments = new ArrayList<Fragment>();
        Fragment mBuyFragment = new BuyFragment();
        Fragment mSellFragment = new SellFragment();
        Fragment mOriginFragment = new OriginFragment();
        Fragment mFindFragment = new FindFragment();
        Fragment mManagFragment = new ManageFragment();
        mFragments.add(mBuyFragment);
        mFragments.add(mSellFragment);
        mFragments.add(mOriginFragment);
        mFragments.add(mFindFragment);
        mFragments.add(mManagFragment);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }
        };
//		mAdapter =new FragmentStatePagerAdapter(getSupportFragmentManager()) {
//			@Override
//			public Fragment getItem(int position) {
//				return mFragments.get(position);
//			}
//
//			@Override
//			public int getCount() {
//				return mFragments.size();
//			}
//		};

        Log.i("---", "will setAdapter");
        mViewPager.setAdapter(mAdapter);
        Log.i("---", "setAdapter succsess");
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                int currentItem = mViewPager.getCurrentItem();
                setTab(currentItem);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttom_tab_buy:
                setSelect(0);
                break;
            case R.id.buttom_tab_sell:
                setSelect(1);
                break;
            case R.id.buttom_tab_origin:
                setSelect(2);
                break;
            case R.id.buttom_tab_find:
                setSelect(3);
                break;
            case R.id.buttom_tab_managment:
                setSelect(4);
                break;

            default:
                break;
        }
    }

    private void setSelect(int i) {
        setTab(i);
        mViewPager.setCurrentItem(i);
    }

    private void setTab(int currentItem) {
        reSetTab();

        switch (currentItem) {
            case 0:
                mImgBuy.setImageResource(R.drawable.main_tab_icon_buyer_press);
                mTxtBuy.setTextColor(getResources().getColor(R.color.primary));
                mTopView.setVisibility(View.VISIBLE);
                mTopHead.setVisibility(View.GONE);
                mTopSearch.setVisibility(View.VISIBLE);
                String buy = "我要买";
                MySpannableStringBuilder span_buy = new MySpannableStringBuilder();
                top_title.setText(span_buy.setText(buy, "买"));
                break;
            case 1:
                mImgSell.setImageResource(R.drawable.main_tab_icon_seller_press);
                mTxtSell.setTextColor(getResources().getColor(R.color.primary));
                mTopView.setVisibility(View.VISIBLE);
                mTopHead.setVisibility(View.GONE);
                mTopSearch.setVisibility(View.VISIBLE);
                String sell = "我要卖";
                MySpannableStringBuilder span_sell = new MySpannableStringBuilder();
                top_title.setText(span_sell.setText(sell, "卖"));
                break;
            case 2:
                mImgOrigin.setImageResource(R.drawable.main_tab_icon_origin_press);
                mTxtOrigin.setTextColor(getResources().getColor(R.color.primary));
                mTopView.setVisibility(View.VISIBLE);
                mTopHead.setVisibility(View.VISIBLE);
                mTopSearch.setVisibility(View.GONE);
                mTextTitle.setText("溯源");
                break;
            case 3:
                mImgFind.setImageResource(R.drawable.main_tab_icon_find_press);
                mTxtfind.setTextColor(getResources().getColor(R.color.primary));
                mTopView.setVisibility(View.VISIBLE);
                mTopHead.setVisibility(View.VISIBLE);
                mTopSearch.setVisibility(View.GONE);
                mTextTitle.setText("发现");
                break;
            case 4:
                mImgManagment.setImageResource(R.drawable.main_tab_icon_management_press);
                mTxtManagment.setTextColor(getResources().getColor(R.color.primary));
                mTopView.setVisibility(View.VISIBLE);
                mTopHead.setVisibility(View.VISIBLE);
                mTopSearch.setVisibility(View.GONE);
                mTextTitle.setText("我");
                break;
        }

    }

    private void reSetTab() {
        mImgBuy.setImageResource(R.drawable.main_tab_icon_buyer_normal);
        mTxtBuy.setTextColor(getResources().getColor(R.color.divider));

        mImgSell.setImageResource(R.drawable.main_tab_icon_seller_normal);
        mTxtSell.setTextColor(getResources().getColor(R.color.divider));

        mImgOrigin.setImageResource(R.drawable.main_tab_icon_origin_normal);
        mTxtOrigin.setTextColor(getResources().getColor(R.color.divider));

        mImgFind.setImageResource(R.drawable.main_tab_icon_find_normal);
        mTxtfind.setTextColor(getResources().getColor(R.color.divider));

        mImgManagment.setImageResource(R.drawable.main_tab_icon_management_normal);
        mTxtManagment.setTextColor(getResources().getColor(R.color.divider));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            boolean hideInputResult = isShouldHideInput(v, ev);
            Log.v("hideInputResult", "zzz-->>" + hideInputResult);
            if (hideInputResult) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) MainActivity.this
                        .getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                if (v != null) {
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            //之前一直不成功的原因是,getX获取的是相对父视图的坐标,getRawX获取的才是相对屏幕原点的坐标！！！
            Log.v("leftTop[]", "zz--left:" + left + "--top:" + top + "--bottom:" + bottom +
                    "--right:" + right);
            Log.v("event", "zz--getX():" + event.getRawX() + "--getY():" + event.getRawY());
            if (event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


}
