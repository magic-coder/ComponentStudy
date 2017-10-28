package com.richfit.module_xngd.module_ms.ubsto;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_xngd.R;
import com.richfit.module_xngd.module_ms.ubsto.imp.XNGDMSDetailPresenterImp;
import com.richfit.sdk_wzyk.base_ms_detail.BaseMSDetailFragment;
import com.richfit.sdk_wzyk.base_ms_detail.imp.MSDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/7/20.
 */

public class XNGDMSYDetailFragment extends BaseMSDetailFragment<XNGDMSDetailPresenterImp>{

    @Override
    public void initPresenter() {
        mPresenter = new XNGDMSDetailPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int viewType) {
        if(viewType == Global.CHILD_NODE_HEADER_TYPE || viewType == Global.CHILD_NODE_ITEM_TYPE) {
            holder.setVisible(R.id.tv_location_type, false);
        }
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected String getSubFunName() {
        return "ubsto转储";
    }


    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        return tmp.subList(0,1);
    }


    /**
     * 第一步过账成功后直接跳转
     */
    @Override
    public void submitBarcodeSystemSuccess() {
        showSuccessDialog(mShowMsg);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        mRefData = null;
        mShowMsg.setLength(0);
        mTransId = "";
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }
}
