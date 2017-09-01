package com.richfit.module_mcq.module_as;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_mcq.R;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

/**
 * Created by monday on 2017/8/31.
 */

public class MCQASEditFragment extends BaseASEditFragment<ASEditPresenterImp> {

    //副计量单位
    TextView tvMaterialUnitCustom;
    //副计量单位应收数量
    TextView tvActQuantityCustom;
    //副计量单位仓位数量
    TextView tvLocQuantityCustom;
    //副计量单位实收数量
    EditText etQuantityCustom;
    //副计量单位累计数量
    TextView tvTotalQuantityCustom;

    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_as_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        tvMaterialUnitCustom = mView.findViewById(R.id.mcq_tv_material_unit_custom);
        tvActQuantityCustom = mView.findViewById(R.id.mcq_tv_act_quantity_custom);
        etQuantityCustom = mView.findViewById(R.id.mcq_et_quantity_custom);
        tvTotalQuantityCustom = mView.findViewById(R.id.mcq_tv_total_quantity_custom);
        tvLocQuantityCustom = mView.findViewById(R.id.mcq_tv_location_quantity_custom);
        //主计量单位
        TextView tvMaterialUniName = mView.findViewById(R.id.mcq_tv_material_unit_name);
        tvMaterialUniName.setText("主计量单位");

        //主计量单位应收数量
        tvActQuantityName.setText("主计量单位应收数量");

        //主计量单位实收数量
        tvActQuantityName.setText("主计量单位实收数量");

        //主计量单位仓位数量
        TextView tvLocationQuantityName = mView.findViewById(R.id.mcq_tv_location_quantity_name);
        tvLocationQuantityName.setText("主计量单位仓位数量");

        //主计量单位累计数量
        TextView tvTotalQuantityName = mView.findViewById(R.id.mcq_total_quantity_name);
        tvTotalQuantityName.setText("主计量单位累计数量");
    }

    @Override
    public void initData() {
        super.initData();
        if (mRefData != null) {
            final RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
            //副计量单位
            tvMaterialUnitCustom.setText(lineData.unitCustom);
            //副计量单位应收数量
            tvActQuantityCustom.setText(lineData.actQuantityCustom);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            //副计量单位实收数量
            final String quantityCustom = bundle.getString(Global.EXTRA_QUANTITY_CUSTOM_KEY);
            //副计量单位仓位数量
            etQuantityCustom.setText(quantityCustom);
            tvLocQuantityCustom.setText(quantityCustom);
            //副计量单位累计数量（注意这里是父子节点）
            final String totalQuantityCustom = bundle.getString(Global.EXTRA_TOTAL_QUANTITY_CUSTOM_KEY);
            tvTotalQuantityCustom.setText(totalQuantityCustom);
        }
    }

    @Override
    public void initDataLazily() {

    }
}
