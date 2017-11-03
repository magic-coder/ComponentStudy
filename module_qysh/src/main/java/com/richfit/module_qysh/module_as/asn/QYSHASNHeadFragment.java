package com.richfit.module_qysh.module_as.asn;

import com.richfit.module_qysh.module_as.asn.imp.QHSYASNHeadPresenterImp;
import com.richfit.sdk_wzrk.base_asn_head.BaseASNHeadFragment;
import com.richfit.sdk_wzrk.base_asn_head.imp.ASNHeadPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHASNHeadFragment extends BaseASNHeadFragment<QHSYASNHeadPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new QHSYASNHeadPresenterImp(mActivity);
    }

    @Override
    protected void initDataLazily() {

    }
}
