package com.richfit.barcodesystemproduct.upload;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.richfit.barcodesystemproduct.R;
import com.richfit.barcodesystemproduct.adapter.MainPagerViewAdapter;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.RefUtil;
import com.richfit.common_lib.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 上传离线数据
 * Created by monday on 2017/4/17.
 */

public class UploadActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    TabLayout mTabLayout;
    NoScrollViewPager mViewPager;
    List<BaseFragment> mFragments;
    FloatingActionButton mFabButton;
    int mCurrentFragmentIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (NoScrollViewPager) findViewById(R.id.viewpager);
        mFabButton = (FloatingActionButton) findViewById(R.id.floating_button);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //设置标题必须在setSupportActionBar之前才有效
        TextView toolBarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolBarTitle.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        toolBarTitle.setText("离线数据上传");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*设置viewPager*/
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initEvent() {
        RxView.clicks(mFabButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(a -> {
                    if (mFragments != null) {
                        BaseFragment fragment = mFragments.get(mCurrentFragmentIndex);
                        fragment.saveCollectedData();
                    }
                });
    }

    private void initData() {
        if (mFragments == null) {
            mFragments = new ArrayList<>();

        }
        mCurrentFragmentIndex = 0;
        try {
            BaseFragment fragment = RefUtil.newInstance(BuziUploadFragment.class, "", "", "", "", -1, "出入库业务");
            mFragments.add(fragment);
            fragment = RefUtil.newInstance(CheckUploadFragment.class, "", "", "", "", -1, "盘点业务");
            mFragments.add(fragment);

            MainPagerViewAdapter<BaseFragment> adapter = new MainPagerViewAdapter<>(getSupportFragmentManager(), mFragments);
            mViewPager.setAdapter(adapter);
            mTabLayout.setupWithViewPager(mViewPager);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("yff", e.getMessage());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentFragmentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
