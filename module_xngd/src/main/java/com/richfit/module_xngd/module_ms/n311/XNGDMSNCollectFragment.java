package com.richfit.module_xngd.module_ms.n311;

import android.text.TextUtils;
import android.util.Log;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzyk.base_msn_collect.BaseMSNCollectFragment;
import com.richfit.sdk_wzyk.base_msn_collect.imp.MSNCollectPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/7/20.
 */

public class XNGDMSNCollectFragment extends BaseMSNCollectFragment<MSNCollectPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new MSNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        //接收批次为空
        etRecBatchFlag.setEnabled(false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean checkHeaderData() {
        if ("0".equals(mRefData.invType) && "1".equals(mRefData.invType)) {
            showMessage("项目移交物资不合理");
            return false;
        }

        //如果用户选择的是项目物资，那么必须检查编号必输
        if (!TextUtils.isEmpty(mRefData.specialInvFlag) &&
                "Q".equals(mRefData.specialInvFlag) && TextUtils.isEmpty(mRefData.projectNum)) {
            showMessage("请先在抬头输入项目编号");
            return false;
        }
        return true;
    }

    /**
     * 这里默认发出仓位和接收仓位一致
     *
     * @param position
     */
    protected void getTransferSingle(int position) {
        super.getTransferSingle(position);
        autoRecLoc.setText(mInventoryDatas.get(position).location);
    }

    @Override
    protected boolean getWMOpenFlag() {
        return false;
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        //注意这里由于抬头选择了specialInvFlag，那么所获取的库存都是该值
        result.specialInvFlag = mRefData.specialInvFlag;
        result.specialInvNum = mRefData.projectNum;
        result.invFlag = mRefData.invFlag;
        result.projectNum = mRefData.projectNum;
        return  result;
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
