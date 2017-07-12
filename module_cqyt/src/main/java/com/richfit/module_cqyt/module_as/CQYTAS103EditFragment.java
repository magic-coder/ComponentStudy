package com.richfit.module_cqyt.module_as;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.module_ms.y313.CQYTMSY313EditFragment;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by monday on 2017/7/4.
 */

public class CQYTAS103EditFragment extends BaseASEditFragment<ASEditPresenterImp> {

    public static final String extra_inspection_result_key = "extra_inspection_result";
    public static final String extra_unqualified_key = "extra_unqualified";
    public static final String extra_remark_key = "extra_remark";

    Spinner spInspectionResult;
    EditText etUnqualifiedQuantity;
    EditText etRemark;
    EditText etQuantityCustom;

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_as103_edit;
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
        spInspectionResult = (Spinner) mView.findViewById(R.id.sp_inspection_result);
        etRemark = (EditText) mView.findViewById(R.id.et_remark);
        etUnqualifiedQuantity = (EditText) mView.findViewById(R.id.cqyt_et_unqualified_quantity);
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
    }

    @Override
    public void initData() {
        super.initData();
        final List<String> items = getStringArray(R.array.cqyt_inspection_results);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, items);
        spInspectionResult.setAdapter(adapter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String quantityCustom = bundle.getString(CQYTMSY313EditFragment.EXTRA_QUANTITY_CUSTOM_KEY);
            String inspectionResult = bundle.getString(extra_inspection_result_key);
            String remark = bundle.getString(extra_remark_key);
            String unqualifiedQuantity = bundle.getString(extra_unqualified_key);
            etRemark.setText(remark);
            etUnqualifiedQuantity.setText(unqualifiedQuantity);
            etQuantityCustom.setText(quantityCustom);
            UiUtil.setSelectionForSp(items, inspectionResult, spInspectionResult);
        }
    }


    @Override
    public void initDataLazily() {

    }


    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (spInspectionResult.getSelectedItemPosition() == 1 && TextUtils.isEmpty(getString(etUnqualifiedQuantity))) {
            showMessage("请先输入不合格数量");
            return false;
        }
        final String quantityCustom = getString(etQuantityCustom);
        if(TextUtils.isEmpty(quantityCustom)) {
            showMessage("请先输入件数");
            return false;
        }
        if (Float.valueOf(quantityCustom) < 0.0f) {
            showMessage("件数不合理");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }

    @Override
    public void saveCollectedData() {
        if (!checkCollectedDataBeforeSave()) {
            return;
        }
        Flowable.create((FlowableOnSubscribe<ResultEntity>) emitter -> {
            RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
            ResultEntity result = new ResultEntity();
            result.businessType = mRefData.bizType;
            result.refCodeId = mRefData.refCodeId;
            result.refCode = mRefData.recordNum;
            result.refLineNum = lineData.lineNum;
            result.voucherDate = mRefData.voucherDate;
            result.refType = mRefData.refType;
            result.moveType = mRefData.moveType;
            result.userId = Global.USER_ID;
            result.refLineId = lineData.refLineId;
            result.workId = lineData.workId;
            result.unit = TextUtils.isEmpty(lineData.recordUnit) ? lineData.materialUnit : lineData.recordUnit;
            result.unitRate = Float.compare(lineData.unitRate, 0.0f) == 0 ? 1.f : lineData.unitRate;
            result.invId = CommonUtil.Obj2String(tvInv.getTag());
            result.materialId = lineData.materialId;
            result.locationId = mLocationId;
            result.location = isNLocation ? "barcode" : getString(etLocation);
            result.batchFlag = getString(tvBatchFlag);
            result.quantity = getString(etQuantity);
            result.modifyFlag = "Y";
            result.refDoc = lineData.refDoc;
            result.refDocItem = lineData.refDocItem;
            result.supplierNum = mRefData.supplierNum;
            result.inspectionResult = spInspectionResult.getSelectedItemPosition() == 0 ? "01" : "02";
            result.unqualifiedQuantity = getString(etUnqualifiedQuantity);
            result.remark = getString(etRemark);
            //提货单
            result.deliveryOrder = mRefData.deliveryOrder;
            //件数
            result.quantityCustom = getString(etQuantityCustom);
            emitter.onNext(result);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER).compose(TransformerHelper.io2main())
                .subscribe(result -> mPresenter.uploadCollectionDataSingle(result));
    }
}
