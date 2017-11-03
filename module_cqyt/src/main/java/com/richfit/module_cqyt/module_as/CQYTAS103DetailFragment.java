package com.richfit.module_cqyt.module_as;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.adapter.CQYTAS103DetailAdapter;
import com.richfit.module_cqyt.module_as.imp.QHYTAS103DetailPresenterImp;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/7/4.
 */

public class CQYTAS103DetailFragment extends BaseASDetailFragment<QHYTAS103DetailPresenterImp> {


    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_as103_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new QHYTAS103DetailPresenterImp(mActivity);
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

        if (TextUtils.isEmpty(mRefData.deliveryOrder)) {
            showMessage("请现在抬头界面输入提货单");
            return;
        }

        super.initDataLazily();
    }

    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        if (mAdapter == null) {
            mAdapter = new CQYTAS103DetailAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    @Override
    protected void submit2BarcodeSystem(String transToSapFlag) {
        String state = (String) SPrefUtil.getData(mBizType + mRefType, "0");
        if (!"0".equals(state)) {
            showMessage(getString(R.string.msg_detail_on_location));
            return;
        }
        mShowMsg.setLength(0);
        mExtraTansMap.clear();
        mExtraTansMap.put("refNum", mRefData.recordNum);
		//到货日期s
		mExtraTansMap.put("arrivalDate",mRefData.arrivalDate);
        //报检日期
		mExtraTansMap.put("inspectionDate",mRefData.inspectionDate);
        //提货单
		mExtraTansMap.put("deliveryOrder",mRefData.deliveryOrder);
        //件数
		mExtraTansMap.put("quantityCustom",mRefData.quantityCustom);
        //报检单位
		mExtraTansMap.put("declaredUnit",mRefData.declaredUnit);
        // 班
		mExtraTansMap.put("team",mRefData.team);
        // 岗位
		mExtraTansMap.put("post",mRefData.post);
        // 生产厂家
		mExtraTansMap.put("manufacture",mRefData.manufacture);
        //检验单位
		mExtraTansMap.put("inspectionUnit",mRefData.inspectionUnit);
        // 备注
		mExtraTansMap.put("remark",mRefData.remark);
        // 检验标准及特殊要求
		mExtraTansMap.put("inspectionStandard",mRefData.inspectionStandard);
        //提货单
		mExtraTansMap.put("deliveryOrder",mRefData.deliveryOrder);
        mPresenter.submitData2BarcodeSystem(mRefData.refCodeId, mTransId, mBizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, mExtraTansMap);
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
    protected String getSubFunName() {
        return "103-采购入库";
    }


    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        tmp.get(0).transToSapFlag = "01";
        return tmp.subList(0, 1);
    }

}
