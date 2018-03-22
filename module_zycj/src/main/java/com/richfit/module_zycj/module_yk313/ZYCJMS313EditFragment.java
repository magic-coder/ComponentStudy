package com.richfit.module_zycj.module_yk313;

import android.view.View;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzyk.base_msn_edit.BaseMSNEditFragment;
import com.richfit.sdk_wzyk.base_msn_edit.imp.MSNEditPresenterImp;

/**
 * Created by monday on 2017/11/3.
 */

public class ZYCJMS313EditFragment extends BaseMSNEditFragment<MSNEditPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new MSNEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        //隐藏接收仓位
        llRecLocation.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initDataLazily() {

    }

}
