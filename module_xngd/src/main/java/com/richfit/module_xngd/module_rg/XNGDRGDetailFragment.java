package com.richfit.module_xngd.module_rg;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_xngd.R;
import com.richfit.module_xngd.module_rg.imp.XNGDRDDetailPresenterImp;
import com.richfit.sdk_wzck.base_ds_detail.BaseDSDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/6/20.
 */

public class XNGDRGDetailFragment extends BaseDSDetailFragment<XNGDRDDetailPresenterImp> {

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        actQuantityName.setText("应退数量");
        super.initView();
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder.getItemViewType() == Global.CHILD_NODE_HEADER_TYPE) {
            holder.setText(R.id.quantity,"实退数量");
        }
    }

    @Override
    public void initPresenter() {
        mPresenter = new XNGDRDDetailPresenterImp(mActivity);
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        return menus.subList(0, 1);
    }



    /**
     * 第一步过账成功后直接跳转
     */
    @Override
    public void submitBarcodeSystemSuccess() {
        showSuccessDialog(mTransNum);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        mRefData = null;
        mTransNum = "";
        mTransId = "";
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }

    @Override
    protected String getSubFunName() {
        return "采购退货";
    }
}
