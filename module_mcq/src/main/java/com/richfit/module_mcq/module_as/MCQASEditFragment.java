package com.richfit.module_mcq.module_as;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_mcq.R;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    String mQuantityCustom;
    float mTotalQuantityCustom;

    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_as_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
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
        tvQuantityName.setText("主计量单位实收数量");

        //主计量单位仓位数量
        TextView tvLocationQuantityName = mView.findViewById(R.id.mcq_tv_location_quantity_name);
        tvLocationQuantityName.setText("主计量单位仓位数量");

        //主计量单位累计数量
        TextView tvTotalQuantityName = mView.findViewById(R.id.mcq_total_quantity_name);
        tvTotalQuantityName.setText("主计量单位累计数量");

        //隐藏批次
        mView.findViewById(R.id.ll_batch_flag).setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null && mRefData != null) {
            final RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
            //副计量单位
            tvMaterialUnitCustom.setText(lineData.unitCustom);
            //副计量单位应收数量
            tvActQuantityCustom.setText(lineData.actQuantityCustom);
            //副计量单位实收数量
            mQuantityCustom = bundle.getString(Global.EXTRA_QUANTITY_CUSTOM_KEY);
            //副计量单位仓位数量
            etQuantityCustom.setText(mQuantityCustom);
            tvLocQuantityCustom.setText(mQuantityCustom);
            //副计量单位累计数量（注意这里是父子节点）
            final String totalQuantityCustom = bundle.getString(Global.EXTRA_TOTAL_QUANTITY_CUSTOM_KEY);
            tvTotalQuantityCustom.setText(totalQuantityCustom);
        }
    }

    @Override
    public void initDataLazily() {

    }

    //增加副计量单位数量的校验
    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (!super.checkCollectedDataBeforeSave()) {
            return false;
        }

        if (!TextUtils.isEmpty(getString(tvMaterialUnitCustom))) {
            if (TextUtils.isEmpty(getString(etQuantityCustom))) {
                showMessage("请输入副计量的实收数量");
                return false;
            }

            float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantityCustom), 0.0f);
            float totalQuantityV = CommonUtil.convertToFloat(getString(tvTotalQuantityCustom), 0.0f);
            float collectedQuantity = CommonUtil.convertToFloat(mQuantityCustom, 0.0f);
            float quantityV = CommonUtil.convertToFloat(getString(etQuantityCustom), 0.0f);
            if (Float.compare(quantityV, 0.0f) <= 0.0f) {
                showMessage("副计量单位的实收数量输入不合理");
                etQuantity.setText("");
                return false;
            }
            float residualQuantity = totalQuantityV - collectedQuantity + quantityV;//减去已经录入的数量
            if (Float.compare(residualQuantity, actQuantityV) > 0.0f) {
                showMessage("副计量单位的实收数量输入有误");
                etQuantity.setText("");
                return false;
            }
            mQuantityCustom = quantityV + "";
            mTotalQuantityCustom = residualQuantity;
        }
        return true;
    }

    @Override
    public void saveEditedDataSuccess(String message) {
        super.saveEditedDataSuccess(message);
        if (!isNLocation) {
            tvLocQuantityCustom.setText(mQuantityCustom);
        }
        tvTotalQuantityCustom.setText(String.valueOf(mTotalQuantityCustom));
    }


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.quantityCustom = getString(etQuantityCustom);
        result.batchFlag = !isOpenBatchManager ? null : getString(tvBatchFlag);
        return result;
    }
}
