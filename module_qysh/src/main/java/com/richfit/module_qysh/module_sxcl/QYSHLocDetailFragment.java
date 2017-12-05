package com.richfit.module_qysh.module_sxcl;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_sxcl.basedetail.LocQTDetailFragment;
import com.richfit.sdk_sxcl.basedetail.imp.LocQTDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/10/18.
 */

public class QYSHLocDetailFragment extends LocQTDetailFragment<LocQTDetailPresenterImp>{

    @Override
    public void initPresenter() {
        mPresenter = new LocQTDetailPresenterImp(mActivity);
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
        tmp.get(0).menuName = "上架处理";
		tmp.get(0).transToSapFlag = "03";
        return tmp.subList(0, 1);
    }
}
