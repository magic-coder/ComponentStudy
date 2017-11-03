package com.richfit.module_cqyt.module_as105;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;


/**
 * 青海物资入库105必检抬头界面(bizType为110)
 * Created by monday on 2017/3/1.
 */

public class CQYTAS105HeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        llSupplier.setVisibility(View.VISIBLE);
    }

    @Override
    public void initDataLazily() {

    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }
}
