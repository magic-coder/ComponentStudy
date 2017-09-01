package com.richfit.module_cqyt.module_as105;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.adapter.CQYTAS105DetailAdapter;
import com.richfit.module_cqyt.module_as105.imp.CQYTAS105DetailPresenterImp;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;

import java.util.List;

/**
 * 青海105必检物资入库的明细界面，注意它与标准的明细界面需要显示
 * 检验批，物料凭证号，物料凭证行号，退货交货数量，移动原因，移动原因说明，项目文本,
 * 以及决策代码
 * Created by monday on 2017/3/7.
 */

public class CQYTAS105DetailFragment extends BaseASDetailFragment<CQYTAS105DetailPresenterImp> {

    /**
     * 注意这里由于105必检的客户化字段太多，我们没有将其写入标准库
     * @return
     */
    @Override
    protected int getContentId() {
        return R.layout.cqyt_fragment_as105_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new CQYTAS105DetailPresenterImp(mActivity);
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
     * 105必检入库与标准入库的明细界面多出很多字段，我们需要重写该方法。
     * 注意这由于我们给出了新的适配器，所以不需要回调onBindViewHolder方法。
     *
     * @param allNodes
     */
    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        if (mAdapter == null) {
            mAdapter = new CQYTAS105DetailAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    @Override
    protected String getSubFunName() {
        return "物资入库-105必检";
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "10";
        menus.get(1).transToSapFlag = "11";
        return menus.subList(0, 2);
    }


}
