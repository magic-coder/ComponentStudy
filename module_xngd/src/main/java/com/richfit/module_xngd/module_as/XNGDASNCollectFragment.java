package com.richfit.module_xngd.module_as;

import android.text.TextUtils;
import android.widget.EditText;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzrk.base_asn_collect.BaseASNCollectFragment;
import com.richfit.sdk_wzrk.base_asn_collect.imp.ASNCollectPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/6/23.
 */

public class XNGDASNCollectFragment extends BaseASNCollectFragment<ASNCollectPresenterImp> {

    EditText etMoney;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_asn_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        etMoney = (EditText) mView.findViewById(R.id.xngd_et_money);
    }

    @Override
    public void initData() {

    }


    @Override
    public void initDataLazily() {
        etMaterialNum.setEnabled(false);
        if (mRefData == null) {
            showMessage("请先在抬头界面输入必要的数据");
            return;
        }
        super.initDataLazily();
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (!isNLocation && TextUtils.isEmpty(getString(etLocation))) {
            showMessage("请先输入上架仓位");
            return false;
        }
        if (!isNLocation && getString(etLocation).split("\\.").length != 4) {
            showMessage("您输入的仓位格式不正确");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
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

    @Override
    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etMoney);
    }
}
