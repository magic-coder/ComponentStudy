package com.richfit.module_cqyt.module_ys;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzys.basehead.BaseApprovalHeadFragment;
import com.richfit.sdk_wzys.basehead.imp.ApprovalHeadPresenterImp;

/**
 * Created by monday on 2017/7/5.
 */

public class CQYTAOHeadFragment extends BaseApprovalHeadFragment {

    @Override
    public void initPresenter() {
        mPresenter = new ApprovalHeadPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        super.initView();
        llArrivalDate.setVisibility(View.GONE);
        LinearLayout llInspectionPerson = (LinearLayout) mView.findViewById(R.id.ll_inspection_person);
        llInspectionPerson.setVisibility(View.GONE);
        llInspectionType.setVisibility(View.GONE);
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
