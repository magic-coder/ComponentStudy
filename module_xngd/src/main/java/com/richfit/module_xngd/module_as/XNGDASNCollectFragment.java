package com.richfit.module_xngd.module_as;

import com.richfit.sdk_wzrk.base_asn_collect.BaseASNCollectFragment;
import com.richfit.sdk_wzrk.base_asn_collect.imp.ASNCollectPresenterImp;

/**
 * Created by monday on 2017/6/23.
 */

public class XNGDASNCollectFragment extends BaseASNCollectFragment<ASNCollectPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new ASNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {

    }
}
