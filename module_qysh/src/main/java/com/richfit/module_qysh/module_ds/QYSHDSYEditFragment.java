package com.richfit.module_qysh.module_ds;

import android.view.View;
import android.widget.TextView;

import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHDSYEditFragment extends BaseDSEditFragment<DSEditPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new DSEditPresenterImp(mActivity);
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
