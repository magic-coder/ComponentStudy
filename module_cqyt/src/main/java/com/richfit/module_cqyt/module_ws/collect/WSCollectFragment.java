package com.richfit.module_cqyt.module_ws.collect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectFragment;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.adapter.InspectInfoAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
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

    /*单条缓存数据列表*/
    List<RefDetailEntity> mHistoryDetailList;
    /*报检单库存*/
    List<InventoryEntity> mDatas;
    InspectInfoAdapter mAdapter;

    /**
     * 处理扫描
     *
     * @param type
     * @param list
     */
    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        super.handleBarCodeScanResult(type, list);
        if (list != null && list.length > 12) {
            if (!etMaterialNum.isEnabled()) {
                showMessage("请先在抬头界面获取相关数据");
                return;
            }
            final String materialNum = list[Global.MATERIAL_POS];
            final String batchFlag = list[Global.BATCHFALG_POS];
            loadMaterialInfo(materialNum, batchFlag);
        }
    }

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
            loadMaterialInfo(materialNum, getString(etBatchFlag));
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
        if (TextUtils.isEmpty(batchFlag)) {
            showMessage("批次为空，请重新输入");
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
        //备注
        etRemark.setText(data.remark);
        mHistoryDetailList = refData.billDetailList;
    }

    @Override
    public void loadTransferSingleInfoFail(String message) {
        showMessage(message);
    }

    //加载单条缓存结束
    @Override
    public void loadTransferSingleInfoComplete() {
        String materialNum = getString(etMaterialNum);
        String bachFlag = getString(etBatchFlag);
        if (TextUtils.isEmpty(materialNum)) {
            showMessage("未获取到物料编码");
            return;
        }
        if (TextUtils.isEmpty(bachFlag)) {
            showMessage("未获取到批次");
            return;
        }
        //加载报检单列表
        mPresenter.getInspectionInfo(mBizType, materialNum, Global.USER_ID, mRefData.workCode, null);
    }

    @Override
    public void showInventory(List<InventoryEntity> list) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
        mDatas.addAll(list);
        if (mAdapter == null) {
            mAdapter = new InspectInfoAdapter(mActivity, R.layout.item_simple_sp, mDatas);
            spDeclarationRef.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadInventoryFail(String message) {
        showMessage(message);
        if (mDatas != null) {
            mDatas.clear();
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadInventoryComplete() {
        //自动匹配缓存中的报检单
        if (mHistoryDetailList != null) {
            RefDetailEntity item = mHistoryDetailList.get(0);
            UiUtil.setSelectionForLocation(mDatas, item.inspectionNum, spDeclarationRef);
        }
    }

    @Override
    public void showOperationMenuOnCollection(final String companyCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("提示");
        builder.setMessage("您真的需要保存数据吗?点击确定将保存数据.");
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            saveCollectedData();
        });
        builder.show();
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (!etMaterialNum.isEnabled()) {
            showMessage("请先获取物料信息");
            return false;
        }

        if (TextUtils.isEmpty(getString(etMaterialNum))) {
            showMessage("物料编码为空");
            return false;
        }

        if (TextUtils.isEmpty(getString(etBatchFlag))) {
            showMessage("请输入批次");
            return false;
        }

        if (mDatas == null || mDatas.size() <= 0) {
            showMessage("请先获取报检单数据");
            return false;
        }

        String quantity = getString(etQuantity);
        if (TextUtils.isEmpty(quantity)) {
            showMessage("请输入数量");
            return false;
        }

        if (Float.valueOf(quantity) < 0.0f) {
            showMessage("输入数量不合理");
            return false;
        }

        return true;
    }


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = new ResultEntity();
        result.quantity = getString(etQuantity);
        result.materialNum = getString(etMaterialNum);
        result.materialId = CommonUtil.Obj2String(etMaterialNum.getTag());
        result.remark = getString(etRemark);
        result.businessType = mBizType;
        result.batchFlag = getString(etBatchFlag);
        result.userId = Global.USER_ID;
        result.voucherDate = mRefData.voucherDate;
        InventoryEntity item = mDatas.get(spDeclarationRef.getSelectedItemPosition());
        result.arrivalDate = item.arrivalDate;
        result.contractNum = item.contractNum;
        result.inspectionDate = item.inspectionDate;
        result.inspectionNum = item.inspectionNum;
        result.invCode = item.invCode;
        result.invName = item.invName;
        result.planNum = item.planNum;
        result.supplierNum = item.supplierNum;
        result.supplierDesc = item.supplierDesc;
        result.workCode = item.workCode;
        result.workName = item.workName;
        result.modifyFlag = "N";
        return result;
    }


    @Override
    public void saveCollectedDataSuccess(String message) {
        showMessage(message);
        //清除
        etQuantity.setText("");
    }

    @Override
    public void saveCollectedDataFail(String message) {
        showMessage(message);
    }

    @Override
    public void _onPause() {
        super._onPause();
        clearAllUI();
    }

    private void clearAllUI() {
        clearCommonUI(etMaterialNum, tvMaterialDesc, tvMaterialGroup, etBatchFlag, etRemark,etQuantity);
    }
}
