package com.richfit.module_xngd.module_rs;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/6/21.
 */

public class XNGDRSCollectFragment extends BaseASCollectFragment<ASCollectPresenterImp>{
    @Override
    public void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        tvActQuantityName.setText("应退数量");
        tvQuantityName.setText("实退数量");
    }

    @Override
    public void initEvent() {
        super.initEvent();
        etLocation.setOnRichAutoEditTouchListener((view, location) -> getTransferSingle(getString(etBatchFlag), location));
    }

    @Override
    public void initData() {

    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        param.queryType = "03";
        param.invType = "1";
        Map<String,Object> extraMap = new HashMap<>();
        extraMap.put("invFlag",lineData.invFlag);
        extraMap.put("specialInvFlag",lineData.specialInvFlag);
        param.extraMap = extraMap;
        return param;
    }
}
