package com.richfit.module_qysh.module_rs;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;
import com.richfit.sdk_wzrk.base_as_detail.imp.ASDetailPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHRSDetailFragment extends BaseASDetailFragment<ASDetailPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new ASDetailPresenterImp(mActivity);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int viewType) {
        super.onBindViewHolder(holder,viewType);
        //隐藏子节点的批次
        if (viewType == Global.CHILD_NODE_HEADER_TYPE || viewType == Global.CHILD_NODE_ITEM_TYPE) {
            holder.setVisible(R.id.batchFlag, false);
        }
    }


    @Override
    protected String getSubFunName() {
        return "物资退库";
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "01";
        menus.get(1).transToSapFlag = "03";
        return menus.subList(0,2);
    }
}
