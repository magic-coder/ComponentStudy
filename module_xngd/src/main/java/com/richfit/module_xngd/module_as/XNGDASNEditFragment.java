package com.richfit.module_xngd.module_as;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzrk.base_asn_edit.BaseASNEditFragment;
import com.richfit.sdk_wzrk.base_asn_edit.imp.ASNEditPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/6/23.
 */

public class XNGDASNEditFragment extends BaseASNEditFragment<ASNEditPresenterImp> {

    public static final String EXTRA_MONEY_KEY = "extra_money";

    EditText etMoney;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_asn_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASNEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        etMoney = (EditText) mView.findViewById(R.id.xngd_et_money);
    }

    @Override
    public void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String money = arguments.getString(XNGDASNEditFragment.EXTRA_MONEY_KEY);
            etMoney.setText(money);
        }
        super.initData();
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.money = getString(etMoney);
        result.invFlag = mRefData.invFlag;
        result.specialInvFlag = mRefData.specialInvFlag;
        result.projectNum = mRefData.projectNum;
        return result;
    }

    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = TextUtils.isEmpty( mRefData.invType) ? "1" :  mRefData.invType;
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", mRefData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum", mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }

}
