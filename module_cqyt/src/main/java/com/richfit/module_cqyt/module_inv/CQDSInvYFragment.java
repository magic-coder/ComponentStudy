package com.richfit.module_cqyt.module_inv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.lib_rv_animation.Animation.animators.FadeInDownAnimator;
import com.richfit.common_lib.lib_tree_rv.RecycleTreeViewHelper;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.adapter.InvyAdapter;
import com.richfit.sdk_xxcx.R2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 物资出库库存查询
 * Created by monday on 2018/3/19.
 */

public class CQDSInvYFragment extends BaseFragment<CQInvYPresenterImpl>
        implements ICQDSInvYView {

    private static final String MOVE_TYPE = "2";

    @BindView(R2.id.base_detail_recycler_view)
    RecyclerView mRecyclerView;
    List<RefDetailEntity> mDatas;

    @Override
    protected int getContentId() {
        return R.layout.cqyt_fragment_invy;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new CQInvYPresenterImpl(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        if(mDatas == null) {
            mDatas = new ArrayList<>();
        }
    }

    @Override
    protected void initView() {
        LinearLayoutManager lm = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setItemAnimator(new FadeInDownAnimator());
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initDataLazily() {
        if(mRefData == null) {
            showMessage("请在抬头先采集数据");
            return;
        }
        if(TextUtils.isEmpty(mRefData.recordNum)) {
            showMessage("未获取到单据号");
            return;
        }
        mDatas.clear();
        getRefData(mRefData.recordNum);
    }

    private void getRefData(String refNum) {
        mPresenter.getReference(refNum, mRefType, mBizType, MOVE_TYPE, "", Global.USER_ID);
    }

    @Override
    public void getReferenceSuccess(List<RefDetailEntity> datas) {
        mDatas.addAll(datas);
    }



    @Override
    public void loadReferenceComplete() {
        ArrayList<RefDetailEntity> allNodes = RecycleTreeViewHelper.getSortedNodes(mDatas, 1);
        InvyAdapter adapter = new InvyAdapter(mActivity, allNodes);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void getReferenceFail(String message) {
        showMessage(message);
    }

}
