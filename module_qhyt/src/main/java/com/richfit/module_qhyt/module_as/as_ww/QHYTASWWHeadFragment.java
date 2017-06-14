package com.richfit.module_qhyt.module_as.as_ww;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * 青海委外入库抬头界面
 * Created by monday on 2017/2/28.
 */

public class QHYTASWWHeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

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
