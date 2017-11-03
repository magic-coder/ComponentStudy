package com.richfit.module_qysh.module_ms.ms311;

import android.text.TextUtils;
import android.view.View;

import com.richfit.module_qysh.R;
import com.richfit.sdk_wzyk.base_msn_collect.BaseMSNCollectFragment;
import com.richfit.sdk_wzyk.base_msn_collect.imp.MSNCollectPresenterImp;

/**
 * Created by monday on 2017/11/3.
 */

public class QYSHMS311CollectFragment extends BaseMSNCollectFragment<MSNCollectPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new MSNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        super.initView();
        //隐藏发出批次
        View batchFlag = mView.findViewById(R.id.ll_batch_flag);
        if (batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
        //隐藏接收批次
        llRecBatch.setVisibility(View.GONE);
    }

    @Override
    protected boolean checkHeaderData() {
        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先选择工厂");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.recInvId)) {
            showMessage("请先选择接收库位");
            return false;
        }
        return true;
    }

    @Override
    protected boolean getWMOpenFlag() {
        return false;
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
