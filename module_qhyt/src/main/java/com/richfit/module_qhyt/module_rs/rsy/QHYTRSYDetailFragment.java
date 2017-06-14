package com.richfit.module_qhyt.module_rs.rsy;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;
import com.richfit.sdk_wzrk.base_as_detail.imp.ASDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/2/27.
 */

public class QHYTRSYDetailFragment extends BaseASDetailFragment<ASDetailPresenterImp> {


    @Override
    protected void initView() {
        tvActQuantity.setText("应退数量");
        super.initView();
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int viewType) {
        if(viewType == Global.CHILD_NODE_HEADER_TYPE) {
            viewHolder.setText(R.id.quantity,"实退数量");
        }
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASDetailPresenterImp(mActivity);
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "01";
        menus.get(1).transToSapFlag = "05";
        return menus.subList(0, 2);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected String getSubFunName() {
        return "转储退库";
    }
}
