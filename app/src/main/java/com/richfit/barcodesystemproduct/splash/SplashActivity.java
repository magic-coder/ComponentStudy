package com.richfit.barcodesystemproduct.splash;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.richfit.barcodesystemproduct.BuildConfig;
import com.richfit.barcodesystemproduct.login.LoginActivity;
import com.richfit.barcodesystemproduct.splash.imp.SplashPresenterImp;
import com.richfit.common_lib.lib_mvp.BaseActivity;
import com.richfit.common_lib.utils.NetworkStateUtil;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.common_lib.utils.SysProp;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.LoadBasicDataWrapper;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * 在启动app的时候，如果一个Activity所属的Application还没运行，系统会为这个Activity创建一个
 * 进程（每开启一个进程都会有一个Application，所以Application的onCreate()可能会被调用多次），
 * 但是启动一个进程需要时间，所以如果该进程初始化耗时较长，那么屏幕没有任何动静。
 * 所以，系统会自动启动一个startingWindow。StartingWindow一般出现在应用程序进程创建并初始化成功前，所以它是个临时窗口，对应的WindowType是TYPE_APPLICATION_STARTING。
 * 目的是告诉用户，系统已经接受到操作，正在响应，在程序初始化完成后实现目的UI，同时移除这个窗口。
 * 系统给startingWindow的DecorView设置Activity或者Application的Theme。
 * 所以，我们启动一个Splash页面，这里需要注意的是Splash页面不能够有耗时操作。
 * 因为如果Splash有耗时操作，那么startingWindow结束显示后，会显示Splash的
 * Window(注意Splash页面没有setContentView)。所以SplashActivity中不要做太多操作，如果确实要做的话，我想到的解决方式是：
 * 将两个的window的background设置的一样，这样即便startingWindow结束，用户也感觉不到切换了。
 * 设置SplashActivity对应window的background的方法，在SplashActivity的onCreate中调用
 * getWindow().setBackgroundDrawableResource(背景图片);
 * Created by monday on 2016/12/2.
 */

public class SplashActivity extends BaseActivity<SplashPresenterImp> implements ISplashView {

    ArrayList<LoadBasicDataWrapper> mRequestParam;
    Disposable mDisposable;

    @Override
    protected int getContentId() {
        return 0;
    }

    @Override
    public void initPresenter() {
        mPresenter = new SplashPresenterImp(this);
    }


    @Override
    protected boolean isOpenStatusBar() {
        return false;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        startBarcodeSystem();
    }

    @Override
    protected void initEvent() {

    }

    /**
     * 进入条码系统的入口
     */
    private void startBarcodeSystem() {
        if (judgeProperty()) {
            new AlertDialog.Builder(this)
                    .setTitle("扫描设置")
                    .setMessage("您未打开\"使能上报扫描键值\",请先打开它")
                    .setPositiveButton("现在去开启", (dialog, which) -> {
                        Intent intent = new Intent(
                                Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                        finish();
                    }).setNegativeButton("下次再说", (dialog, which) -> finish()).show();
            return;
        }
        //1. 检查是否有网络；2.检查是否是第一次登陆
        if (!NetworkStateUtil.isNetConnected(this.getApplicationContext()) && mPresenter != null) {
            mPresenter.setLocal(true);
            toLogin();
            return;
        }
        //如果有网络那么直接进行基础数据更新
        mPresenter.setLocal(false);
        Global.DBSOURCE = BuildConfig.APP_NAME;
        Global.MAC_ADDRESS = CommonUtil.getMacAddress();
        mPresenter.getConnectionStatus();
    }

    /**
     * 如果该地址能够连接到服务器。所有的地区公司不论是否存在离线业务都先下载数据库
     */
    @Override
    public void networkAvailable() {
        mPresenter.downloadInitialDB();
    }

    @Override
    public void networkNotAvailable(String message) {
        showMessage(message);
        toLogin();
    }

    /**
     * 判断快捷扫描是否勾选。如果不勾选跳转到系统设置中进行设置
     */
    private boolean judgeProperty() {
        //如果该渠道的isServiceDL为true说明使用的是SPEEDATA的手持
        if (!BuildConfig.ISSERVICEDL)
            return false;
        String result = SysProp.get("persist.sys.keyreport", "false");
        return result.equals("false");
    }


    /**
     * 跳转到登陆页面
     */
    @Override
    public void toLogin() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        overridePendingTransition(0, android.R.anim.fade_out);
        finish();
    }

    /**
     * 同步基础数据
     */
    private void startSyncBasicData() {
        if (mRequestParam == null) {
            mRequestParam = new ArrayList<>();
        }

        LoadBasicDataWrapper task = new LoadBasicDataWrapper();
        task.isByPage = false;
        task.queryType = "ZZ";
        mRequestParam.add(task);

        //如果需要二级单位的组织机构
        switch (BuildConfig.APP_NAME) {
            case Global.QYSH:
                task = new LoadBasicDataWrapper();
                task.isByPage = false;
                task.queryType = "ZZ2";
                mRequestParam.add(task);
                break;
        }

        //获取扩展字段的字典
        task = new LoadBasicDataWrapper();
        task.isByPage = false;
        task.queryType = "SD";
        mRequestParam.add(task);

        mPresenter.loadAndSaveBasicData(mRequestParam);
    }


    /**
     * 同步基础数据出错
     *
     * @param message
     */
    @Override
    public void syncDataError(String message) {
        if (BuildConfig.LOG_DEBUG && BuildConfig.APP_NAME.equals(Global.QYSH)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示")
                    .setMessage("同步基础数据出错:" + message);
            builder.setPositiveButton("继续", (dialog, which) -> {
                dialog.dismiss();
                toLogin();
            });
            builder.show();
        } else {
            toLogin();
        }
    }

    /**
     * 如果基础数据下载完毕或者不是第一次进入该app那么直接同步基础数据
     */
    @Override
    public void downDBComplete() {
        Log.d("yff", "下载基础DB成功");
        SPrefUtil.saveData(Global.IS_APP_FIRST_KEY, false);
        startSyncBasicData();
    }

    @Override
    public void downDBFail(String message) {
        showMessage(message);
        Log.d("yff", "下载基础DB失败");
        //如果下载错误，强制下次下载
        SPrefUtil.saveData(Global.IS_APP_FIRST_KEY, true);
        toLogin();
    }


    @Override
    public void networkConnectError(String retryAction) {
        showNetConnectErrorDialog(retryAction);
    }

    @Override
    public void retry(String action) {
        switch (action) {
            case Global.RETRY_SYNC_BASIC_DATA_ACTION:
                mPresenter.loadAndSaveBasicData(mRequestParam);
                break;
        }
        //回调父方法关闭对话框
        super.retry(action);
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.isDisposed();
        }
        super.onDestroy();
    }
}
