package com.richfit.common_lib.lib_mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.richfit.common_lib.R;
import com.richfit.common_lib.lib_dialog.NetConnectErrorDialogFragment;
import com.richfit.common_lib.lib_dialog.ShowErrorMessageDialog;
import com.richfit.common_lib.lib_interface.IFragmentState;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by monday on 2016/11/10.
 */

public abstract class BaseFragment<P extends IPresenter> extends Fragment implements BaseView,
        NetConnectErrorDialogFragment.INetworkConnectListener, IFragmentState {
    /*抬头界面索引*/
    public static final int HEADER_FRAGMENT_INDEX = 0x0;
    /*明细界面索引*/
    public static final int DETAIL_FRAGMENT_INDEX = 0x1;
    /*数据采集界面索引*/
    public static final int COLLECT_FRAGMENT_INDEX = 0x2;

    private boolean isActivityCreated;
    protected View mView;
    protected P mPresenter;
    /*宿主activity*/
    protected Activity mActivity;
    private Unbinder mUnbinder;
    protected NetConnectErrorDialogFragment mNetConnectErrorDialogFragment;

    /*抬头界面，数据明细界面，数据采集界面共享的单据数据，注意我们需要将单据数据和缓存数据隔离*/
    protected static ReferenceEntity mRefData;
    /*对于委外入库，组件界面的明细界面和数据采集界面共享的数据明细*/
    protected static List<RefDetailEntity> mRefDetail;
    protected String mCompanyCode;
    protected String mModuleCode;
    protected String mBizType;
    protected String mRefType;
    protected int mFragmentType;
    /*该页签名称*/
    protected String mTabTitle;
    /*批次管理，默认是打开的*/
    protected boolean isOpenBatchManager = true;
    /*仓储类型*/
    protected boolean isOpenLocationType = false;
    protected boolean isOpenRecLocationType = false;
    /*底部按钮菜单的数据*/
    private static List<BottomMenuEntity> mBottomMenus;
    /*获取库存的数据*/
    private static InventoryQueryParam mInventoryParam;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCompanyCode = bundle.getString(Global.EXTRA_COMPANY_CODE_KEY);
            mModuleCode = bundle.getString(Global.EXTRA_MODULE_CODE_KEY);
            mBizType = bundle.getString(Global.EXTRA_BIZ_TYPE_KEY);
            mRefType = bundle.getString(Global.EXTRA_REF_TYPE_KEY);
            mTabTitle = bundle.getString(Global.EXTRA_TITLE_KEY);
            mFragmentType = bundle.getInt(Global.EXTRA_FRAGMENT_TYPE_KEY);
        }
        initVariable(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(getContentId(), container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(BaseFragment.this);
        }
        initView();
        initEvent();
        initData();
        isActivityCreated = true;
    }

    /**
     * 此方法目前仅适用于标示ViewPager中的Fragment是否真实可见.
     * 第一次执行时候，该方法可能早与onCreate方法，此时不适合做业务
     * 因为我们需要在onCreate方法初始化相关变量，所以设置isCreated标志
     * 位。如果isVisibleToUser为true，说明此时该页面真正的对用户可见。
     * 那么可以执行相关的业务。
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isActivityCreated) {
            return;
        }
        if (isVisibleToUser) {
            initDataLazily();
        } else {
            _onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mView = null;
        mPresenter = null;
    }

    //获取布局文件
    protected abstract int getContentId();

    //初始化presenter层
    protected abstract void initPresenter();

    protected void showMessage(String message) {
        if (mView != null) {
            Snackbar.make(mView, message, Snackbar.LENGTH_LONG).show();
        } else {
            Toast.makeText(mActivity.getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @CheckResult
    protected String getString(View view) {
        if (view != null && TextView.class.isInstance(view)) {
            TextView tv = (TextView) view;
            return CommonUtil.Obj2String(tv.getText());
        }
        return "";
    }

    protected int getInteger(int resId) {
        if (resId != 0) {
            return mActivity.getResources().getInteger(resId);
        }
        return -1;
    }

    protected void hideKeyboard(View view) {
        if (view == null)
            view = mActivity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示网络错误重试对话框
     *
     * @param action：重试的事件类型
     */
    protected void showNetConnectErrorDialog(String action) {
        AppCompatActivity activity = (AppCompatActivity) mActivity;
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        mNetConnectErrorDialogFragment = (NetConnectErrorDialogFragment) supportFragmentManager.findFragmentByTag("NETWORK_CONNECT_ERROR_TAG");
        if (mNetConnectErrorDialogFragment == null) {
            mNetConnectErrorDialogFragment = NetConnectErrorDialogFragment.newInstance(action);
            mNetConnectErrorDialogFragment.setINetworkConnectListener(this);
        }
        mNetConnectErrorDialogFragment.show(supportFragmentManager, Global.NETWORK_CONNECT_ERROR_TAG);
    }

    @Override
    public void retry(String action) {
        if (mNetConnectErrorDialogFragment != null) {
            mNetConnectErrorDialogFragment.dismiss();
        }
    }

    @Override
    public void networkConnectError(String retryAction) {
        showNetConnectErrorDialog(retryAction);
    }

    protected boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    protected void showSuccessDialog(CharSequence message) {
        //注意这里需要处理\n和-----分割符
        if (TextUtils.isEmpty(message))
            return;
        String[] msgs = message.toString().split("______");
        StringBuffer msg = new StringBuffer();
        for (String s : msgs) {
            if (msg.indexOf(s) > 0)
                continue;
            msg.append(s);
        }
        new SweetAlertDialog(mActivity, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("温馨提示")
                .setContentText(msg.toString())
                .show();
    }

    protected void showFailDialog(String message) {
        new SweetAlertDialog(mActivity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("温馨提示")
                .setContentText(message)
                .show();
    }

    protected void showErrorDialog(CharSequence message) {
        if (TextUtils.isEmpty(message))
            return;
        String[] errors = message.toString().split("______");
        //注意这里使用多态性质，父类AppCompatActivity中的FragmentManager
        AppCompatActivity activity = (AppCompatActivity) mActivity;
        FragmentManager fm = activity.getSupportFragmentManager();
        ShowErrorMessageDialog dialog = ShowErrorMessageDialog.newInstance(errors);
        dialog.show(fm, "nms_show_error_messages");
    }


    protected void setAutoCompleteConfig(AutoCompleteTextView autoComplete) {
        autoComplete.setThreshold(1);
        autoComplete.setDropDownWidth(autoComplete.getWidth() * 2);
        autoComplete.isPopupShowing();
    }

    protected void showAutoCompleteConfig(AutoCompleteTextView autoComplete) {
        if (autoComplete.getAdapter() != null) {
            autoComplete.showDropDown();
        }
    }

    public String getTabTitle() {
        return mTabTitle;
    }

    public int getFragmentType() {
        return mFragmentType;
    }

    public void setTabTitle(String title) {
        this.mTabTitle = title;
    }

    protected List<BottomMenuEntity> provideDefaultBottomMenu() {
        if (mBottomMenus == null) {
            mBottomMenus = new ArrayList<>();
        }
        mBottomMenus.clear();
        BottomMenuEntity menu = new BottomMenuEntity();
        menu.menuName = "过账";
        menu.menuImageRes = R.mipmap.icon_transfer;
        mBottomMenus.add(menu);

        menu = new BottomMenuEntity();
        menu.menuName = "上架";
        menu.menuImageRes = R.mipmap.icon_data_submit;
        mBottomMenus.add(menu);

        menu = new BottomMenuEntity();
        menu.menuName = "下架";
        menu.menuImageRes = R.mipmap.icon_down_location;
        mBottomMenus.add(menu);

        menu = new BottomMenuEntity();
        menu.menuName = "记账更改";
        menu.menuImageRes = R.mipmap.icon_detail_transfer;
        mBottomMenus.add(menu);
        return mBottomMenus;
    }

    protected InventoryQueryParam provideInventoryQueryParam() {
        if (mInventoryParam == null) {
            mInventoryParam = new InventoryQueryParam();
        }
        mInventoryParam.reset();
        mInventoryParam.invType = "1";
        mInventoryParam.queryType = "04";
        mInventoryParam.extraMap = null;
        return mInventoryParam;
    }

    //初始化相关变量
    protected abstract void initVariable(@Nullable Bundle savedInstanceState);

    //初始化view
    protected abstract void initView();

    //注册所有的监听事件,可以不实现
    protected abstract void initEvent();

    /**
     * 初始化数据, 可以不实现.该方法在onResume方法中调用，说明在大部分的时候
     * 该方法只会调用一次，也就是我们需要初始化一下静态的数据。比如单据类型等。
     */
    protected abstract void initData();

    //赖加载数据。该方法在Fragment可见的时候调用。可以动态的加载数据
    protected abstract void initDataLazily();

    //该方法在fragment不可见的时候调用，相当于onPause方法，用户保存抬头界面相关数据。
    public void _onPause() {
        if (mView != null)
            hideKeyboard(mView);
    }

    protected List<String> getStringArray(@ArrayRes int id) {
        return Arrays.asList(getResources().getStringArray(id));
    }

    @Override
    public boolean checkDataBeforeOperationOnHeader() {
        return true;
    }

    @Override
    public void operationOnHeader(final String companyCode) {

    }

    @Override
    public boolean checkDataBeforeOperationOnDetail() {
        return true;
    }

    @Override
    public void showOperationMenuOnDetail(String companyCode) {

    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        return true;
    }

    @Override
    public void saveCollectedData() {

    }

    @Override
    public void showOperationMenuOnCollection(String companyCode) {

    }

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {

    }

    /**
     * BaseFragment实现isNeedShowFloatingButton该方法，默认显示FloatingButton
     *
     * @return
     */
    @Override
    public boolean isNeedShowFloatingButton() {
        return true;
    }

    /**
     * 清除公共空控件的数据
     */
    protected void clearCommonUI(View... views) {
        for (View view : views) {
            if (view == null)
                continue;
            if (CheckBox.class.isInstance(view)) {
                CheckBox cb = (CheckBox) view;
                cb.setChecked(false);
            } else if (TextView.class.isInstance(view)) {
                TextView tv = (TextView) view;
                tv.setText("");
            } else if (Spinner.class.isInstance(view)) {
                Spinner sp = (Spinner) view;
                if (sp.getAdapter() != null)
                    sp.setSelection(0);
            }
        }
    }



}
