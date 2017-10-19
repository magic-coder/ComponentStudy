package com.richfit.module_qhyt.module_sxcl;

import com.richfit.sdk_sxcl.basedetail.LocQTDetailFragment;
import com.richfit.sdk_sxcl.basedetail.imp.LocQTDetailPresenterImp;

/**
 * Created by monday on 2017/10/18.
 */

public class QHYTLocQTDetailFragment extends LocQTDetailFragment<LocQTDetailPresenterImp> {
    @Override
    public void initPresenter() {
        mPresenter = new LocQTDetailPresenterImp(mActivity);
    }
}
