package com.richfit.module_xngd.module_ds.dsy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_xngd.R;
import com.richfit.module_xngd.module_ds.dsy.imp.XNGDSLLDetailPresenterImp;
import com.richfit.sdk_wzck.base_ds_detail.BaseDSDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/1/20.
 */

public class XNGDDSYDetailFFragment extends BaseDSDetailFragment<XNGDSLLDetailPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new XNGDSLLDetailPresenterImp(mActivity);
    }

    @Override
    protected String getSubFunName() {
        return "领料申请出库";
    }


    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int viewType) {
        if(viewType == Global.CHILD_NODE_HEADER_TYPE||
                viewType == Global.CHILD_NODE_ITEM_TYPE) {
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
