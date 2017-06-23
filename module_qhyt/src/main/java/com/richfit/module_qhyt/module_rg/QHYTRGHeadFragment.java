package com.richfit.module_qhyt.module_rg;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;

/**
 * Created by monday on 2017/2/23.
 */

public class QHYTRGHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {

    private TextView tvInvFlag;
    private Spinner spGoodsTypes;

    @Override
    public void initPresenter() {
        mPresenter = new DSHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        llSuppier.setVisibility(View.VISIBLE);
        llCreator.setVisibility(View.GONE);
        super.initView();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public boolean isNeedShowFloatingButton() {
        return false;
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "2";
    }

}
