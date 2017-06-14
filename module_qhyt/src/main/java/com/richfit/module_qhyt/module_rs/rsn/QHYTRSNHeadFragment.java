package com.richfit.module_qhyt.module_rs.rsn;


import com.richfit.sdk_wzrs.base_rsn_head.BaseRSNHeadFragment;
import com.richfit.sdk_wzrs.base_rsn_head.imp.RSNHeadPresenterImp;

/**
 * Created by monday on 2017/3/2.
 */

public class QHYTRSNHeadFragment extends BaseRSNHeadFragment<RSNHeadPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new RSNHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }
}
