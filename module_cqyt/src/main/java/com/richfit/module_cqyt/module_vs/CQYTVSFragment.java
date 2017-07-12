package com.richfit.module_cqyt.module_vs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadFragment;
import com.richfit.common_lib.lib_rv_animation.Animation.animators.FadeInDownAnimator;
import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.adapter.CQYTVSADetailAdapter;

import java.util.List;

/**
 * Created by monday on 2017/7/3.
 */

public class CQYTVSFragment extends BaseHeadFragment<CQYTPresenterImp>
        implements CQYTVSContract.View {

    RichEditText etRefNum;
    RecyclerView mRecyclerView;
    MultiItemTypeTreeAdapter<RefDetailEntity> mAdapter;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        if (list != null && list.length == 1) {
            getRefData(list[0]);
        }
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        mRefData = null;
    }

    @Override
    protected int getContentId() {
        return R.layout.cqyt_fragmnent_vs_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new CQYTPresenterImp(mActivity);
    }


    @Override
    protected void initView() {
        etRefNum = (RichEditText) mView.findViewById(R.id.et_ref_num);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.base_detail_recycler_view);
        LinearLayoutManager lm = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setItemAnimator(new FadeInDownAnimator());
    }

    @Override
    public void initEvent() {
        etRefNum.setOnRichEditTouchListener((view, refNum) -> {
            hideKeyboard(view);
            getRefData(refNum);
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initDataLazily() {

    }

    protected void getRefData(String refNum) {
        clearAllUI();
        mRefData = null;
        mPresenter.getReference(refNum, mRefType, mBizType, "3", "", Global.USER_ID);
    }

    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        if (mAdapter == null) {
            mAdapter = new CQYTVSADetailAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.removeAllVisibleNodes();
            mAdapter.addAll(allNodes);
        }
    }

    @Override
    public void showDataFail(String message) {
        showMessage(message);
    }

    @Override
    public void clearAllUI() {
        clearCommonUI(etRefNum);
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {

    }

}
