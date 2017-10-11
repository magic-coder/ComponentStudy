package com.richfit.module_xngd.module_rs;

import android.text.TextUtils;

import com.richfit.common_lib.utils.UiUtil;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * 退库申请单退库，库存地点不让修改---张颖
 * Created by monday on 2017/6/21.
 */

public class XNGDRSCollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

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
    public void loadInvComplete() {
        super.loadInvComplete();
        spInv.setEnabled(false);
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.invFlag = mRefData.invFlag;
        result.projectNum = mRefData.projectNum;
        //从单据行里面取出库存标识
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        result.specialInvFlag = lineData.specialInvFlag;
        result.specialInvNum = lineData.specialInvNum;
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
}
