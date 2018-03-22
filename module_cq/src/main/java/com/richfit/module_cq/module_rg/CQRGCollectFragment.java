package com.richfit.module_cq.module_rg;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

/**
 * Created by monday on 2017/12/5.
 */

public class CQRGCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp>{

    @Override
    protected void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        tvQuantityName.setText("实退数量");
        tvActQuantityName.setText("应退数量");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }


    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.queryType = "01";
        return queryParam;
    }
}
