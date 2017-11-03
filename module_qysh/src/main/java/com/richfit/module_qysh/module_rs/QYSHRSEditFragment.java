package com.richfit.module_qysh.module_rs;

import android.view.View;
import android.widget.TextView;

import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHRSEditFragment extends BaseASEditFragment<ASEditPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    protected void initView() {
        super.initView();
        View batchFlag = mView.findViewById(R.id.ll_batch_flag);
        if (batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
    }
}
