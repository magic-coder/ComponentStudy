package com.richfit.module_qhyt.module_ds.ds_ww;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.sdk_wzck.base_ds_detail.BaseDSDetailFragment;
import com.richfit.sdk_wzck.base_ds_detail.imp.DSDetailPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/3/5.
 */

public class QHYTDSWWDetailFragment extends BaseDSDetailFragment<DSDetailPresenterImp> {


    @Override
    protected String getSubFunName() {
        return "委外出库";
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSDetailPresenterImp(mActivity);
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        tmp.get(0).transToSapFlag = "01";
        tmp.get(2).transToSapFlag = "05";
        ArrayList menus = new ArrayList();
        menus.add(tmp.get(0));
        menus.add(tmp.get(2));
        return menus;
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
}
