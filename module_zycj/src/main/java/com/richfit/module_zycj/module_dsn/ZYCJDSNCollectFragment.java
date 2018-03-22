package com.richfit.module_zycj.module_dsn;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzck.base_dsn_collect.BaseDSNCollectFragment;
import com.richfit.sdk_wzck.base_dsn_collect.imp.DSNCollectPresenterImp;

/**
 * Created by monday on 2018/1/10.
 */

public class ZYCJDSNCollectFragment extends BaseDSNCollectFragment<DSNCollectPresenterImp> {
    @Override
    protected void initPresenter() {
        mPresenter = new DSNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

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
