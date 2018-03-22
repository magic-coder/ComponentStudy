package com.richfit.module_zycj.module_assc;

import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

/**
 * Created by monday on 2018/1/2.
 */

public class ZYCJASSCCollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }
}
