package com.richfit.module_cqyt.module_ms.y313;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.adapter.CQYTMSY313DetailAdapter;
import com.richfit.sdk_wzyk.base_ms_detail.BaseMSDetailFragment;
import com.richfit.sdk_wzyk.base_ms_detail.imp.MSDetailPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/6/29.
 */

public class CQYTMSY313DetailFragment extends BaseMSDetailFragment<MSDetailPresenterImp>{

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_msy313_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MSDetailPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

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
