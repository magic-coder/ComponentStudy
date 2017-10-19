package com.richfit.module_xngd.module_rs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.common_lib.utils.ArithUtil;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by monday on 2017/6/21.
 */

public class XNGDRSEditFragment extends BaseASEditFragment<ASEditPresenterImp> {

    public static final String EXTRA_MONEY_KEY = "extra_money";
    public static final String EXTRA_TOTAL_MONEY_KEY = "extra_total_money";

    EditText etMoney;
    TextView tvTotalMoney;
    float mTotalMoney;
    String mMoney;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_rs_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        tvActQuantityName.setText("应退数量");
        tvQuantityName.setText("实退数量");
        etMoney = mView.findViewById(R.id.xngd_et_money);
        tvTotalMoney = mView.findViewById(R.id.xngd_tv_total_money);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mMoney = bundle.getString(EXTRA_MONEY_KEY);
            String totalMoney = bundle.getString(EXTRA_TOTAL_MONEY_KEY);
            etMoney.setText(mMoney);
            tvTotalMoney.setText(totalMoney);
        }
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (!super.checkCollectedDataBeforeSave())
            return false;

        if(TextUtils.isEmpty(getString(etMoney))) {
            showMessage("请输入金额");
            return false;
        }

        float totalMoneyV = CommonUtil.convertToFloat(getString(tvTotalMoney), 0.0f);
        float collectedMoney = CommonUtil.convertToFloat(mMoney, 0.0f);
        float moneyV = CommonUtil.convertToFloat(getString(etMoney), 0.0f);
        float residualQuantity = totalMoneyV - collectedMoney + moneyV;//减去已经录入的数量
        mMoney = moneyV + "";
        mTotalMoney = residualQuantity;
        return true;
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.invFlag = mRefData.invFlag;
        result.projectNum = mRefData.projectNum;
        RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        result.specialInvFlag = lineData.specialInvFlag;
        result.specialInvNum = lineData.specialInvNum;
        result.money = getString(etMoney);
        return result;
    }

    @Override
    public void saveEditedDataSuccess(String message) {
        super.saveEditedDataSuccess(message);
        tvTotalMoney.setText(String.valueOf(mTotalMoney));
    }


    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        param.queryType = "03";
        param.invType = TextUtils.isEmpty(mRefData.invType) ? "1" : mRefData.invType;
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", lineData.invFlag);
        extraMap.put("specialInvFlag", lineData.specialInvFlag);
        param.extraMap = extraMap;
        return param;
    }

}
