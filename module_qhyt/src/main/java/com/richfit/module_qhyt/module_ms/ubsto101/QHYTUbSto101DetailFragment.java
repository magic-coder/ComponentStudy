package com.richfit.module_qhyt.module_ms.ubsto101;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;
import com.richfit.sdk_wzrk.base_as_detail.imp.ASDetailPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * 101转储接收，注意父节点显示的节点字段为接收库位和接收工厂;
 * 子节点字段为接收仓位，接收批次
 * Created by monday on 2017/2/15.
 */

public class QHYTUbSto101DetailFragment extends BaseASDetailFragment<ASDetailPresenterImp> {


    @Override
    protected void initView() {
        //父节点显示接收库位和接收工厂、
        TextView work = mView.findViewById(R.id.work);
        if(work != null) {
          work.setText("接收工厂");
        }
        TextView inv = mView.findViewById(R.id.inv);
        if(work != null) {
            inv.setText("接收库位");
        }
        super.initView();
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int viewType) {
        if (Global.CHILD_NODE_HEADER_TYPE == viewType) {
            //子节点仅仅显示接收仓位和接收批次
            viewHolder.setText(R.id.location, "接收仓位")
                    .setText(R.id.batchFlag, "接收批次");
        }
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASDetailPresenterImp(mActivity);
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        tmp.get(0).transToSapFlag = "01";
        tmp.get(1).transToSapFlag = "05";
        ArrayList menus = new ArrayList();
        menus.add(tmp.get(0));
        menus.add(tmp.get(1));
        return menus;
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected String getSubFunName() {
        return "101移库";
    }


}
