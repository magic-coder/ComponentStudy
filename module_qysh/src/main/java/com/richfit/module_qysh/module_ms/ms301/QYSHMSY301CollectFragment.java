package com.richfit.module_qysh.module_ms.ms301;

import android.view.View;

import com.richfit.module_qysh.R;
import com.richfit.sdk_wzyk.base_ms_collect.BaseMSCollectFragment;
import com.richfit.sdk_wzyk.base_ms_collect.imp.MSCollectPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHMSY301CollectFragment extends BaseMSCollectFragment<MSCollectPresenterImp>{

    @Override
    protected void initPresenter() {
        mPresenter = new MSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

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
    protected int getOrgFlag() {
        return 0;
    }
}
