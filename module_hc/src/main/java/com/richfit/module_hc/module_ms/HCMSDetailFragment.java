package com.richfit.module_hc.module_ms;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.sdk_wzyk.base_ms_detail.BaseMSDetailFragment;
import com.richfit.sdk_wzyk.base_ms_detail.imp.MSDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/10/18.
 */

public class HCMSDetailFragment extends BaseMSDetailFragment<MSDetailPresenterImp> {
    @Override
    public void initPresenter() {
        mPresenter = new MSDetailPresenterImp(mActivity);
    }


    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected String getSubFunName() {
        return "物资移库";
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "01";
        menus.get(1).transToSapFlag = "05";
        return menus.subList(0, 2);
    }
}
