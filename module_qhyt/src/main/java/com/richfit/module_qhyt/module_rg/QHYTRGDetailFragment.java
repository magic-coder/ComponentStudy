package com.richfit.module_qhyt.module_rg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzck.base_ds_detail.BaseDSDetailFragment;
import com.richfit.sdk_wzck.base_ds_detail.imp.DSDetailPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * 子节点的发货数量修改成退货数量
 * Created by monday on 2017/2/23.
 */

public class QHYTRGDetailFragment extends BaseDSDetailFragment<DSDetailPresenterImp> {

    @Override
    protected void initView() {
        super.initView();
        TextView actQuantity = mView.findViewById(R.id.actQuantity);
        if (actQuantity != null) {
            actQuantity.setText("应退数量");
        }
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getItemViewType() == Global.CHILD_NODE_HEADER_TYPE) {
            holder.setText(R.id.quantity, "实退数量");
        }
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSDetailPresenterImp(mActivity);
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
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected String getSubFunName() {
        return "采购退货";
    }
}
