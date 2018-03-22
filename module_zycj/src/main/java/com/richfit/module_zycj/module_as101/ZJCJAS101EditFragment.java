package com.richfit.module_zycj.module_as101;

import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

/**
 * Created by monday on 2018/1/2.
 */

public class ZJCJAS101EditFragment extends BaseASEditFragment<ASEditPresenterImp> {
    @Override
    protected void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }

    @Override
    protected void initDataLazily() {

    }
}
