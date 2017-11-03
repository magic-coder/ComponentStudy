package com.richfit.module_cqyt.module_ms.y315;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.adapter.CQYTMSY315DetailAdapter;
import com.richfit.module_cqyt.module_ms.y315.imp.CQYTMSY315DetailPresenterImp;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 注意工厂和库存地点使用的workCode,和invCode,不涉及到接收
 * Created by monday on 2017/6/30.
 */

public class CQYTMSY315DetailFragment extends BaseASDetailFragment<CQYTMSY315DetailPresenterImp> {


    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_msy315_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new CQYTMSY315DetailPresenterImp(mActivity);
    }

    /**
     * 显示物资移库明细界面，注意这里显示的标准界面
     *
     * @param allNodes
     */
    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        if (mAdapter == null) {
            mAdapter = new CQYTMSY315DetailAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

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
    protected String getSubFunName() {
        return "315转储接收";
    }
}
