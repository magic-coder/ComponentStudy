package com.richfit.module_xngd.module_ds.dsn;


import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.richfit.data.constant.Global;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzck.base_dsn_edit.BaseDSNEditFragment;
import com.richfit.sdk_wzck.base_dsn_edit.imp.DSNEditPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/3/27.
 */

public class XNGDDSNEditFragment extends BaseDSNEditFragment<DSNEditPresenterImp> {


    EditText etRemark;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_dsn_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSNEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        etRemark = mView.findViewById(R.id.et_remark);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        String remark = bundle.getString(Global.EXTRA_REMARK_KEY);
        etRemark.setText(remark);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.glAccount = mRefData.glAccount;
        result.invFlag = mRefData.invFlag;
        result.projectNum = mRefData.projectNum;
        result.orderNum = mRefData.orderNum;
        result.remark = getString(etRemark);
        return result;
    }

    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = TextUtils.isEmpty(mRefData.invType) ? "1" : mRefData.invType;
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", mRefData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum", mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }
}
