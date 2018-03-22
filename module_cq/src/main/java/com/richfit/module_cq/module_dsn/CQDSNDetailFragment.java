package com.richfit.module_cq.module_dsn;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.sdk_wzck.base_dsn_detail.BaseDSNDetailFragment;
import com.richfit.sdk_wzck.base_dsn_detail.imp.DSNDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/12/8.
 */

public class CQDSNDetailFragment extends BaseDSNDetailFragment<DSNDetailPresenterImp>{
    @Override
    protected void initPresenter() {
        mPresenter = new DSNDetailPresenterImp(mActivity);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected String getSubFunName() {
        return "无参考出库";
    }

    @Override
    public void submitBarcodeSystemSuccess() {
        setRefreshing(false, "过账成功");
        showSuccessDialog(mShowMsg);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        //注意这里必须清除单据数据
        mRefData = null;
        mShowMsg.setLength(0);
        mTransId = "";
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "01";
        return menus.subList(0,1);
    }
}
