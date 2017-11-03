package com.richfit.module_qysh.module_sxcl;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_qysh.R;
import com.richfit.module_qysh.module_sxcl.imp.QYSHLocDetailPresenterImp;
import com.richfit.sdk_sxcl.basedetail.LocQTDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/10/18.
 */

public class QYSHLocDetailFragment extends LocQTDetailFragment<QYSHLocDetailPresenterImp>{

    @Override
    public void initPresenter() {
        mPresenter = new QYSHLocDetailPresenterImp(mActivity);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int viewType) {
        super.onBindViewHolder(holder, viewType);
        //隐藏下架仓位
        if (viewType == Global.CHILD_NODE_HEADER_TYPE || viewType == Global.CHILD_NODE_ITEM_TYPE) {
            holder.setVisible(R.id.xLocation, false);
        }
        //隐藏批次
        if (viewType == Global.CHILD_NODE_HEADER_TYPE || viewType == Global.CHILD_NODE_ITEM_TYPE) {
            holder.setVisible(R.id.batchFlag, false);
        }

        //上下架数量给出上架数量
        if (viewType == Global.CHILD_NODE_HEADER_TYPE) {
            holder.setText(R.id.quantity,"上架数量");
        }
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        return tmp.subList(0, 1);
    }
}
