package com.richfit.module_xngd.module_ys;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzys.basehead.BaseApprovalHeadFragment;
import com.richfit.sdk_wzys.basehead.imp.ApprovalHeadPresenterImp;

/**
 * Created by monday on 2017/6/26.
 */

public class XNGDAOHeadFragment extends BaseApprovalHeadFragment {


    @Override
    public void initPresenter() {
        mPresenter = new ApprovalHeadPresenterImp(mActivity);
    }


    @Override
    public void initDataLazily() {

    }

    @Override
    public void initView() {
        super.initView();
        llInspectionType.setVisibility(View.GONE);
        llArrivalDate.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "0";
    }
}
