package com.richfit.module_qysh.module_ys.head;


import android.support.annotation.NonNull;

import com.richfit.sdk_wzys.basehead.BaseApprovalHeadFragment;
import com.richfit.sdk_wzys.basehead.imp.ApprovalHeadPresenterImp;

/**
 * 庆阳验收其他抬头界面
 * Created by monday on 2016/11/23.
 */

public class QYSHAOHeadFragment extends BaseApprovalHeadFragment {


    @Override
    public void initPresenter() {
        mPresenter = new ApprovalHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "00";
    }
}
