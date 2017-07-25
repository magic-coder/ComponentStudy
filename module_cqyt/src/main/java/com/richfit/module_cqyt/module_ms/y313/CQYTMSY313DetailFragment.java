package com.richfit.module_cqyt.module_ms.y313;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.adapter.CQYTMSY313DetailAdapter;
import com.richfit.module_cqyt.module_ms.y313.imp.CQYTMYS313DetailPresenterImp;
import com.richfit.sdk_wzyk.base_ms_detail.BaseMSDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/6/29.
 */

public class CQYTMSY313DetailFragment extends BaseMSDetailFragment<CQYTMYS313DetailPresenterImp> {

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_msy313_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new CQYTMYS313DetailPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        super.initView();
        //父节点没有接收工厂和接收库存，发出工厂改为工厂
        setVisibility(View.GONE, tvRecWork, tvRecInv);
        tvSendWork.setText("工厂");
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int viewType) {
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

    /**
     * 显示物资移库明细界面，注意这里显示的标准界面
     *
     * @param allNodes
     */
    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        saveTurnFlag(allNodes);
        if (mAdapter == null) {
            mAdapter = new CQYTMSY313DetailAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        tmp.get(0).transToSapFlag = "01";
        //下架
        tmp.get(2).transToSapFlag = "05";
        ArrayList menus = new ArrayList();
        menus.add(tmp.get(0));
        menus.add(tmp.get(2));
        return menus;
    }


    @Override
    protected String getSubFunName() {
        return "313转储发出";
    }

}
