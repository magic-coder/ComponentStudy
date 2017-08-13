package com.richfit.sdk_xxcx.inventory_query_y.head;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_xxcx.R;
import com.richfit.sdk_xxcx.R2;
import com.richfit.sdk_xxcx.adapter.InvYQueryRefDetailAdapter;
import com.richfit.sdk_xxcx.inventory_query_y.head.imp.InvYQueryHeadPresenterImp;

import butterknife.BindView;

/**
 * Created by monday on 2017/5/25.
 */

public class InvYQueryHeadFragment extends BaseFragment<InvYQueryHeadPresenterImp>
        implements IInvYQueryHeadView {

    private static final String MOVE_TYPE = "1";

    @BindView(R2.id.et_ref_num)
    RichEditText etRefNum;
    //单据明细
    @BindView(R2.id.base_detail_recycler_view)
    RecyclerView mRecyclerView;
    InvYQueryRefDetailAdapter mAdapter;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        if (list != null && list.length == 1) {
            getRefData(list[0]);
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.xxcx_fragment_invy_query_header;
    }

    @Override
    public void initPresenter() {
        mPresenter = new InvYQueryHeadPresenterImp(mActivity);
    }


    @Override
    public void initVariable(Bundle savedInstanceState) {
        mRefData = null;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initDataLazily() {

    }

    protected void getRefData(String refNum) {
        mRefData = null;
        clearAllUI();
        mPresenter.getReference(refNum, mRefType, mBizType, MOVE_TYPE, "", Global.USER_ID);
    }

    @Override
    public void getReferenceSuccess(ReferenceEntity refData) {
        refData.bizType = mBizType;
        refData.moveType = MOVE_TYPE;
        refData.refType = mRefType;
        mRefData = refData;
    }

    @Override
    public void getReferenceFail(String message) {
        showMessage(message);
        mRefData = null;
        //清除所有控件绑定的数据
        clearAllUI();
    }

    @Override
    public void getReferenceComplete() {
        if (mAdapter != null) {
            mAdapter = new InvYQueryRefDetailAdapter(mActivity, R.layout.xxcx_item_invy_query_ref,
                    mRefData.billDetailList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void clearAllUI() {
        clearCommonUI(etRefNum);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
    }

    @Override
    public void retry(String action) {
        switch (action) {
            case Global.RETRY_LOAD_REFERENCE_ACTION:
                mPresenter.getReference(getString(etRefNum), mRefType, mBizType, MOVE_TYPE, "", Global.LOGIN_ID);
                break;
        }
        super.retry(action);
    }

}
