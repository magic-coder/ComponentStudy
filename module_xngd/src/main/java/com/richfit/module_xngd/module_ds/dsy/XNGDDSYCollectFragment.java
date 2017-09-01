package com.richfit.module_xngd.module_ds.dsy;


import android.text.TextUtils;

import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.domain.bean.InventoryQueryParam;
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

    @Override
    public void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected int getOrgFlag() {
        return getInteger(R.integer.orgNorm);
    }

    /**
     * 这里需要重写获取库存的参数，因为抬头选择是否应急，获取库存必须考虑
     * @return
     */
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
