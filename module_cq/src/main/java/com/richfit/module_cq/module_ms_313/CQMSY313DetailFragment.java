package com.richfit.module_cq.module_ms_313;

import android.view.View;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_cq.R;
import com.richfit.module_cq.module_ms_313.imp.CQMYS313DetailPresenterImp;
import com.richfit.sdk_wzyk.base_ms_detail.BaseMSDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/6/29.
 */

public class CQMSY313DetailFragment extends BaseMSDetailFragment<CQMYS313DetailPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new CQMYS313DetailPresenterImp(mActivity);
    }


    @Override
    public void initView() {
        super.initView();
        //父节点没有接收工厂和接收库存，发出工厂改为工厂
        TextView recWork = mView.findViewById(R.id.recWork);
        if (recWork != null) {
            recWork.setVisibility(View.GONE);
        }
        TextView recInv = mView.findViewById(R.id.recInv);
        if (recInv != null) {
            recInv.setVisibility(View.GONE);
        }
        TextView sendWork = mView.findViewById(R.id.sendWork);
        if (sendWork != null) {
            sendWork.setText("工厂");
        }
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int viewType) {
        super.onBindViewHolder(viewHolder,viewType);
        switch (viewType) {
            //隐藏父节点的接收工厂和接收库位
            case Global.PARENT_NODE_HEADER_TYPE:
                viewHolder.setVisible(R.id.recWork, false)
                        .setVisible(R.id.recInv, false);
                break;
            //隐藏子节点的接收仓位
            case Global.CHILD_NODE_HEADER_TYPE:
            case Global.CHILD_NODE_ITEM_TYPE:
                viewHolder.setVisible(R.id.recLocation, false);
                break;
        }
    }



    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        tmp.get(0).transToSapFlag = "01";
        ArrayList menus = new ArrayList();
        menus.add(tmp.get(0));
        return menus;
    }


    @Override
    protected String getSubFunName() {
        return "313转储发出";
    }

}
