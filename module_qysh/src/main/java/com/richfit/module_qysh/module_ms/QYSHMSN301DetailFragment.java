package com.richfit.module_qysh.module_ms;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_qysh.module_ms.imp.QYSHMSN301DetailPresenterImp;
import com.richfit.sdk_wzyk.base_msn_detail.BaseMSNDetailFragment;

import java.util.List;

/**
 * 庆阳301数据过账只有一步
 * Created by monday on 2017/2/8.
 */

public class QYSHMSN301DetailFragment extends BaseMSNDetailFragment<QYSHMSN301DetailPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new QYSHMSN301DetailPresenterImp(mActivity);
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
    public void initDataLazily() {
        if (mRefData == null) {
            setRefreshing(false, "获取明细失败,请现在抬头界面选择相应的参数");
            return;
        }

        if (TextUtils.isEmpty(mRefData.workId) ||
                TextUtils.isEmpty(mRefData.recWorkId)) {
            setRefreshing(false, "获取明细失败,请先选择发出工厂和接收工厂");
            return;
        }

        if (TextUtils.isEmpty(mRefData.recInvId)) {
            setRefreshing(false, "获取明细失败,请先选择接收库位");
            return;
        }

        startAutoRefresh();
    }

    @Override
    public void submitBarcodeSystemSuccess() {
        setRefreshing(false, "过账成功");
        showSuccessDialog(mShowMsg);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        mRefData = null;
        mTransId = "";
        mShowMsg.setLength(0);
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }


    @Override
    protected boolean checkTransStateBeforeRefresh() {
        return true;
    }


    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        return menus.subList(3, 4);
    }


    @Override
    protected String getSubFunName() {
        return "期初物资移库";
    }
}
