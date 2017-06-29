package com.richfit.module_xngd.module_ys;

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
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

import java.util.ArrayList;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by monday on 2017/5/26.
 */

public class XNGDAOEditFragment extends BaseASEditFragment<ASEditPresenterImp> {

    public final static String EXTRA_ALL_QUANTITY_KEY = "extra_all_quantity";
    public final static String EXTRA_PART_QUANTITY_KEY = "extra_part_quantity";
    public final static String EXTRA_INSPECTION_TYPE_KEY = "extra_inspection_type";
    public final static String EXTRA_INSPECTION_STATUS_KEY = "extra_inspection_status";
    public final static String EXTRA_PROCESS_RESULT_KEY = "extra_process_result";

    //全检数量
    EditText etAllQuantity;
    //抽检数量
    EditText etPartQuantity;
    //检验方法
    Spinner spInspectionType;
    //检验状态
    Spinner spInspectionStatus;
    //处理结果
    EditText etProcessResult;

    ArrayList<String> mInspectionTypes;
    ArrayList<String> mInspectionStatus;

    @Override
    protected int getContentId() {
        return R.layout.xngd_fragment_ao_edit;
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
        etAllQuantity = (EditText) mView.findViewById(R.id.xngd_et_all_quantity);
        etPartQuantity = (EditText) mView.findViewById(R.id.xngd_et_part_quantity);
        spInspectionType = (Spinner) mView.findViewById(R.id.xngd_sp_inspection_type);
        spInspectionStatus = (Spinner) mView.findViewById(R.id.xngd_sp_inspection_status);
        etProcessResult = (EditText) mView.findViewById(R.id.xngd_et_process_result);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        super.initData();

        //初始化检验方法
        if (mInspectionTypes == null) {
            mInspectionTypes = new ArrayList<>();
        }
        mInspectionTypes.clear();
        mInspectionTypes.addAll(getStringArray(R.array.xngd_inspection_types));

        ArrayAdapter<String> inspectionTypeAdapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, mInspectionTypes);
        spInspectionType.setAdapter(inspectionTypeAdapter);

        //初始化检验状况
        if (mInspectionStatus == null) {
            mInspectionStatus = new ArrayList<>();
        }
        mInspectionStatus.clear();
        mInspectionStatus.addAll(getStringArray(R.array.xngd_inspection_status));
        ArrayAdapter<String> inspectionStatusAdapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, mInspectionStatus);
        spInspectionStatus.setAdapter(inspectionStatusAdapter);

        //初始化缓存数据
        Bundle bundle = getArguments();
        if(bundle != null) {
            String allQuantity = bundle.getString(EXTRA_ALL_QUANTITY_KEY);
            String partQuantity = bundle.getString(EXTRA_PART_QUANTITY_KEY);
            String inspectionType = bundle.getString(EXTRA_INSPECTION_TYPE_KEY);
            String inspectionStatus = bundle.getString(EXTRA_INSPECTION_STATUS_KEY);
            String processResult = bundle.getString(EXTRA_PROCESS_RESULT_KEY);
            etAllQuantity.setText(allQuantity);
            etPartQuantity.setText(partQuantity);
            etProcessResult.setText(processResult);
            //检验状况
            UiUtil.setSelectionForSp(mInspectionTypes, inspectionType, spInspectionType);
            //检验方法
            UiUtil.setSelectionForSp(mInspectionStatus, inspectionStatus, spInspectionStatus);
        }
    }

    @Override
    public void initDataLazily() {

    }


    @Override
    public void saveCollectedData() {
        if (!checkCollectedDataBeforeSave()) {
            return;
        }
        Flowable.create((FlowableOnSubscribe<ResultEntity>) emitter -> {
            RefDetailEntity lineData =  mRefData.billDetailList.get(mPosition);
            ResultEntity result = new ResultEntity();
            result.businessType = mRefData.bizType;
            result.refCodeId = mRefData.refCodeId;
            result.refCode = mRefData.recordNum;
            result.refLineNum = lineData.lineNum;
            result.voucherDate = mRefData.voucherDate;
            result.refType = mRefData.refType;
            result.moveType = mRefData.moveType;
            result.userId = Global.USER_ID;
            result.companyCode = Global.COMPANY_CODE;
            result.inspectionPerson = Global.USER_ID;
            result.refLineId = lineData.refLineId;
            result.workId = lineData.workId;
            result.unit = TextUtils.isEmpty(lineData.recordUnit) ? lineData.materialUnit : lineData.recordUnit;
            result.unitRate = Float.compare(lineData.unitRate, 0.0f) == 0 ? 1.f : lineData.unitRate;
            result.invId = CommonUtil.Obj2String(tvInv.getTag());
            result.materialId = lineData.materialId;
            result.locationId = mLocationId;
            result.location = isNLocation ? "barcode" : getString(etLocation);
            result.batchFlag = "20170101";
            result.specialInvFlag = "N";
            result.invType = "1";
            result.quantity = getString(etQuantity);
            result.modifyFlag = "Y";
            result.refDoc = lineData.refDoc;
            result.refDocItem = lineData.refDocItem;
            result.supplierNum = mRefData.supplierNum;
            //是否应急
            result.invFlag = mRefData.invFlag;
            //全检数量
            result.allQuantity = getString(etAllQuantity);
            //抽检数量
            result.partQuantity = getString(etPartQuantity);
            //检验方法
            if (spInspectionType.getSelectedItemPosition() > 0)
                result.inspectionType = spInspectionType.getSelectedItemPosition();
            //检验状况
            if (spInspectionStatus.getSelectedItemPosition() > 0) {
                result.inspectionStatus = String.valueOf(spInspectionStatus.getSelectedItemPosition());
            }
            //处理情况
            result.processResult = getString(etProcessResult);

            emitter.onNext(result);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER).compose(TransformerHelper.io2main())
                .subscribe(result -> mPresenter.uploadCollectionDataSingle(result));
    }

}
