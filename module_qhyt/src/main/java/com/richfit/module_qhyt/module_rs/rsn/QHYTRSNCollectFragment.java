package com.richfit.module_qhyt.module_rs.rsn;


import android.text.TextUtils;

import com.richfit.sdk_wzrs.base_rsn_collect.BaseRSNCollectFragment;
import com.richfit.sdk_wzrs.base_rsn_collect.imp.RSNCollectPresenterImp;

/**
 * Created by monday on 2017/3/2.
 */

public class QHYTRSNCollectFragment extends BaseRSNCollectFragment<RSNCollectPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new RSNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

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

        if ("46".equals(mBizType) && TextUtils.isEmpty(mRefData.costCenter)) {
            showMessage("请先在抬头界面输入成本中心");
            return;
        }
        if ("47".equals(mBizType) && TextUtils.isEmpty(mRefData.projectNum)) {
            showMessage("请现在抬头界面输入项目编号");
            return;
        }
        super.initDataLazily();
    }
}
