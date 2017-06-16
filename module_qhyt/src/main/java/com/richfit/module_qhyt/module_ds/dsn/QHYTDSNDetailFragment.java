package com.richfit.module_qhyt.module_ds.dsn;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.richfit.sdk_wzck.base_dsn_detail.BaseDSNDetailFragment;
import com.richfit.sdk_wzck.base_dsn_detail.imp.DSNDetailPresenterImp;

/**
 * Created by monday on 2017/3/27.
 */

public class QHYTDSNDetailFragment extends BaseDSNDetailFragment<DSNDetailPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new DSNDetailPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initDataLazily() {
        if (mRefData == null) {
            showMessage("请先在抬头界面选择工厂");
            return;
        }
        if ("26".equals(mBizType) && TextUtils.isEmpty(mRefData.costCenter)) {
            showMessage("请先在抬头界面输入成本中心");
            return;
        }
        if ("27".equals(mBizType) && TextUtils.isEmpty(mRefData.projectNum)) {
            showMessage("请现在抬头界面输入项目编号");
            return;
        }
        super.initDataLazily();
    }
}
