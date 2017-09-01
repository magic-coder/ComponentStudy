package com.richfit.module_qhyt.module_ds.dsn;


import android.text.TextUtils;

import com.richfit.data.constant.Global;
import com.richfit.sdk_wzck.base_dsn_head.BaseDSNHeadFragment;
import com.richfit.sdk_wzck.base_dsn_head.imp.DSNHeadPresenterImp;

/**
 * 202显示项目编号
 * Created by monday on 2017/3/27.
 */

public class QHYTDSNHead202Fragment extends BaseDSNHeadFragment<DSNHeadPresenterImp> {

    @Override
    protected int getOrgFlag() {
        return 0;
    }



    @Override
    public void initPresenter() {
        mPresenter = new DSNHeadPresenterImp(mActivity);
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
            String projectNum = getString(etAutoComp);
            if(!TextUtils.isEmpty(projectNum)) {
                mRefData.projectNum = projectNum.split("_")[0];
            }
        }
    }

    @Override
    protected String getAutoComDataType() {
        return Global.PROJECT_NUM_DATA;
    }
}
