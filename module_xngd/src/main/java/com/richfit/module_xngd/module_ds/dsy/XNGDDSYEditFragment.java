package com.richfit.module_xngd.module_ds.dsy;


import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.richfit.data.constant.Global;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/1/20.
 */

public class XNGDDSYEditFragment extends BaseDSEditFragment<DSEditPresenterImp> {

    EditText etRemark;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_ds_edit;
    }


    @Override
    public void initPresenter() {
        mPresenter = new DSEditPresenterImp(mActivity);
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
        result.invFlag = mRefData.invFlag;
        result.projectNum = mRefData.projectNum;
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
        //extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum", mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }
}
