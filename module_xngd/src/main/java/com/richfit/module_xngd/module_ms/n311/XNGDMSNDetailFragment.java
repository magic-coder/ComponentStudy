package com.richfit.module_xngd.module_ms.n311;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_xngd.R;
import com.richfit.module_xngd.module_ms.n311.imp.XNGDMSN311DetailPresenterImp;
import com.richfit.sdk_wzyk.base_msn_detail.BaseMSNDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/7/20.
 */

public class XNGDMSNDetailFragment extends BaseMSNDetailFragment<XNGDMSN311DetailPresenterImp>{

    @Override
    public void initPresenter() {
        mPresenter = new XNGDMSN311DetailPresenterImp(mActivity);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected String getSubFunName() {
        return "311无参考移库";
    }

    @Override
    protected boolean checkTransStateBeforeRefresh() {
        return true;
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
}
