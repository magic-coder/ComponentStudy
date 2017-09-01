package com.richfit.module_qysh.module_ms;


import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.domain.bean.InvEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzyk.base_msn_head.BaseMSNHeadFragment;
import com.richfit.sdk_wzyk.base_msn_head.imp.MSNHeadPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/2/8.
 */

public class QYSHMSN301HeaderFragment extends BaseMSNHeadFragment<MSNHeadPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new MSNHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void showSendInvs(List<InvEntity> sendInvs) {
        mSendInvs.clear();
        mSendInvs.addAll(sendInvs);
        if (mSendInvAdapter == null) {
            mSendInvAdapter = new InvAdapter(mActivity, R.layout.item_simple_sp, mSendInvs);
            spSendInv.setAdapter(mSendInvAdapter);
        } else {
            mSendInvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showRecInvs(List<InvEntity> recInvs) {
        mRecInvs.clear();
        mRecInvs.addAll(recInvs);
        if (mRecInvAdapter == null) {
            mRecInvAdapter = new InvAdapter(mActivity, R.layout.item_simple_sp, mRecInvs);
            spRecInv.setAdapter(mRecInvAdapter);
        } else {
            mRecInvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadSendInvsFail(String message) {
        showMessage("获取发出库位失败;" + message);
    }


    @Override
    public void loadSendInvsComplete() {

    }


    @Override
    public void loadRecInvsFail(String message) {
        showMessage("获取接收库位失败;" + message);
    }

    @Override
    public void loadRecInvsComplete() {

    }

    @Override
    protected String getMoveType() {
        return "5";
    }

    @Override
    protected int getOrgFlag() {
        return getInteger(R.integer.orgSecond);
    }


    @Override
    public void clearAllUIAfterSubmitSuccess() {

    }
}
