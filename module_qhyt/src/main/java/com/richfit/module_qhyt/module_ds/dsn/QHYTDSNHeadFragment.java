package com.richfit.module_qhyt.module_ds.dsn;


import com.richfit.module_qhyt.module_ds.dsn.imp.QHYTDSNHeadPresenterImp;
import com.richfit.sdk_wzck.base_dsn_head.BaseDSNHeadFragment;

/**
 * Created by monday on 2017/3/27.
 */

public class QHYTDSNHeadFragment extends BaseDSNHeadFragment<QHYTDSNHeadPresenterImp> {

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
        //如果BizType是26那么显示成本中心,否者显示项目编号
        if ("27".equalsIgnoreCase(mBizType)) {
            tvAutoCompName.setText("项目编号");
        }
    }
}
