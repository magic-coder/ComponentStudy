package com.richfit.module_qhyt.module_rs.rsy;


import android.view.View;

import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * 青海转储退库抬头界面
 * Created by monday on 2017/2/27.
 */

public class QHYTRSYHeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        llSendWork.setVisibility(View.VISIBLE);
        llCreator.setVisibility(View.VISIBLE);
        super.initView();
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    protected String getMoveType() {
        return "352";
    }

}
