package com.richfit.module_qysh.module_as.asn;

import android.view.View;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzrk.base_asn_collect.BaseASNCollectFragment;
import com.richfit.sdk_wzrk.base_asn_collect.imp.ASNCollectPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHASNCollectFragment extends BaseASNCollectFragment<ASNCollectPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new ASNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }
    @Override
    public void initView() {
        super.initView();
        View batchFlag = mView.findViewById(R.id.ll_batch_flag);
        if(batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.batchFlag = null;
        return result;
    }
}
