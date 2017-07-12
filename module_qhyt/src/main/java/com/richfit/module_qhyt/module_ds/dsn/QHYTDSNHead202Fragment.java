package com.richfit.module_qhyt.module_ds.dsn;


import com.richfit.module_qhyt.module_ds.dsn.imp.QHYTDSNHeadPresenterImp;
import com.richfit.sdk_wzck.base_dsn_head.BaseDSNHeadFragment;

/**
 * Created by monday on 2017/3/27.
 */

public class QHYTDSNHead202Fragment extends BaseDSNHeadFragment<QHYTDSNHeadPresenterImp> {

    @Override
    protected int getOrgFlag() {
        return 0;
    }


    @Override
    public void initPresenter() {
        mPresenter = new QHYTDSNHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    protected void initView() {
        tvAutoCompName.setText("项目编号");
    }

    @Override
    public void _onPause() {
        super._onPause();
        if(mRefData != null) {
            mRefData.projectNum = getString(etAutoComp).split("_")[0];
        }
    }
}
