package com.richfit.module_qhyt.module_ms.n311;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzyk.base_msn_head.BaseMSNHeadFragment;
import com.richfit.sdk_wzyk.base_msn_head.imp.MSNHeadPresenterImp;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * 青海311无参考移库抬头界面
 * Created by monday on 2017/2/16.
 */

public class QHYTMSN311HeadFragment extends BaseMSNHeadFragment<MSNHeadPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new MSNHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        //工厂内没有接收工厂，并且将发出工厂修改为工厂
        llRecWork.setVisibility(View.GONE);
        tvSendWorkName.setText("工厂");
    }

    @Override
    public void initEvent() {
        //过账日期
        etTransferDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etTransferDate, Global.GLOBAL_DATE_PATTERN_TYPE1));

        //发出工厂
        RxAdapterView.itemSelections(spSendWork)
                .filter(position -> position.intValue() > 0)
                .subscribe(position -> mPresenter.getSendInvsByWorkId(mSendWorks.get(position.intValue()).workId, getOrgFlag()));
    }

    @Override
    public void initDataLazily() {

    }

    /**
     * 发出库位列表
     *
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

    @Override
    public void loadSendInvsComplete() {
        if (mUploadMsgEntity != null && !TextUtils.isEmpty(mUploadMsgEntity.invId)) {
            Log.d("yff","invId = " + mUploadMsgEntity.invId);
            Flowable.just(mSendInvs)
                    .map(list -> {
                        int pos = -1;
                        for (InvEntity item : list) {
                            ++pos;
                            //注意请选择没有id
                            if (mUploadMsgEntity.invId.equalsIgnoreCase(item.invId))
                                return pos;
                        }
                        return pos;
                    })
                    .filter(pos -> pos.intValue() >= 0 && pos.intValue() < mSendInvs.size())
                    .compose(TransformerHelper.io2main())
                    .subscribe(pos -> spSendInv.setSelection(pos.intValue()), e -> Log.d("yff",e.getMessage()), () -> lockUIUnderEditState(spSendInv));
        }


        if (mUploadMsgEntity != null && !TextUtils.isEmpty(mUploadMsgEntity.recInvId)) {
            Log.d("yff","recInvId = " + mUploadMsgEntity.recInvId);
            Flowable.just(mRecInvs)
                    .map(list -> {
                        int pos = -1;
                        for (InvEntity item : list) {
                            ++pos;
                            //注意请选择没有id
                            if (mUploadMsgEntity.recInvId.equalsIgnoreCase(item.invId))
                                return pos;
                        }
                        return pos;
                    })
                    .filter(pos -> pos.intValue() >= 0 && pos.intValue() < mRecInvs.size())
                    .compose(TransformerHelper.io2main())
                    .subscribe(pos -> spRecInv.setSelection(pos.intValue()), e -> Log.d("yff",e.getMessage()), () -> lockUIUnderEditState(spRecInv));
        }
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
    public void operationOnHeader(final String companyCode) {
        if (mLocalHeaderResult == null) {
            mLocalHeaderResult = new ResultEntity();
        }
        mLocalHeaderResult.voucherDate = getString(etTransferDate);
        mLocalHeaderResult.transId = mUploadMsgEntity.transId;
        mLocalHeaderResult.businessType = mBizType;
        super.operationOnHeader(companyCode);
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
        return "5";
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {

    }
}
