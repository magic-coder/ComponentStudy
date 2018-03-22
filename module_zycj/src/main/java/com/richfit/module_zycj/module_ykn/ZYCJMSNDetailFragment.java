package com.richfit.module_zycj.module_ykn;

import android.view.View;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.sdk_wzyk.base_msn_detail.BaseMSNDetailFragment;
import com.richfit.sdk_wzyk.base_msn_detail.imp.MSNDetailPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/11/3.
 */

public class ZYCJMSNDetailFragment extends BaseMSNDetailFragment<MSNDetailPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new MSNDetailPresenterImp(mActivity);
    }



    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected String getSubFunName() {
        return "物资无参考移库";
    }

    @Override
    protected boolean checkTransStateBeforeRefresh() {
        return true;
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
