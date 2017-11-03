package com.richfit.module_cqyt.module_as;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.module_ms.y313.CQYTMSY313EditFragment;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/7/4.
 */

public class CQYTAS103EditFragment extends BaseASEditFragment<ASEditPresenterImp> {

    public static final String EXTRA_INSPECTION_RESULT_KEY = "extra_inspection_result";
    public static final String EXTRA_UNQUALIFIED_KEY = "extra_unqualified";
    public static final String EXTRA_REMARK_KEY = "extra_remark";
    public static final String EXTRA_DECLARED_QUANTITY_KEY = "extra_declared_quantity";
    public static final String EXTRA_ARRIVAL_QUANTITY_KEY = "extra_arrival_quantity";

    Spinner spInspectionResult;
    TextView tvUnqualifiedQuantity;
    EditText etQuantityCustom;
    EditText etArrivalQuantity;


    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_as103_edit;
    }


    @Override
    public void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }


    @Override
    protected void initView() {
        super.initView();
        spInspectionResult = (Spinner) mView.findViewById(R.id.sp_inspection_result);
        tvUnqualifiedQuantity = (TextView) mView.findViewById(R.id.cqyt_tv_unqualified_quantity);
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        etArrivalQuantity = (EditText) mView.findViewById(R.id.arrival_quantity);
    }

    @Override
    public void initData() {
        super.initData();
        final List<String> items = getStringArray(R.array.cqyt_inspection_results);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, items);
        spInspectionResult.setAdapter(adapter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String quantityCustom = bundle.getString(Global.EXTRA_QUANTITY_CUSTOM_KEY);
            String inspectionResult = bundle.getString(EXTRA_INSPECTION_RESULT_KEY);
            String arrivalQuantity = bundle.getString(EXTRA_ARRIVAL_QUANTITY_KEY);
            etQuantityCustom.setText(quantityCustom);
            etArrivalQuantity.setText(arrivalQuantity);
            float quantityQ = CommonUtil.convertToFloat(getString(etQuantity), 0.0F);
            float arrivalQ = CommonUtil.convertToFloat(getString(etArrivalQuantity), 0.0F);
            tvUnqualifiedQuantity.setText(String.valueOf(arrivalQ - quantityQ));
            UiUtil.setSelectionForSp(items, inspectionResult, spInspectionResult);
        }

    }


    @Override
    public void initDataLazily() {

    }


    @Override
    public boolean checkCollectedDataBeforeSave() {
        final String quantityCustom = getString(etQuantityCustom);
        if (TextUtils.isEmpty(quantityCustom)) {
            showMessage("请先输入件数");
            return false;
        }
        if (Float.valueOf(quantityCustom) < 0.0f) {
            showMessage("件数不合理");
            return false;
        }
        //处理到货数量
        final float quantityV = CommonUtil.convertToFloat(getString(etQuantity), 0.0F);
        final float arrivalQuantityV = CommonUtil.convertToFloat(getString(etArrivalQuantity), 0.0F);
        if (Float.compare(arrivalQuantityV, 0.0f) <= 0.0f) {
            showMessage("输入到货数量不合理");
            return false;
        }
        if (Float.compare(quantityV, arrivalQuantityV) > 0.0f) {
            showMessage("输入实收数量不能大于到货数量");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }

    @Override
    public void saveEditedDataSuccess(String message) {
        //更新不不合格数量
        float quantityQ = CommonUtil.convertToFloat(getString(etQuantity), 0.0F);
        float arrivalQ = CommonUtil.convertToFloat(getString(etArrivalQuantity), 0.0F);
        tvUnqualifiedQuantity.setText(String.valueOf(arrivalQ - quantityQ));
        super.saveEditedDataSuccess(message);
    }


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        //验收结果
        result.inspectionResult = spInspectionResult.getSelectedItemPosition() == 0 ? "01" : "02";
        //提货单
        result.deliveryOrder = mRefData.deliveryOrder;
        //件数
        result.quantityCustom = getString(etQuantityCustom);
        //到货数量
        result.arrivalQuantity = getString(etArrivalQuantity);
        return result;
    }
}
