package com.richfit.module_zycj.module_yk315;

import android.view.View;

import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.domain.bean.InvEntity;
import com.richfit.module_zycj.R;
import com.richfit.sdk_wzyk.base_msn_head.BaseMSNHeadFragment;
import com.richfit.sdk_wzyk.base_msn_head.imp.MSNHeadPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/11/3.
 */

public class ZYCJMS315HeadFragment extends BaseMSNHeadFragment<MSNHeadPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new MSNHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        //只有工厂
        tvSendWorkName.setText("工厂");
        llRecWork.setVisibility(View.GONE);
        llSendInv.setVisibility(View.GONE);
        llRecInv.setVisibility(View.GONE);
    }

    @Override
    protected void initDataLazily() {

    }


    @Override
    public void showSendInvs(List<InvEntity> sendInvs) {

    }

    @Override
    public void loadSendInvsFail(String message) {

    }

    @Override
    public void loadSendInvsComplete() {

    }

    @Override
    public void showRecInvs(List<InvEntity> recInvs) {

    }

    @Override
    public void loadRecInvsFail(String message) {

    }

    @Override
    public void loadRecInvsComplete() {

    }

    @Override
    protected String getMoveType() {
        return "3";
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }


    @Override
    public void _onPause() {
        super._onPause();
        //工厂内移库，默认接收工厂默认等于接收工厂
        if (mRefData != null) {
            mRefData.recWorkName = mRefData.workName;
            mRefData.recWorkCode = mRefData.workCode;
            mRefData.recWorkId = mRefData.workId;
        }
    }
}
