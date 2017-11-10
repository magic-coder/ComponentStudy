package com.richfit.module_qysh.module_ms.ms301;

import android.view.View;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzyk.base_ms_edit.BaseMSEditFragment;
import com.richfit.sdk_wzyk.base_ms_edit.imp.MSEditPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHMSY301EditFragment extends BaseMSEditFragment<MSEditPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new MSEditPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        super.initView();
        //打开接收仓位
        llRecLocation.setVisibility(View.VISIBLE);
        //隐藏发出批次
        View batchFlag = mView.findViewById(R.id.ll_batch_flag);
        if (batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.batchFlag = null;
        result.recBatchFlag = null;
        return result;
    }
}
