package com.richfit.module_xngd.module_ds.dsn;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_xngd.R;
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
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    /**
     * 第一步过账成功后直接跳转
     */
    @Override
    public void submitBarcodeSystemSuccess() {
        showSuccessDialog(mShowMsg);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        mRefData = null;
        mShowMsg.setLength(0);
        mTransId = "";
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        return tmp.subList(0,1);
    }

    @Override
    protected String getSubFunName() {
        return "紧急出库";
    }


}
