package com.richfit.module_xngd.module_ds.dsy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_xngd.R;
import com.richfit.module_xngd.module_ds.dsy.imp.XNGDSLLDetailPresenterImp;
import com.richfit.sdk_wzck.base_ds_detail.BaseDSDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/1/20.
 */

public class XNGDDSYDetailFFragment extends BaseDSDetailFragment<XNGDSLLDetailPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new XNGDSLLDetailPresenterImp(mActivity);
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
        return "物资出库";
    }
}
