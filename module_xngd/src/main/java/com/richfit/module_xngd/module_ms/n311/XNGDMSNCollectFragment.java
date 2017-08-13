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
        //接收批次为空
        etRecBatchFlag.setEnabled(false);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initDataLazily() {

        if (mRefData == null) {
            showMessage("请现在抬头界面选择合适的参数");
            return;
        }

        if ("0".equals(mRefData.invType) && "1".equals(mRefData.invType)) {
            showMessage("项目移交物资不合理");
            return;
        }

        //如果用户选择的是项目物资，那么必须检查编号必输
        if ("Q".equals(mRefData.specialInvFlag) && TextUtils.isEmpty(mRefData.projectNum)) {
            showMessage("请先在抬头输入项目编号");
            return;
        }

        super.initDataLazily();
    }

    @Override
    protected boolean checkHeaderData() {
        if ("1".equals(mRefData.invType) && TextUtils.isEmpty(mRefData.projectNum)) {
            //选择库存类型为项目物资的时候，项目编号必输
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
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = mRefData.invType;
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", mRefData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum", mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }
}
