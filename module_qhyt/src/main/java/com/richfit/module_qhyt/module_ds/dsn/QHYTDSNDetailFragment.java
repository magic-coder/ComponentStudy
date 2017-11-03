package com.richfit.module_qhyt.module_ds.dsn;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzck.base_dsn_detail.BaseDSNDetailFragment;
import com.richfit.sdk_wzck.base_dsn_detail.imp.DSNDetailPresenterImp;

/**
 * Created by monday on 2017/3/27.
 */

public class QHYTDSNDetailFragment extends BaseDSNDetailFragment<DSNDetailPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new DSNDetailPresenterImp(mActivity);
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
            showMessage("请先在抬头界面选择工厂");
            return;
        }
        if ("26".equals(mBizType) && TextUtils.isEmpty(mRefData.costCenter)) {
            showMessage("请先在抬头界面输入成本中心");
            return;
        }
        if ("27".equals(mBizType) && TextUtils.isEmpty(mRefData.projectNum)) {
            showMessage("请现在抬头界面输入项目编号");
            return;
        }
        super.initDataLazily();
    }

    @Override
    protected void submit2BarcodeSystem(String tranToSapFlag) {
        //如果需要寄售转自有但是没有成功过，都需要用户需要再次寄售转自有
        if (isNeedTurn && !isTurnSuccess) {
            startTurnOwnSupplies("07");
            return;
        }
        String transferFlag = (String) SPrefUtil.getData(mBizType, "0");
        if ("1".equals(transferFlag)) {
            showMessage(getString(R.string.msg_detail_off_location));
            return;
        }
        mExtraTansMap.clear();
        mExtraTansMap.put("costCenter", mRefData.costCenter);
        mExtraTansMap.put("projectNum", mRefData.projectNum);
        mExtraTansMap.put("zzzdy9",mRefData.zzzdy9);
        mExtraTansMap.put("zzzxlb",mRefData.zzzxlb);
        mExtraTansMap.put("zzzxnr",mRefData.zzzxnr);
        mPresenter.submitData2BarcodeSystem("",mTransId, mRefData.bizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, tranToSapFlag, mExtraTansMap);
    }

    @Override
    protected String getSubFunName() {
        return "无仓考出库";
    }
}
