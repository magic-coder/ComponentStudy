package com.richfit.module_qhyt.module_as.as105n;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_qhyt.R;
import com.richfit.module_qhyt.module_as.as105n.imp.QHYTAS105NDetailPresenterImp;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;

import java.util.List;

/**
 * 青海物资入库105非必检数据明细界面。与标准入库的区别在于显示参考单据行
 * Created by monday on 2017/2/20.
 */

public class QHYTAS105NDetailFragment extends BaseASDetailFragment<QHYTAS105NDetailPresenterImp> {

    private TextView tvLineNum105;

    @Override
    public void initPresenter() {
        mPresenter = new QHYTAS105NDetailPresenterImp(mActivity);
    }


    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        super.initView();
        tvLineNum105 = (TextView) mView.findViewById(R.id.lineNum105);
        setVisibility(View.VISIBLE,tvLineNum105);
    }

    /**
     * 显示参考单据行
     * @param holder
     * @param viewType
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int viewType) {
        if(Global.PARENT_NODE_HEADER_TYPE == viewType) {
            holder.setVisible(R.id.lineNum105,true);
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
        return "物资出库105-非必检";
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "01";
        menus.get(1).transToSapFlag = "05";
        return menus.subList(0, 2);
    }
}
