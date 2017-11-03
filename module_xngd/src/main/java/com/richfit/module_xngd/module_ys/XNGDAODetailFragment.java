package com.richfit.module_xngd.module_ys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_xngd.R;
import com.richfit.module_xngd.adapter.XNGDAODetailAdapter;
import com.richfit.module_xngd.module_ys.detail.XNGDAODetailPresenterImp;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/5/26.
 */

public class XNGDAODetailFragment extends BaseASDetailFragment<XNGDAODetailPresenterImp> {

    @Override
    protected int getContentId() {
        return R.layout.xngd_fragment_ao_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new XNGDAODetailPresenterImp(mActivity);
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
            showMessage("请现在抬头界面获取单据数据");
            return;
        }

        if (TextUtils.isEmpty(mRefData.recordNum)) {
            showMessage("请现在抬头界面输入验收单号");
            return;
        }

        if (TextUtils.isEmpty(mRefData.bizType)) {
            showMessage("未获取到业务类型");
            return;
        }
        startAutoRefresh();
    }


    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        if (mAdapter == null) {
            mAdapter = new XNGDAODetailAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    /**
     * 1.过账。过账前需要检查全检和抽检数量小于累计实收数量
     */
    @Override
    protected void submit2BarcodeSystem(String transToSapFlag) {
        if (mAdapter != null && XNGDAODetailAdapter.class.isInstance(mAdapter)) {
            XNGDAODetailAdapter xngdaoDetailAdapter = (XNGDAODetailAdapter) mAdapter;
            if (!xngdaoDetailAdapter.isTransferValid()) {
                showMessage("您采集的全检数量或抽检数量大于累计实收数量");
                return;
            }
        }
        String state = (String) SPrefUtil.getData(mBizType + mRefType, "0");
        if (!"0".equals(state)) {
            showMessage(getString(R.string.msg_detail_on_location));
            return;
        }
        mShowMsg.setLength(0);
        //这里需要refCodeId;
        mPresenter.transferCollectionData(mRefData.recordNum,mRefData.refCodeId,mTransId, mBizType,
                mRefType,mRefData.inspectionType, Global.USER_ID,false, mRefData.voucherDate, transToSapFlag, null);
    }

    /**
     * 第一步过账成功后直接跳转
     */
    @Override
    public void submitBarcodeSystemSuccess() {
        showSuccessDialog(mShowMsg);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        mRefData = null;
        mShowMsg.setLength(0);
        mTransId = "";
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }


    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        return tmp.subList(0, 1);
    }

    @Override
    protected String getSubFunName() {
        return "物资验收";
    }

}
