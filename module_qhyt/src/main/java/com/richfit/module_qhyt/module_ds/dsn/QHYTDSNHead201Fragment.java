package com.richfit.module_qhyt.module_ds.dsn;

import android.text.TextUtils;
import android.widget.EditText;

import com.richfit.data.constant.Global;
import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzck.base_dsn_head.BaseDSNHeadFragment;
import com.richfit.sdk_wzck.base_dsn_head.imp.DSNHeadPresenterImp;

/**
 * 201出库。显示成本中心
 * Created by monday on 2017/7/12.
 */

public class QHYTDSNHead201Fragment extends BaseDSNHeadFragment<DSNHeadPresenterImp> {

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
    protected String getAutoComDataType() {
        return Global.COST_CENTER_DATA;
    }


    @Override
    public void initPresenter() {
        mPresenter = new DSNHeadPresenterImp(mActivity);
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
            String costCenter = getString(etAutoComp);
            if (!TextUtils.isEmpty(costCenter)) {
                mRefData.costCenter = costCenter.split("_")[0];
            }
        }
    }
}
