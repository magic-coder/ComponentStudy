package com.richfit.module_qysh.module_ms.ms311;

import android.view.View;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzyk.base_msn_detail.BaseMSNDetailFragment;
import com.richfit.sdk_wzyk.base_msn_detail.imp.MSNDetailPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/11/3.
 */

public class QYSHMS311DetailFragment extends BaseMSNDetailFragment<MSNDetailPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new MSNDetailPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        super.initView();
        //隐藏发出批次
        View batchFlag = mView.findViewById(R.id.sendBatchFlag);
        if (batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
        //隐藏接收批次
        View recBatchFlag = mView.findViewById(R.id.recBatchFlag);
        if(recBatchFlag != null) {
            recBatchFlag.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected String getSubFunName() {
        return "311物资移库";
    }

    @Override
    protected boolean checkTransStateBeforeRefresh() {
        return true;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int viewType) {
        super.onBindViewHolder(holder,viewType);
        //隐藏接收和发出批次
        holder.setVisible(R.id.sendBatchFlag,false)
                .setVisible(R.id.recBatchFlag,false);
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "01";
        menus.get(3).transToSapFlag = "04";
        ArrayList<BottomMenuEntity> tmp = new ArrayList<>();
        tmp.add(menus.get(0));
        tmp.add(menus.get(3));
        return tmp;
    }
}
