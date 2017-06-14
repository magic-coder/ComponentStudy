package com.richfit.module_qhyt.module_ds.ds_xs;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.sdk_wzck.adapter.DSYDetailAdapter;
import com.richfit.sdk_wzck.base_ds_detail.BaseDSDetailFragment;
import com.richfit.sdk_wzck.base_ds_detail.imp.DSDetailPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * 06。所有行必须都完成才能过账
 * Created by monday on 2017/1/20.
 */

public class QHYTDSXSDetailFFragment extends BaseDSDetailFragment<DSDetailPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new DSDetailPresenterImp(mActivity);
    }

    @Override
    protected String getSubFunName() {
        return "销售出库";
    }

    /**
     * 1.过账。必须保证所有的明细行都完成了才能开始过账,所以需要重写第一步过账方法
     */
    @Override
    protected void submit2BarcodeSystem(String tranToSapFlag) {
        if (mAdapter != null && DSYDetailAdapter.class.isInstance(mAdapter)) {
            final DSYDetailAdapter adapter = (DSYDetailAdapter) mAdapter;
            if (!adapter.isTransferValide()) {
                showMessage("您必须对所有的明细采集数据后，才能过账");
                return;
            }
        }
        //调用父类的过账方法，进入标准出库流程
        super.submit2BarcodeSystem(tranToSapFlag);
    }


    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        tmp.get(0).transToSapFlag = "06";
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
