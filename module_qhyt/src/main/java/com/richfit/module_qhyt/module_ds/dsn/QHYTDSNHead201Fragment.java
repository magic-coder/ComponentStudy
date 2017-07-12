package com.richfit.module_qhyt.module_ds.dsn;

import android.widget.EditText;

import com.richfit.module_qhyt.R;
import com.richfit.module_qhyt.module_ds.dsn.imp.QHYTDSNHeadPresenterImp;
import com.richfit.sdk_wzck.base_dsn_head.BaseDSNHeadFragment;

/**
 * 201出库。显示成本中心
 * Created by monday on 2017/7/12.
 */

public class QHYTDSNHead201Fragment extends BaseDSNHeadFragment<QHYTDSNHeadPresenterImp> {

    EditText etAuxiliary9;
    EditText etAccountType;
    EditText etAccountContent;

    @Override
    public int getContentId() {
        return R.layout.qhyt_fragment_dsn201_head;
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }


    @Override
    public void initPresenter() {
        mPresenter = new QHYTDSNHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    protected void initView() {
        etAuxiliary9 = (EditText) mView.findViewById(R.id.qhyt_et_zzzdy9);
        etAccountType = (EditText) mView.findViewById(R.id.qhyt_et_zzzxlb);
        etAccountContent = (EditText) mView.findViewById(R.id.qhyt_et_zzzxnr);
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            mRefData.zzzdy9 = getString(etAuxiliary9);
            mRefData.zzzxlb = getString(etAccountType);
            mRefData.zzzxnr = getString(etAccountContent);
            mRefData.costCenter = getString(etAutoComp).split("_")[0];
        }
    }
}
