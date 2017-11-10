package com.richfit.module_qysh.module_ms.ms301;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzyk.base_ms_detail.BaseMSDetailFragment;
import com.richfit.sdk_wzyk.base_ms_detail.imp.MSDetailPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHMSY301DetailFragment extends BaseMSDetailFragment<MSDetailPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new MSDetailPresenterImp(mActivity);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int viewType) {
        //显示接收仓储类型
        super.onBindViewHolder(holder,viewType);
        //隐藏发出批次和接收批次
        if (viewType == Global.CHILD_NODE_HEADER_TYPE || viewType == Global.CHILD_NODE_ITEM_TYPE) {
            holder.setVisible(R.id.sendBatchFlag, false);
            holder.setVisible(R.id.recBatchFlag,false);
        }
    }

    @Override
    protected String getSubFunName() {
        return "物资移库";
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "01";
        menus.get(3).transToSapFlag = "04";
        ArrayList<BottomMenuEntity> tmp = new ArrayList<>();
        tmp.add(menus.get(0));
        tmp.add(menus.get(3));
        return tmp;
    }
}
