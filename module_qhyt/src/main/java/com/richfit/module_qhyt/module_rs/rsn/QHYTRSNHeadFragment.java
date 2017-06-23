package com.richfit.module_qhyt.module_rs.rsn;


import com.richfit.module_qhyt.module_rs.rsn.imp.QHYTRSNHeadPresenterImp;
import com.richfit.sdk_wzrs.base_rsn_head.BaseRSNHeadFragment;

/**
 * Created by monday on 2017/3/2.
 */

public class QHYTRSNHeadFragment extends BaseRSNHeadFragment<QHYTRSNHeadPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new QHYTRSNHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    protected void initView() {
        //如果BizType是46那么显示成本中心,否者显示项目编号
        if ("47".equalsIgnoreCase(mBizType)) {
            tvAutoCompName.setText("项目编号");
        }
    }

    @Override
    public void _onPause() {
        super._onPause();
        if(mRefData != null) {
            if ("47".equalsIgnoreCase(mBizType)) {
                mRefData.projectNum = getString(etAutoComp).split("_")[0];
            } else {
                mRefData.costCenter = getString(etAutoComp).split("_")[0];
            }
        }
    }

}
