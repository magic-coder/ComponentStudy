package com.richfit.module_qhyt.module_rs.rsy;


import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

/**
 * Created by monday on 2017/2/27.
 */

public class QHYTRSYCollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {


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
    protected String getInvType() {
        return "1";
    }

    @Override
    protected String getInventoryQueryType() {
        return getString(R.string.inventoryQueryTypeSAPLocation);
    }
}
