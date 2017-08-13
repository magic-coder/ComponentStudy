package com.richfit.module_qhyt.module_ds.dsn;


import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzck.base_dsn_edit.BaseDSNEditFragment;
import com.richfit.sdk_wzck.base_dsn_edit.imp.DSNEditPresenterImp;

/**
 * Created by monday on 2017/3/27.
 */

public class QHYTDSNEditFragment extends BaseDSNEditFragment<DSNEditPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new DSNEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.zzzdy9 = mRefData.zzzdy9;
        result.zzzxlb = mRefData.zzzxlb;
        result.zzzxnr = mRefData.zzzxnr;
        return result;
    }
}
