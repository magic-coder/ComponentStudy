package com.richfit.module_qysh.module_ds;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzck.base_ds_detail.BaseDSDetailFragment;
import com.richfit.sdk_wzck.base_ds_detail.imp.DSDetailPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHDSYDetailFragment extends BaseDSDetailFragment<DSDetailPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new DSDetailPresenterImp(mActivity);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int viewType) {
        //隐藏子节点的批次
        super.onBindViewHolder(holder, viewType);
        if (viewType == Global.CHILD_NODE_HEADER_TYPE || viewType == Global.CHILD_NODE_ITEM_TYPE) {
            holder.setVisible(R.id.batchFlag, false);
        }
    }

    @Override
    protected String getSubFunName() {
        return "其他物资出库";
    }


    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "01";
        menus.get(2).transToSapFlag = "04";
        ArrayList<BottomMenuEntity> tmp = new ArrayList<>();
        tmp.add(menus.get(0));
        tmp.add(menus.get(2));
        return tmp;
    }
}
