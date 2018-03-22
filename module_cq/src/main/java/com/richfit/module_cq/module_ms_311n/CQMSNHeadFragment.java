package com.richfit.module_cq.module_ms_311n;

import android.text.TextUtils;
import android.view.View;

import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.domain.bean.InvEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzyk.base_msn_head.BaseMSNHeadFragment;
import com.richfit.sdk_wzyk.base_msn_head.imp.MSNHeadPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/12/5.
 */

public class CQMSNHeadFragment extends BaseMSNHeadFragment<MSNHeadPresenterImp>{

    @Override
    protected void initPresenter() {
        mPresenter = new MSNHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        //隐藏接收工厂
        llRecWork.setVisibility(View.GONE);
        //发出工厂->工厂
        tvSendWorkName.setText("工厂");
    }

    @Override
    protected void initDataLazily() {

    }

    /**
     * 发出库存地点
     * @param sendInvs
     */
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
        //同时初始化接收库位
        mRecInvs.clear();
        mRecInvs.addAll(sendInvs);
        if (mRecInvAdapter == null) {
            mRecInvAdapter = new InvAdapter(mActivity, R.layout.item_simple_sp, mRecInvs);
            spRecInv.setAdapter(mRecInvAdapter);
        } else {
            mRecInvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadSendInvsFail(String message) {
        showMessage(message);
    }
    /**
     * 加载发出库位完成
     */
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
    public void _onPause() {
        super._onPause();
        //工厂内移库，默认接收工厂默认等于接收工厂
        if (mRefData != null) {
            mRefData.recWorkName = mRefData.workName;
            mRefData.recWorkCode = mRefData.workCode;
            mRefData.recWorkId = mRefData.workId;
        }
    }

    @Override
    protected String getMoveType() {
        return "3";
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
