package com.richfit.module_qhyt.module_ms.ubsto351;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzyk.base_ms_detail.BaseMSDetailFragment;
import com.richfit.sdk_wzyk.base_ms_detail.imp.MSDetailPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * 351发出子节点没有接收仓位和接收批次
 * Created by monday on 2017/2/10.
 */

public class QHYTUbSto351DetailFragment extends BaseMSDetailFragment<MSDetailPresenterImp> {


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int viewType) {
        switch (viewType) {
            //隐藏子节点的发出批次和发出仓位
            case Global.CHILD_NODE_HEADER_TYPE:
            case Global.CHILD_NODE_ITEM_TYPE:
                viewHolder.setVisible(R.id.recLocation, false)
                        .setVisible(R.id.recBatchFlag, false);
                break;
        }
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }


    @Override
    public void initPresenter() {
        mPresenter = new MSDetailPresenterImp(mActivity);
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        tmp.get(0).transToSapFlag = "01";
        tmp.get(2).transToSapFlag = "05";
        //注意351是发出
        ArrayList menus = new ArrayList();
        menus.add(tmp.get(0));
        menus.add(tmp.get(2));
        return menus;
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }


    @Override
    protected String getSubFunName() {
        return "351移库";
    }

}
