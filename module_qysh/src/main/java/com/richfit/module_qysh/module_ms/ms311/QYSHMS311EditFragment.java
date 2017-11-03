package com.richfit.module_qysh.module_ms.ms311;

import android.view.View;

import com.richfit.module_qysh.R;
import com.richfit.sdk_wzyk.base_ms_edit.BaseMSEditFragment;
import com.richfit.sdk_wzyk.base_msn_edit.BaseMSNEditFragment;
import com.richfit.sdk_wzyk.base_msn_edit.imp.MSNEditPresenterImp;

/**
 * Created by monday on 2017/11/3.
 */

public class QYSHMS311EditFragment extends BaseMSNEditFragment<MSNEditPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new MSNEditPresenterImp(mActivity);
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
        llRecBatchFlag.setVisibility(View.GONE);
    }

    @Override
    protected void initDataLazily() {

    }
}
