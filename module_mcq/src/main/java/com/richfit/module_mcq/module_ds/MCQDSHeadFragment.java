package com.richfit.module_mcq.module_ds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.richfit.data.constant.Global;
import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;

/**
 * Created by monday on 2017/8/31.
 */

public class MCQDSHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new DSHeadPresenterImp(mActivity);

    }

    @Override
    public void initView() {
        //打开客户
        llCustomer.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        //读取Intent传递过来的refNum,如果存在那么自动加载数据
        Bundle arguments = getArguments();
        if (arguments != null) {
            String refNum = arguments.getString(Global.EXTRA_REF_NUM_KEY);
            if (!TextUtils.isEmpty(refNum)) {
                getRefData(refNum);
            }
        }
    }

    @Override
    public void initDataLazily() {

    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "2";
    }
}
