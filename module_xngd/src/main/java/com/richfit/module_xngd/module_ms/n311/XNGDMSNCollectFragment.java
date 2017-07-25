package com.richfit.module_xngd.module_ms.n311;

import android.text.TextUtils;

import com.richfit.domain.bean.InventoryQueryParam;
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

    }

    @Override
    public void initData() {

    }

    @Override
    protected boolean checkHeaderData() {
        if("1".equals(mRefData.invType) && TextUtils.isEmpty(mRefData.projectNum)) {
            //选择库存类型为项目物资的时候，项目编号必输
            showMessage("请先在抬头输入项目编号");
            return false;
        }
        return true;
    }

    /**
     * 这里默认发出仓位和接收仓位一致
     * @param position
     */
    protected void loadLocationQuantity(int position) {
        super.loadLocationQuantity(position);
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
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = "1";
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", mRefData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum",mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }
}
