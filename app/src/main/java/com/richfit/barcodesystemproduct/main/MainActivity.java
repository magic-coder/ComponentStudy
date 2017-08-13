package com.richfit.barcodesystemproduct.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.richfit.barcodesystemproduct.R;
import com.richfit.barcodesystemproduct.adapter.MainPagerViewAdapter;
import com.richfit.barcodesystemproduct.barcodescan.BaseBarScannerActivity;
import com.richfit.common_lib.lib_interface.IBarcodeSystemMain;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.widget.NoScrollViewPager;
import com.richfit.data.constant.Global;

import butterknife.BindView;



/**
 * Created by monday on 2017/3/10.
 */

public class MainActivity extends BaseBarScannerActivity<MainPresenterImp> implements
        MainContract.View, ViewPager.OnPageChangeListener, IBarcodeSystemMain{


    /*当前选中的页签下表，用于恢复*/
    public static final String CURRENT_PAGE_INDEX_KEY = "current_page_index";

    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.floating_button)
    FloatingActionButton mFloatingButton;

    String mCompanyCode;
    String mCaption;

    /*当前的显示页面*/
    int mCurrentPage = 0;
    int mode;


    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MainPresenterImp(this);
    }

    @Override
    public void initVariables() {
        super.initVariables();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mCompanyCode = bundle.getString(Global.EXTRA_COMPANY_CODE_KEY);
                mCaption = bundle.getString(Global.EXTRA_CAPTION_KEY);
                //默认给出的是在线模式
                mode = bundle.getInt(Global.EXTRA_MODE_KEY, Global.ONLINE_MODE);
            }
        }
    }

    @Override
    protected void initViews() {
        setupToolBar(R.id.toolbar, R.id.toolbar_title, mCaption);
        /*设置viewPager*/
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void initEvent() {
        mFloatingButton.setOnClickListener(v -> responseOnClick());
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        setupMainContent(savedInstanceState);
    }


    /**
     * 设置viewpager的当前显示页
     */
    @Override
    public void showFragmentByPosition(final int position) {
        if (position < 0 || position >= mViewPager.getAdapter().getCount())
            return;
        mViewPager.setCurrentItem(position);

    }

    @Override
    public Class getMainActivityClass() {
        return MainActivity.class;
    }

    /**
     * 设置主页面的内容
     */
    private void setupMainContent(Bundle savedInstanceState) {
        int currentPageIndex = savedInstanceState == null ? -1 :
                savedInstanceState.getInt(CURRENT_PAGE_INDEX_KEY, -1);
        Intent intent = getIntent();
        Bundle bundle = null;
        if (intent != null) {
            bundle = intent.getExtras();
            if (bundle == null) {
                showMessage("未获取到业务类型");
                return;
            }
        }
        mPresenter.setLocal(mode == Global.ONLINE_MODE ? false : true);
        mPresenter.setupMainContent(getSupportFragmentManager(), bundle, currentPageIndex, mode);
    }

    @Override
    public void showMainContent(MainPagerViewAdapter adapter, int currentPageIndex) {
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        //默认显示第一个页签
        mCurrentPage = currentPageIndex == -1 ? 0 : currentPageIndex;
        mViewPager.setCurrentItem(mCurrentPage);
        //如果默认显示的是抬头页面，那么根据isNeedShowFloatingButton决定是否显示按钮
        isShowFloatingButton();
    }

    private void isShowFloatingButton() {
        final BaseFragment fragment = getFragmentByPosition(mCurrentPage);
        if (fragment == null)
            return;
        mFloatingButton.setVisibility(fragment.isNeedShowFloatingButton() ?
                View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setupMainContentFail(String message) {
        showMessage(message);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //当该Activity从后台再次进入前台的时候(也就是后台的Activity已经onStop了)，那么根据需求需要
        //Fragment再次执行懒加载
        BaseFragment fragment = getFragmentByPosition(mCurrentPage);
        if (fragment != null && fragment.getFragmentType() == BaseFragment.DETAIL_FRAGMENT_INDEX) {
            getFragmentByPosition(mCurrentPage).setUserVisibleHint(true);
        }
    }

    /**
     * 拦截点击事件，在屏幕的任何空白处点击关闭软键盘。
     * 注意dispatcherTouchEvent是事件WMS->Activity
     * 的回到，之后才有Window.superDispatchTouchEvent()->DecorView上面
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                //使EditText触发一次失去焦点事件
                v.setFocusable(false);
//                v.setFocusable(true); //这里不需要是因为下面一句代码会同时实现这个功能
                v.setFocusableInTouchMode(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exitBy2Click();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    /**
     * 获取一个Fragment实例对象
     */
    public BaseFragment getFragmentByPosition(final int position) {

        if (mViewPager != null) {
            PagerAdapter adapter = mViewPager.getAdapter();
            if (adapter != null && MainPagerViewAdapter.class.isInstance(adapter)) {
                MainPagerViewAdapter mainPagerViewAdapter = (MainPagerViewAdapter) adapter;
                if (position < 0 || position > mainPagerViewAdapter.getCount() - 1)
                    return null;
                return (BaseFragment) mainPagerViewAdapter.getItem(position);
            }
        }
        return null;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPage = position;
        isShowFloatingButton();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 响应FloatingButton的点击事件
     */
    private void responseOnClick() {
        final BaseFragment fragment = getFragmentByPosition(mCurrentPage);
        if (fragment == null)
            return;
        switch (fragment.getFragmentType()) {
            //抬头界面
            case BaseFragment.HEADER_FRAGMENT_INDEX:
                if (fragment.checkDataBeforeOperationOnHeader()) {
                    fragment.operationOnHeader(mCompanyCode);
                }

                break;
            //数据明细界面
            case BaseFragment.DETAIL_FRAGMENT_INDEX:
                if (fragment.checkDataBeforeOperationOnDetail()) {
                    fragment.showOperationMenuOnDetail(mCompanyCode);
                }
                break;
            //数据采集界面
            case BaseFragment.COLLECT_FRAGMENT_INDEX:
                //这里要兼容验收模块拍照，情景是如果该行采集网拍照，然后回退之后，在接着拍照
                fragment.showOperationMenuOnCollection(mCompanyCode);
                break;
        }
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



    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        if (list == null || list.length <= 0)
            return;
        final BaseFragment fragment = getFragmentByPosition(mCurrentPage);
        if (fragment == null) {
            return;
        }
        final int fragmentType = fragment.getFragmentType();
        //物料
        if (list.length > 2 && fragmentType == BaseFragment.COLLECT_FRAGMENT_INDEX) {
            fragment.handleBarCodeScanResult(type, list);
            //单据
        } else if (list.length == 1 && fragmentType == BaseFragment.HEADER_FRAGMENT_INDEX) {
            fragment.handleBarCodeScanResult(type, list);
            //仓位
        } else if (list.length == 2 && fragmentType == BaseFragment.COLLECT_FRAGMENT_INDEX) {
            fragment.handleBarCodeScanResult(type, list);
        } else {
            fragment.handleBarCodeScanResult(type, list);
        }
    }

}
