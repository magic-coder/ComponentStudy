package com.richfit.module_xngd.module_as;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.sdk_wzrk.base_asn_detail.BaseASNDetailFragment;
import com.richfit.sdk_wzrk.base_asn_detail.imp.ASNDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/6/23.
 */

public class XNGDASNDetailFragment extends BaseASNDetailFragment<ASNDetailPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new ASNDetailPresenterImp(mActivity);
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

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        return tmp.subList(0,1);
    }
}
