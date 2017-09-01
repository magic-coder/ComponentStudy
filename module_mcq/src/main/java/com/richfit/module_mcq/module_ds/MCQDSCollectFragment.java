package com.richfit.module_mcq.module_ds;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.module_mcq.R;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

/**
 * Created by monday on 2017/8/31.
 */

public class MCQDSCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {

    //副计量单位
    TextView tvMaterialUnitCustom;

    //副计量单位应发数量
    TextView tvActQuantityCustom;

    //副计量单位库存数量
    TextView tvInvQuantityCustom;

    //副计量单位仓位数量
    TextView tvLocationQuantityCustom;

    //副计量单位实发数量
    EditText etQuantityCustom;

    //副计量单位累计数量
    TextView tvTotalQuantityCustom;

    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_ds_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        tvMaterialUnitCustom = mView.findViewById(R.id.mcq_material_unit_custom);
        tvActQuantityCustom = mView.findViewById(R.id.mcq_tv_act_quantity_custom);
        tvLocationQuantityCustom = mView.findViewById(R.id.mcq_tv_location_quantity_custom);
        etQuantityCustom = mView.findViewById(R.id.mcq_quantity_custom);
        tvInvQuantityCustom = mView.findViewById(R.id.mcq_tv_inv_quantity_custom);
        tvTotalQuantityCustom = mView.findViewById(R.id.mcq_tv_total_quantity_custom);

        //实际下架仓位
        TextView tvLocationName = mView.findViewById(R.id.mcq_location_name);
        tvLocationName.setText("实际下架仓位");


        //主计量单位
        TextView tvMaterialUniName = mView.findViewById(R.id.mcq_tv_material_unit_name);
        tvMaterialUniName.setText("主计量单位");

        //主计量单位应收数量
        tvActQuantityName.setText("主计量单位应发数量");

        //主计量单位实收数量
        tvQuantityName.setText("主计量单位实发数量");

        //主计量单位仓位数量
        TextView tvLocationQuantityName = mView.findViewById(R.id.mcq_tv_location_quantity_name);
        tvLocationQuantityName.setText("主计量单位仓位数量");

        //主计量单位累计数量
        TextView tvTotalQuantityName = mView.findViewById(R.id.mcq_total_quantity_name);
        tvTotalQuantityName.setText("主计量单位累计数量");

    }

    @Override
    public void initData() {

    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
