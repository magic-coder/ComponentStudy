package com.richfit.module_xngd.module_ds.dsn;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_xngd.module_ds.dsn.imp.XNGDDSNDetailPresenterImp;
import com.richfit.sdk_wzck.base_dsn_detail.BaseDSNDetailFragment;

import java.util.List;


/**
 * Created by monday on 2017/3/27.
 */

public class XNGDDSNDetailFragment extends BaseDSNDetailFragment<XNGDDSNDetailPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new XNGDDSNDetailPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        return tmp.subList(0,1);
    }


    /**
     * 第一步过账成功后直接跳转
     */
    @Override
    public void submitBarcodeSystemSuccess() {
        showSuccessDialog(mTransNum);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        mRefData = null;
        mTransNum = "";
        mTransId = "";
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }
}
