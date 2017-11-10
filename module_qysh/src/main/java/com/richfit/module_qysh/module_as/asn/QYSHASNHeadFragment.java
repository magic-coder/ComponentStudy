package com.richfit.module_qysh.module_as.asn;

import com.richfit.domain.bean.ResultEntity;
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

    @Override
    public void _onPause() {
        super._onPause();
        if(mRefData != null) {
            mRefData.specialInvFlag = "K";
            //这里需要供应商code
            mRefData.supplierNum = getString(etSupplier).split("_")[0];
        }
    }
}
