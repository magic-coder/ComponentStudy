package com.richfit.module_qhyt.module_as.as105n;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;


/**
 * 青海105入库非必检抬头界面
 * Created by monday on 2017/2/20.
 */

public class QHYTAS105NHeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {


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

    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }

}
