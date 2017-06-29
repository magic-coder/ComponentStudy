package com.richfit.module_xngd.module_rg;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.module_xngd.R;
import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;

import java.util.ArrayList;

/**
 * 退货物资类型。非限制和限制存入到invFlag。非限制为1
 * Created by monday on 2017/6/20.
 */

public class XNGDRGHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {

    //是否应急
    private TextView tvInvFlag;
    //退货物资类型
    private Spinner spRgGoodsTypes;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_rg_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        llSuppier.setVisibility(View.VISIBLE);
        llCreator.setVisibility(View.GONE);
        super.initView();
        tvInvFlag = (TextView) mView.findViewById(R.id.xngd_tv_inv_flag);
        spRgGoodsTypes = (Spinner) mView.findViewById(R.id.xngd_sp_goods_type);
    }

    @Override
    public void initData() {
        ArrayList<String> list = new ArrayList<>();
        list.add("非限制");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp,
                list);
        spRgGoodsTypes.setAdapter(adapter);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void bindCommonHeaderUI() {
        super.bindCommonHeaderUI();
        if (mRefData != null) {
            tvInvFlag.setText(mRefData.invFlag);
        }
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null && spRgGoodsTypes.getAdapter() != null) {
            //如果选择非限制，那么invType为1
            mRefData.invType = spRgGoodsTypes.getSelectedItemPosition() == 0 ? String.valueOf(1) :
            String.valueOf(0);
        }
    }

    @Override
    public boolean isNeedShowFloatingButton() {
        return false;
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "2";
    }

}
