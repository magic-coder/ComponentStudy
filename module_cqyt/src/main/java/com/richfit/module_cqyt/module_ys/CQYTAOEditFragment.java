package com.richfit.module_cqyt.module_ys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

/**
 * Created by monday on 2017/7/5.
 */

public class CQYTAOEditFragment extends BaseASEditFragment<ASEditPresenterImp> {

    TextView tvInsLotQuantity;

    @Override
    public void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        TextView tvRefLineNumName = (TextView) mView.findViewById(R.id.tv_ref_line_num_name);
        tvRefLineNumName.setText("检验批");
        LinearLayout llInsLotQuantity = (LinearLayout) mView.findViewById(R.id.ll_inslot_quantity);
        tvInsLotQuantity = (TextView) mView.findViewById(R.id.tv_insLot_quantity);
        llInsLotQuantity.setVisibility(View.VISIBLE);
        tvActQuantityName.setText("允许过账数量");
        quantityName.setText("过账数量");

    }

    @Override
    public void initData() {
        super.initData();
        final RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        tvInsLotQuantity.setText(lineData.orderQuantity);
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {

        if (!isNLocation && !isValidatedLocation(getString(etLocation))) {
            return false;
        }

        if (TextUtils.isEmpty(getString(etQuantity))) {
            showMessage("请输入入库数量");
            return false;
        }

        float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        float totalQuantityV = CommonUtil.convertToFloat(getString(tvTotalQuantity), 0.0f);
        float collectedQuantity = CommonUtil.convertToFloat(mQuantity, 0.0f);
        float quantityV = CommonUtil.convertToFloat(getString(etQuantity), 0.0f);
        if (Float.compare(quantityV, 0.0f) <= 0.0f) {
            showMessage("输入数量不合理");
            etQuantity.setText("");
            return false;
        }
        float residualQuantity = totalQuantityV - collectedQuantity + quantityV;//减去已经录入的数量
        if (Float.compare(residualQuantity, actQuantityV) > 0.0f) {
            showMessage("输入实收数量不能大于应收数量");
            etQuantity.setText("");
            return false;
        }
        final float orderQuantityV = CommonUtil.convertToFloat(getString(tvInsLotQuantity),0.0F);
        if (Float.compare(residualQuantity, orderQuantityV) > 0.0f) {
            showMessage("输入实收数量不能大于检验批数量");
            etQuantity.setText("");
            return false;
        }
        mQuantity = quantityV + "";
        mTotalQuantity = residualQuantity;
        return true;
    }


    @Override
    public void initDataLazily() {

    }
}
