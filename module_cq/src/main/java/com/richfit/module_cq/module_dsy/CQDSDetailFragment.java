package com.richfit.module_cq.module_dsy;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cq.adapter.DsyAdapter;
import com.richfit.sdk_wzck.adapter.DSYDetailAdapter;
import com.richfit.sdk_wzck.base_ds_detail.BaseDSDetailFragment;
import com.richfit.sdk_wzck.base_ds_detail.imp.DSDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/12/5.
 */

public class CQDSDetailFragment extends BaseDSDetailFragment<DSDetailPresenterImp>{

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
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        saveTurnFlag(allNodes);
        if (mAdapter == null) {
            mAdapter = new DsyAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    @Override
    protected String getSubFunName() {
        return "物资出库";
    }

    @Override
    public void submitBarcodeSystemSuccess() {
        setRefreshing(false, "过账成功");
        showSuccessDialog(mShowMsg);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        //注意这里必须清除单据数据
        mRefData = null;
        mShowMsg.setLength(0);
        mTransId = "";
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "01";
        return menus.subList(0,1);
    }
}
