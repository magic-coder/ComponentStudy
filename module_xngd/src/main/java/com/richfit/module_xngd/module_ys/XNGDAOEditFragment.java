package com.richfit.module_xngd.module_ys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    ArrayList<SimpleEntity> mInspectionTypes;
    ArrayList<SimpleEntity> mInspectionStatus;

    @Override
    protected int getContentId() {
        return R.layout.xngd_fragment_ao_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        etAllQuantity = (EditText) mView.findViewById(R.id.xngd_et_all_quantity);
        etPartQuantity = (EditText) mView.findViewById(R.id.xngd_et_part_quantity);
        spInspectionType = (Spinner) mView.findViewById(R.id.xngd_sp_inspection_type);
        spInspectionStatus = (Spinner) mView.findViewById(R.id.xngd_sp_inspection_status);
        etProcessResult = (EditText) mView.findViewById(R.id.xngd_et_process_result);
    }



    @Override
    public void initData() {
        super.initData();
        //初始化检验方法
        if (mInspectionTypes == null) {
            mInspectionTypes = new ArrayList<>();
        }
        mInspectionTypes.clear();
        List<String> inspectionTypes = getStringArray(R.array.xngd_inspection_types);
        for (int i = 0; i < inspectionTypes.size(); i++) {
            SimpleEntity item = new SimpleEntity();
            item.name = inspectionTypes.get(i);
            item.code = String.valueOf(i);
            mInspectionTypes.add(item);
        }

        SimpleAdapter inspectionTypeAdapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, mInspectionTypes);
        spInspectionType.setAdapter(inspectionTypeAdapter);

        //初始化检验状况
        if (mInspectionStatus == null) {
            mInspectionStatus = new ArrayList<>();
        }
        mInspectionStatus.clear();
        List<String> inspectionStatusList = getStringArray(R.array.xngd_inspection_status);
        for (int i = 0; i < inspectionStatusList.size(); i++) {
            SimpleEntity item = new SimpleEntity();
            item.name = inspectionStatusList.get(i);
            item.code = String.valueOf(i);
            mInspectionStatus.add(item);
        }
        SimpleAdapter inspectionStatusAdapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, mInspectionStatus);
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
            UiUtil.setSelectionForSimpleSp(mInspectionTypes, inspectionType, spInspectionType);
            //检验方法
            UiUtil.setSelectionForSimpleSp(mInspectionStatus, inspectionStatus, spInspectionStatus);
        }
    }

    @Override
    public void initDataLazily() {

    }


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
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
        //是否应急
        //修改成明细取该字段
        result.invFlag = mRefData.billDetailList.get(mPosition).invFlag;
        result.specialInvFlag = mRefData.billDetailList.get(mPosition).specialInvFlag;
        result.specialInvNum = mRefData.billDetailList.get(mPosition).specialInvNum;
        result.projectNum = mRefData.projectNum;
        return result;
    }


    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        param.queryType = "03";
        param.invType = TextUtils.isEmpty( mRefData.invType) ? "1" :  mRefData.invType;
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", lineData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum", mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }

}
