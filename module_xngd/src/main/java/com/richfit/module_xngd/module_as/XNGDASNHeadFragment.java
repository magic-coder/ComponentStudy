package com.richfit.module_xngd.module_as;

import android.view.View;

import com.richfit.module_xngd.module_as.imp.XNGDASNHeadPresenterImp;
import com.richfit.sdk_wzrk.base_asn_head.BaseASNHeadFragment;

/**
 * Created by monday on 2017/6/23.
 */

public class XNGDASNHeadFragment extends BaseASNHeadFragment<XNGDASNHeadPresenterImp>{

    @Override
    public void initPresenter() {
        mPresenter = new XNGDASNHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void initView() {
        super.initView();
        llSupplier.setVisibility(View.GONE);
    }


}
