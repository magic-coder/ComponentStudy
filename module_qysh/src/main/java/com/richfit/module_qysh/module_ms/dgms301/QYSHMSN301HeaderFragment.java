package com.richfit.module_qysh.module_ms.dgms301;


import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.data.constant.Global;
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
    protected void initEvent() {
        //过账日期
        etTransferDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etTransferDate, Global.GLOBAL_DATE_PATTERN_TYPE1));

        //发出工厂
        RxAdapterView.itemSelections(spSendWork)
                .filter(position -> position.intValue() > 0)
                .subscribe(position -> mPresenter.getSendInvsByWorkId(mSendWorks.get(position.intValue()).workId, getOrgFlag()));

        //接收工厂
        RxAdapterView.itemSelections(spRecWork)
                .filter(position -> position.intValue() > 0)
                .filter(position -> position.intValue() > 0)
                .subscribe(position -> mPresenter.getRecInvsByWorkId( mRecWorks.get(position.intValue()).workId, getOrgFlag()));
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

}
