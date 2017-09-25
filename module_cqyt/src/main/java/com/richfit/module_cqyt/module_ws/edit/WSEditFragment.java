package com.richfit.module_cqyt.module_ws.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditFragment;
import com.richfit.data.constant.Global;
import com.richfit.module_cqyt.R;

/**
 * Created by monday on 2017/9/21.
 */

public class WSEditFragment extends BaseEditFragment<WSEditPresenterImp>
        implements IWSEditlView {
    public static final String EXTRA_DECLARATION_REF_KEY = "declaration_ref";

    //物料
    TextView tvMaterialNum;
    TextView tvMaterialDesc;
    TextView tvMaterialGroup;
    EditText etQuantity;
    EditText etBatchFlag;
    //报检单
    Spinner spDeclarationRef;
    //备注
    EditText etRemark;

    String mQuantity;

    @Override
    protected int getContentId() {
        return R.layout.cqyt_fragment_ws_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new WSEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        tvMaterialNum = mView.findViewById(R.id.tv_material_num);
        tvMaterialDesc = mView.findViewById(R.id.tv_material_desc);
        tvMaterialGroup = mView.findViewById(R.id.tv_material_group);
        etQuantity = mView.findViewById(R.id.et_quantity);
        etBatchFlag = mView.findViewById(R.id.et_batch_flag);
        spDeclarationRef = mView.findViewById(R.id.cqyt_sp_declaration_ref);
        etRemark = mView.findViewById(R.id.et_remark);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        //物料编码
        String materialNum = bundle.getString(Global.EXTRA_MATERIAL_NUM_KEY);
        String materialId = bundle.getString(Global.EXTRA_MATERIAL_ID_KEY);

        //批次
        final String batchFlag = bundle.getString(Global.EXTRA_BATCH_FLAG_KEY);

        //数量
        mQuantity = bundle.getString(Global.EXTRA_QUANTITY_KEY);

        //绑定数据
        tvMaterialNum.setText(materialNum);
        tvMaterialNum.setTag(materialId);
        etQuantity.setText(mQuantity);
        etBatchFlag.setText(batchFlag);


        //获取报检单
    }

    @Override
    public void initDataLazily() {

    }
}
