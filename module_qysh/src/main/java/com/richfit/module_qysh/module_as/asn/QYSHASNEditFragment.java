package com.richfit.module_qysh.module_as.asn;

import android.view.View;

import com.richfit.module_qysh.R;
import com.richfit.sdk_wzrk.base_asn_edit.BaseASNEditFragment;
import com.richfit.sdk_wzrk.base_asn_edit.imp.ASNEditPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHASNEditFragment extends BaseASNEditFragment<ASNEditPresenterImp> {
    @Override
    protected void initPresenter() {
        mPresenter = new ASNEditPresenterImp(mActivity);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    public void initView() {
        super.initView();
        View batchFlag = mView.findViewById(R.id.ll_batch_flag);
        if(batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
    }
}
