package com.richfit.common_lib.lib_base_sdk.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.richfit.common_lib.R;
import com.richfit.common_lib.lib_mvp.BaseActivity;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.RefUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BizFragmentConfig;

/**
 * 修改页面基类
 * Created by monday on 2016/11/19.
 */

public class EditActivity extends BaseActivity<EditPresenterImp> implements IEditContract.View
,View.OnClickListener{

    private FloatingActionButton mBtnSaveModifiedData;
//    protected FragmentManager mFragmentManager;
    protected BaseFragment mFragment = null;
    protected String mTitle;
    protected String mCompanyCode;
    protected String mBizType;
    protected String mRefType;

    @Override
    protected int getContentId() {
        return R.layout.activity_edit;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new EditPresenterImp(this);
    }


    @Override
    public void initVariables() {
        mFragment = null;
//        if (mFragmentManager == null) {
//            mFragmentManager = getSupportFragmentManager();
//        }
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mTitle = bundle.getString(Global.EXTRA_TITLE_KEY);
                mCompanyCode = bundle.getString(Global.EXTRA_COMPANY_CODE_KEY);
                mBizType = bundle.getString(Global.EXTRA_BIZ_TYPE_KEY);
                mRefType = bundle.getString(Global.EXTRA_REF_TYPE_KEY);
            }
        }
    }

    @Override
    public void initViews() {
        setupToolBar(R.id.toolbar, R.id.toolbar_title, mTitle);
        mBtnSaveModifiedData = (FloatingActionButton) findViewById(R.id.floating_button);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.setupEditFragment(mBizType, mRefType, getResources().getInteger(R.integer.edit_fragment_type));
    }

    @Override
    protected void initEvent() {
        mBtnSaveModifiedData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mFragment != null) {
            mFragment.showOperationMenuOnCollection(mCompanyCode);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.anim_slide_still, R.anim.anim_slide_out_right);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showEditFragment(BizFragmentConfig fragmentConfig) {
        final String fragmentTag = fragmentConfig.fragmentTag;
        final String className = fragmentConfig.className;
        if (TextUtils.isEmpty(fragmentTag) || TextUtils.isEmpty(className)) {
            showMessage("不能初始化修改界面");
            return;
        }
        try {
            mFragment = RefUtil.newInstance(className,getIntent().getExtras());
            if (mFragment != null)
                getSupportFragmentManager().beginTransaction().replace(R.id.edit_content, mFragment, fragmentTag).commit();
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }

    @Override
    public void initEditFragmentFail(String message) {
        showMessage("初始化修改界面错误" + message);
    }

    @Override
    public void onDestroy() {
        mFragment = null;
        super.onDestroy();
    }


}
