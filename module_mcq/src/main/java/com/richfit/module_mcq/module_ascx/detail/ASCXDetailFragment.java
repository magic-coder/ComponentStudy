package com.richfit.module_mcq.module_ascx.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.richfit.common_lib.lib_adapter_rv.MultiItemTypeAdapter;
import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailFragment;
import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.module_mcq.R;
import com.richfit.module_mcq.adapter.ASCXAdapter;

import java.util.List;

/**
 * Created by monday on 2017/8/28.
 */

public class ASCXDetailFragment extends BaseDetailFragment<ASCXDetailPresenterImp, RefDetailEntity>
        implements IASCXDetailView, MultiItemTypeAdapter.OnItemClickListener {

    @Override
    protected int getContentId() {
        return R.layout.mcq_fragment_ascx_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASCXDetailPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }


    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initDataLazily() {
        if (mRefData == null) {
            showMessage("请现在抬头界面获取单据数据,并选择合适的单据");
            return;
        }

        if (TextUtils.isEmpty(mRefData.recordNum)) {
            showMessage("请现在抬头界面获取单据数据,并选择合适的单据");
            return;
        }

        if (TextUtils.isEmpty(mRefData.bizType)) {
            showMessage("未获取到业务类型");
            return;
        }

        if (TextUtils.isEmpty(mRefData.refType)) {
            showMessage("未获取到单据类型");
            return;
        }
        //开始刷新
        startAutoRefresh();
    }

    //重写该方法的目的是，我们不在获取整单缓存，转而获取该单据的明细数据
    @Override
    public void onRefresh() {
        //单据抬头id
        final String refNum = mRefData.recordNum;
        //业务类型
        final String bizType = mRefData.bizType;
        //单据类型
        final String refType = mRefData.refType;
        //移动类型
        final String moveType = mRefData.moveType;
        mPresenter.getReference(refNum, refType, bizType, moveType, "", Global.USER_ID);
    }

    @Override
    public void getReferenceSuccess(ReferenceEntity refData) {
        List<RefDetailEntity> allNodes = refData.billDetailList;
        ASCXAdapter adapter = new ASCXAdapter(mActivity, R.layout.mcq_item_ascx_head, allNodes);
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void deleteNode(RefDetailEntity node, int position) {

    }

    @Override
    public void editNode(RefDetailEntity node, int position) {

    }

    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {

    }

    @Override
    public void refreshComplete() {

    }

    @Override
    public void deleteNodeSuccess(int position) {

    }

    @Override
    public void submitBarcodeSystemSuccess() {

    }

    @Override
    public void submitSAPSuccess() {

    }

    @Override
    public void upAndDownLocationSuccess() {

    }


    @Override
    protected boolean checkTransStateBeforeRefresh() {
        return true;
    }

    @Override
    protected void submit2BarcodeSystem(String tranToSapFlag) {

    }

    @Override
    protected void submit2SAP(String tranToSapFlag) {

    }

    @Override
    protected void sapUpAndDownLocation(String transToSapFlag) {

    }


    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        Log.e("yff", "点击");
        //点击跳转到主页面，并且实现上架功能跳转
        Intent intent = new Intent(getContext(), mActivity.getClass());
        Bundle bundle = new Bundle();
        bundle.putString(Global.EXTRA_COMPANY_CODE_KEY, Global.COMPANY_CODE);
        bundle.putString(Global.EXTRA_MODULE_CODE_KEY, "");
        bundle.putString(Global.EXTRA_BIZ_TYPE_KEY, "113");
        bundle.putString(Global.EXTRA_REF_TYPE_KEY, "16");
        bundle.putString(Global.EXTRA_CAPTION_KEY, "物资上架");
        bundle.putInt(Global.EXTRA_MODE_KEY, Global.ONLINE_MODE);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }
}
