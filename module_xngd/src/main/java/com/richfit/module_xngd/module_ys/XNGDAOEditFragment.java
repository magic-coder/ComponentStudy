package com.richfit.module_xngd.module_ys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.richfit.common_lib.utils.UiUtil;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

import java.util.ArrayList;

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
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
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
        return result;
    }

}
