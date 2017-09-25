package com.richfit.module_cqyt.module_ws.collect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectFragment;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by monday on 2017/9/21.
 */

public class WSCollectFragment extends BaseCollectFragment<WSCollectPresenterImp>
        implements IWSCollectView {

    //物料
    RichEditText etMaterialNum;
    TextView tvMaterialDesc;
    TextView tvMaterialGroup;
    EditText etQuantity;
    EditText etBatchFlag;
    //报检单
    Spinner spDeclarationRef;
    //备注
    EditText etRemark;

    List<RefDetailEntity> mHistoryDetailList;

    @Override
    protected int getContentId() {
        return R.layout.cqyt_fragment_ws_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new WSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        mRefData = null;
    }

    @Override
    protected void initView() {
        etMaterialNum = mView.findViewById(R.id.et_material_num);
        tvMaterialDesc = mView.findViewById(R.id.tv_material_desc);
        tvMaterialGroup = mView.findViewById(R.id.tv_material_group);
        etQuantity = mView.findViewById(R.id.et_quantity);
        etBatchFlag = mView.findViewById(R.id.et_batch_flag);
        spDeclarationRef = mView.findViewById(R.id.cqyt_sp_declaration_ref);
        etRemark = mView.findViewById(R.id.et_remark);
    }

    @Override
    public void initEvent() {
        etMaterialNum.setOnRichEditTouchListener((view, materialNum) -> {
            //获取物料信息
            loadMaterialInfo(materialNum,getString(etBatchFlag));
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initDataLazily() {
        etMaterialNum.setEnabled(false);
        if (mRefData == null) {
            showMessage("请先在抬头界面输入相应的数据");
            return;
        }
        if (TextUtils.isEmpty(mRefData.workCode)) {
            showMessage("请先在抬头界面选择工厂");
            return;
        }
        if (TextUtils.isEmpty(mRefData.voucherDate)) {
            showMessage("请先在抬头界面选择取样日期");
            return;
        }
        etMaterialNum.setEnabled(true);
    }


    private void loadMaterialInfo(String materialNum, String batchFlag) {
        if (!etMaterialNum.isEnabled())
            return;
        if (TextUtils.isEmpty(materialNum)) {
            showMessage("物料编码为空,请重新输入");
            return;
        }
        clearAllUI();
        etMaterialNum.setText(materialNum);
        etBatchFlag.setText(batchFlag);
        mPresenter.getTransferSingleInfo(mRefData.bizType, materialNum,
                Global.USER_ID, mRefData.workId, mRefData.invId, mRefData.recWorkId,
                mRefData.recInvId, batchFlag, "", -1);
    }


    @Override
    public void bindCommonCollectUI(ReferenceEntity refData, String batchFlag) {
        RefDetailEntity data = refData.billDetailList.get(0);
        isOpenBatchManager = true;
        etBatchFlag.setEnabled(true);
        manageBatchFlagStatus(etBatchFlag, data.batchManagerStatus);
        //刷新UI
        etMaterialNum.setTag(data.materialId);
        tvMaterialDesc.setText(data.materialDesc);
        tvMaterialGroup.setText(data.materialGroup);
        etBatchFlag.setText(!TextUtils.isEmpty(data.batchFlag) ? data.batchFlag : batchFlag);
        mHistoryDetailList = refData.billDetailList;
    }

    @Override
    public void loadTransferSingleInfoFail(String message) {
        showMessage(message);
    }


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.quantity = getString(etQuantity);
        result.materialNum = getString(etMaterialNum);
        result.materialId = CommonUtil.Obj2String(etMaterialNum.getTag());
        return result;
    }

    /**
     * 获取报检单数据
     */
    private void loadDeclarationRef() {
        String materialNum = getString(etMaterialNum);
        String batchFlag = getString(etBatchFlag);
        if (TextUtils.isEmpty(materialNum)) {
            showMessage("请先输入物资编码");
            return;
        }

    }

    @Override
    public void saveCollectedDataSuccess(String message) {

    }

    @Override
    public void saveCollectedDataFail(String message) {

    }

    @Override
    public void _onPause() {
        super._onPause();
        clearAllUI();
    }

    private void clearAllUI() {
        clearCommonUI(etMaterialNum, tvMaterialDesc, tvMaterialGroup, etBatchFlag, etRemark);
        if (spDeclarationRef.getAdapter() != null) {
            spDeclarationRef.setSelection(0);
        }
    }
}
