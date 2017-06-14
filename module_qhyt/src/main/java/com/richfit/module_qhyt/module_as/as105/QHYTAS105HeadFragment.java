package com.richfit.module_qhyt.module_as.as105;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;


/**
 * 青海物资入库105必检抬头界面(bizType为110)
 * Created by monday on 2017/3/1.
 */

public class QHYTAS105HeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {


    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        llSupplier.setVisibility(View.VISIBLE);
        super.initView();
    }

    @Override
    public void initDataLazily() {

    }
}
