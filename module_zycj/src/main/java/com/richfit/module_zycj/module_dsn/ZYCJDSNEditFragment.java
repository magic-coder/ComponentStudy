package com.richfit.module_zycj.module_dsn;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;
import com.richfit.sdk_wzck.base_dsn_edit.BaseDSNEditFragment;
import com.richfit.sdk_wzck.base_dsn_edit.imp.DSNEditPresenterImp;

/**
 * Created by monday on 2018/1/10.
 */

public class ZYCJDSNEditFragment extends BaseDSNEditFragment<DSNEditPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new DSNEditPresenterImp(mActivity);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.moveCause = mRefData.moveCause;
        result.moveType = mRefData.moveType;
        result.projectNum = mRefData.projectNum;
        result.costCenter = mRefData.costCenter;
        return result;
    }
}
