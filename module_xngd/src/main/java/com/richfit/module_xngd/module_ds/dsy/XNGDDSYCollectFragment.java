package com.richfit.module_xngd.module_ds.dsy;


import android.text.TextUtils;
import android.widget.EditText;

import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by monday on 2017/1/20.
 */

public class XNGDDSYCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {

    EditText etRemark;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_ds_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        etRemark = mView.findViewById(R.id.et_remark);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getOrgFlag() {
        return getInteger(R.integer.orgNorm);
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.invFlag = mRefData.invFlag;
        result.projectNum = mRefData.projectNum;
        result.remark = getString(etRemark);
        return result;
    }

    /**
     * 这里需要重写获取库存的参数，因为抬头选择是否应急，获取库存必须考虑
     * @return
     */
    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = TextUtils.isEmpty( mRefData.invType) ? "1" :  mRefData.invType;
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", mRefData.invFlag);
       // extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum",mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }

    @Override
    public void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etRemark);
    }
}
