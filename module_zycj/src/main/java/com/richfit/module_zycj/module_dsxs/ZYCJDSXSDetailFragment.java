package com.richfit.module_zycj.module_dsxs;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.sdk_wzck.base_ds_detail.BaseDSDetailFragment;
import com.richfit.sdk_wzck.base_ds_detail.imp.DSDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2018/1/2.
 */

public class ZYCJDSXSDetailFragment extends BaseDSDetailFragment<DSDetailPresenterImp>{
    @Override
    protected void initPresenter() {
        mPresenter=new DSDetailPresenterImp(mActivity);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected String getSubFunName() {
        return "销售出库";
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
