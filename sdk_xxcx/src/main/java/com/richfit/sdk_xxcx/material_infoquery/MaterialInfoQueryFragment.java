package com.richfit.sdk_xxcx.material_infoquery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.sdk_xxcx.R;
import com.richfit.sdk_xxcx.R2;
import com.richfit.sdk_xxcx.material_infoquery.imp.MaterialInfoQueryPresenterImp;

import butterknife.BindView;

/**
 * 物资条码信息查询
 * Created by monday on 2017/3/16.
 */

public class MaterialInfoQueryFragment extends BaseFragment<MaterialInfoQueryPresenterImp>
        implements IMaterialInfoQueryView {

    @BindView(R2.id.et_material_num)
    RichEditText etMaterialNum;
    @BindView(R2.id.tv_material_desc)
    TextView tvMaterialDesc;
    @BindView(R2.id.tv_material_group)
    TextView tvMaterialGroup;
    @BindView(R2.id.tv_material_unit)
    TextView tvMaterialUnit;
    @BindView(R2.id.tv_batch_flag)
    TextView tvBatchFlag;


    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        String materialNum;
        String batchFlag;

        if (list != null && list.length > 12) {
            materialNum = list[Global.MATERIAL_POS];
            batchFlag = list[Global.BATCHFALG_POS];

            etMaterialNum.setText(materialNum);
            tvBatchFlag.setText(batchFlag);
            loadMaterialInfo(materialNum);
        } else if (list != null && list.length >= 7 && list.length <= 12) {
            materialNum = list[Global.MATERIAL_POS_L];
            batchFlag = list[Global.BATCHFALG_POS_L];
            etMaterialNum.setText(materialNum);
            tvBatchFlag.setText(batchFlag);
            loadMaterialInfo(materialNum);
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.xxcx_fragment_material_info_query;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MaterialInfoQueryPresenterImp(mActivity);
    }


    @Override
    public void initEvent() {
        //手动输入物料编码查询物料信息
        etMaterialNum.setOnRichEditTouchListener((view, materialNum) -> {
            hideKeyboard(view);
            loadMaterialInfo(materialNum);
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initDataLazily() {

    }

    /**
     * 查询物料信息
     *
     * @param materialNum
     */
    private void loadMaterialInfo(String materialNum) {
        if (TextUtils.isEmpty(materialNum)) {
            showMessage("请输入物料条码");
            return;
        }
        clearAllUI();
        mPresenter.getMaterialInfo("01", materialNum);
    }

    @Override
    public void querySuccess(MaterialEntity entity) {
        if (entity != null) {
            tvMaterialDesc.setText(entity.materialDesc);
            tvMaterialGroup.setText(entity.materialGroup);
            tvMaterialUnit.setText(entity.unit);
        }
    }

    @Override
    public void queryFail(String message) {
        showMessage(message);
    }

    private void clearAllUI() {
        clearCommonUI(tvMaterialDesc, tvMaterialGroup, tvMaterialUnit);
    }


    @Override
    public void retry(String action) {
        loadMaterialInfo(getString(etMaterialNum));
        super.retry(action);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {

    }


    @Override
    public boolean isNeedShowFloatingButton() {
        return false;
    }
}
