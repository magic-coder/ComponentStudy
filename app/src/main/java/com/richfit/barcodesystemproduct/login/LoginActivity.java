package com.richfit.barcodesystemproduct.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.richfit.barcodesystemproduct.BarcodeSystemApplication;
import com.richfit.barcodesystemproduct.BuildConfig;
import com.richfit.barcodesystemproduct.R;
import com.richfit.barcodesystemproduct.splash.SplashActivity;
import com.richfit.barcodesystemproduct.update.UpdateActivity;
import com.richfit.barcodesystemproduct.welcome.WelcomeActivity;
import com.richfit.common_lib.lib_adapter.BottomDialogMenuAdapter;
import com.richfit.common_lib.lib_mvp.BaseActivity;
import com.richfit.common_lib.utils.FileUtil;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.common_lib.widget.ButtonCircleProgressBar;
import com.richfit.common_lib.widget.RichAutoEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.UpdateEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import zlc.season.rxdownload2.entity.DownloadStatus;

/**
 * Created by monday on 2016/10/27.
 */

public class LoginActivity extends BaseActivity<LoginPresenterImp> implements LoginContract.View {

    public static final String UPDATEI_NFO_KEY = "updateInfo";

    @BindView(R.id.et_username)
    RichAutoEditText etUsername;
    @BindView(R.id.et_password)
    RichAutoEditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.floating_button)
    FloatingActionButton btnShowInfo;


    private TextView mOldIP;
    private EditText mIP1;
    private EditText mIP2;
    private EditText mIP3;
    private EditText mIP4;
    private EditText mEtPort;

    @Override
    protected int getContentId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenterImp(this);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    public void initEvent() {

        etUsername.setOnRichAutoEditTouchListener((view, text) -> etUsername.setText(""));
        etPassword.setOnRichAutoEditTouchListener((view, text) -> etPassword.setText(""));

        RxView.clicks(btnLogin)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(a -> prepareLogin());

        RxView.clicks(etUsername)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(a -> {
                    if (etUsername.getAdapter() != null) {
                        etUsername.setThreshold(0);
                        etUsername.showDropDown();
                    }
                });

        RxAutoCompleteTextView.itemClickEvents(etUsername)
                .delay(50, TimeUnit.MILLISECONDS)
                .subscribe(a -> hideKeyboard(etUsername));

        RxView.clicks(btnShowInfo)
                .subscribe(a -> showChooseDialog());
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.readUserInfos();
        //如果没有网络，不需要上传奔溃日志
        if (mPresenter != null && !mPresenter.isLocal()) {
            if (!BuildConfig.DEBUG) {
                //debug状态不上传异常日志
                mPresenter.uploadCrashLogFiles();
            }
            //强制检查更新
            mPresenter.getAppVersion();
        }
    }

    /**
     * 准备登陆，如果是离线的情况那么直接登陆，如果是在线那么先检查是否注册过了
     */
    private void prepareLogin() {
        if (mPresenter.isLocal()) {
            mPresenter.login(etUsername.getText().toString(),
                    etPassword.getText().toString());
        } else {
            mPresenter.getMappingInfo();
        }
    }

    @Override
    public void registered() {
        mPresenter.login(etUsername.getText().toString(),
                etPassword.getText().toString());
    }

    @Override
    public void unRegister(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("提示")
                .setMessage(message + ".该手持MAC地址是" + Global.MAC_ADDRESS + ".注意注册完成后系统将自动重启APP!")
                .setPositiveButton("已经注册", (dialog1, which) -> {
                    dialog1.dismiss();
                    SPrefUtil.saveData(Global.IS_APP_FIRST_KEY, true);
                    Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();
                }).show();
    }

    @Override
    public void checkAppVersion(UpdateEntity info) {
        //获取当前的版本号
        String currentVersionName = CommonUtil.getCurrentVersionName(this.getApplicationContext());
        float versionName = CommonUtil.convertToFloat(currentVersionName,0.0F);
        float appVersion = CommonUtil.convertToFloat(info.appVersion,0.0F);
        Log.e("yff", "versionName = " + versionName + "; appVersion = " + appVersion);
        if (appVersion > versionName) {
            //提示用户需要更新
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("检测到最新的版本:" + info.appVersion);
            dialog.setMessage(info.appUpdateDesc);
            dialog.setPositiveButton("立即更新", (dialogInterface, i) -> {
                //启动下载页面
                Intent intent = new Intent(LoginActivity.this, UpdateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(UPDATEI_NFO_KEY, info);
                intent.putExtras(bundle);
                startActivity(intent);
            });
            dialog.setNegativeButton("以后再说", (dialogInterface, i) -> dialogInterface.dismiss());
            dialog.show();
        }
    }

    @Override
    public void getUpdateInfoFail(String message) {
        //nothing todo
    }


    @Override
    public void toHome() {
        showMessage("登陆成功");
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void showChooseDialog() {
        View rootView = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_menu, null);
        GridView menu = (GridView) rootView.findViewById(R.id.gd_menus);
        ArrayList<BottomMenuEntity> items = new ArrayList<>();
        BottomMenuEntity item = new BottomMenuEntity();
        item.menuName = "App基本信息";
        item.menuImageRes = R.mipmap.icon_data_submit;
        items.add(item);

        item = new BottomMenuEntity();
        item.menuName = "修改IP";
        item.menuImageRes = R.mipmap.icon_transfer;
        items.add(item);


        BottomDialogMenuAdapter adapter = new BottomDialogMenuAdapter(this, R.layout.item_dialog_bottom_menu, items);
        menu.setAdapter(adapter);

        final Dialog dialog = new Dialog(this, R.style.MaterialDialogSheet);
        dialog.setContentView(rootView);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        menu.setOnItemClickListener((adapterView, view, position, id) -> {
            switch (position) {
                case 0:
                    showAppBasicInfo();
                    break;
                case 1:
                    showEditIPDialog();
                    break;

            }
            dialog.dismiss();
        });
    }

    private void showAppBasicInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        StringBuffer sb = new StringBuffer();
        sb.append("当前使用的系统")
                .append(mPresenter.isLocal() ? "离线系统" : "在线系统")
                .append("\n")
                .append("当前版本号:")
                .append("V(")
                .append(CommonUtil.getCurrentVersionName(this))
                .append(")")
                .append("\n")
                .append("服务器地址:")
                .append(BarcodeSystemApplication.baseUrl);
        builder.setTitle("App信息查看")
                .setMessage(sb.toString())
                .show();
    }


    /**
     * 修改ip
     */
    private void showEditIPDialog() {
        if (mPresenter != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("设置新的IP");
            ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.dialog_ip_manager, null);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            extView.setLayoutParams(lp);
            mOldIP = (TextView) extView.findViewById(R.id.tv_old_ip);
            mIP1 = (EditText) extView.findViewById(R.id.ip1);
            mIP2 = (EditText) extView.findViewById(R.id.ip2);
            mIP3 = (EditText) extView.findViewById(R.id.ip3);
            mIP4 = (EditText) extView.findViewById(R.id.ip4);
            mEtPort = (EditText) extView.findViewById(R.id.et_port);
            int indexOf = BarcodeSystemApplication.baseUrl.indexOf("/", 7);
            if (indexOf < 7) {
                mOldIP.setVisibility(ViewGroup.GONE);
            } else {
                String ip = BarcodeSystemApplication.baseUrl.substring(7, indexOf);
                mOldIP.setText("当前的ip是:" + ip);
            }
            builder.setView(extView);
            builder.setPositiveButton("确定", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                prepareEditIP();

            }).setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss()).show();
        }
    }

    @Override
    public void loginFail(String message) {
        showMessage(message);
        etUsername.setAdapter(null);
    }

    @Override
    public void showUserInfos(ArrayList<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, list);
        etUsername.setAdapter(adapter);
    }

    @Override
    public void loadUserInfosFail(String message) {
        showMessage(message);
    }


    @Override
    public void networkConnectError(String retryAction) {
        showNetConnectErrorDialog(Global.RETRY_LOGIN_ACTION);
    }

    @Override
    public void setupUrlComplete() {
        //启动SplashActivity
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish();
    }

    private void prepareEditIP() {
        hideKeyboard(mIP1);
        //判断是否是拓展窗口View，而且点击的是非取消按钮

        if (!checkIP(mIP1, mIP2, mIP3, mIP4)) {
            showMessage("您输入的IP不合理");
            return;
        }

        final String port = mEtPort.getText().toString();
        if (TextUtils.isEmpty(port)) {
            showMessage("请输入端口号");
            return;
        }
        //1. 先拿到当前服务器地址的资源名
        final String url = BarcodeSystemApplication.baseUrl;
        final StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(url)) {
            int indexOf = url.indexOf("/", 7);//这里去除http://
            if (indexOf > 0) {
                String webURI = url.substring(indexOf);
                sb.append("http://")
                        .append(mIP1.getText())
                        .append(".")
                        .append(mIP2.getText())
                        .append(".")
                        .append(mIP3.getText())
                        .append(".")
                        .append(mIP4.getText())
                        .append(":")
                        .append(port)
                        .append(webURI);
            }
        }
        final String newUrl = sb.toString();
        if (TextUtils.isEmpty(newUrl)) {
            showMessage("服务为空");
            return;
        }
        mPresenter.setupUrl(newUrl);
        return;
    }

    private boolean checkIP(EditText... ets) {
        for (EditText et : ets) {
            int ip = CommonUtil.convertToInt(et.getText(), 0);
            if (ip <= 0 || ip > 255) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void retry(String action) {
        switch (action) {
            case Global.RETRY_LOGIN_ACTION:
                //因为登陆有可能是注册也有可能是登陆接口超时，所以统一重新走
                prepareLogin();
                break;
            case Global.RETRY_REGISTER_ACTION:
                mPresenter.getMappingInfo();
                break;
        }
        super.retry(action);
    }

}
