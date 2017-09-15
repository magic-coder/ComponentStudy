package com.richfit.barcodesystemproduct.update;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.richfit.barcodesystemproduct.R;
import com.richfit.barcodesystemproduct.login.LoginActivity;
import com.richfit.barcodesystemproduct.view.NumberProgressBar;
import com.richfit.common_lib.lib_mvp.BaseActivity;
import com.richfit.common_lib.utils.FileUtil;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.UpdateEntity;

import java.io.File;

import zlc.season.rxdownload2.entity.DownloadStatus;

/**
 * Created by monday on 2017/9/5.
 */

public class UpdateActivity extends BaseActivity<UpdateContract.Presenter> implements
        UpdateContract.View, View.OnClickListener {

    UpdateEntity mUpdateInfo;
    TextView tvUpdateDesc;
    NumberProgressBar progressbar;



    @Override
    protected int getContentId() {
        return R.layout.activity_update;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new UpdatePresenterImp(this);
    }

    @Override
    protected void initVariables() {
        mUpdateInfo = null;
        //读取数据
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            UpdateEntity updateInfo = bundle.getParcelable(LoginActivity.UPDATEI_NFO_KEY);
            mUpdateInfo = updateInfo;
        }
    }

    @Override
    protected void initViews() {
        tvUpdateDesc = (TextView) findViewById(R.id.tv_update_desc);
        progressbar = (NumberProgressBar) findViewById(R.id.progress_bar);
        findViewById(R.id.btn_start_load).setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //显示更新信息
       if (mUpdateInfo != null) {
            tvUpdateDesc.setText(mUpdateInfo.appUpdateDesc);
        }
    }

    @Override
    protected void initEvent() {
        progressbar.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (mUpdateInfo != null) {
            startLoadLatestApk(mUpdateInfo.appDownloadUrl, mUpdateInfo.appName);
        }
    }

    /**
     * 开始下载app
     *
     * @param url
     * @param saveName
     */
    private void startLoadLatestApk(String url, String saveName) {
        String apkCacheDir = FileUtil.getApkCacheDir(this.getApplicationContext());
        mPresenter.loadLatestApp(url, saveName, apkCacheDir);
    }


    @Override
    public void loadLatestAppFail(String message) {
        showMessage(message);
    }

    @Override
    public void showLoadAppProgress(DownloadStatus status) {
        long downloadSize = status.getDownloadSize();
        long totalSize = status.getTotalSize();
        progressbar.setProgress((int) ((downloadSize * 1.0F / totalSize) * 100));
    }

    @Override
    public void loadAppComplete() {
        //自动安装
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("温馨提示")
                .setMessage("下载成功,是否现在安装?在安装新版本之前请确认你是否有离线的业务数据未上传!")
                .setPositiveButton("现在安装", (dialog1, which) -> {
                    dialog1.dismiss();
                    autoInstall();
                }).setNegativeButton("取消安装", (dialog12, which) -> {
            dialog12.dismiss();
            deleteApp();
        }).show();
    }


    private void autoInstall() {
        //如果新版本不能覆盖旧版本的数据，那么必须让它从新下载
        SPrefUtil.saveData(Global.IS_APP_FIRST_KEY, true);
        SPrefUtil.saveData(Global.IS_INITED_FRAGMENT_CONFIG_KEY, false);
        mPresenter.setLocal(false);
        String apkCacheDir = FileUtil.getApkCacheDir(this.getApplicationContext());
        String appName = mUpdateInfo.appName;
        File file = new File(apkCacheDir, appName);
        if (file == null || !file.exists()) {
            showMessage("文件不存在");
            return;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void deleteApp() {
        String apkCacheDir = FileUtil.getApkCacheDir(this.getApplicationContext());
        String appName = mUpdateInfo.appName;
        File file = new File(apkCacheDir, appName);
        if (file == null || !file.exists()) {
            showMessage("文件不存在");
            return;
        }
        file.delete();
    }


    @Override
    public void networkConnectError(String retryAction) {

    }


}
