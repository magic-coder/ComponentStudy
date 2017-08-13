package com.richfit.module_cqyt.module_ms.y313;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.common_lib.utils.ArithUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzyk.base_ms_collect.BaseMSCollectFragment;
import com.richfit.sdk_wzyk.base_ms_collect.imp.MSCollectPresenterImp;

import java.util.ArrayList;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by monday on 2017/6/30.
 */

public class CQYTMSY313CollectFragment extends BaseMSCollectFragment<MSCollectPresenterImp> {

    EditText etQuantityCustom;
    TextView tvTotalQuantityCustom;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        mLineNumForFilter = list[list.length - 1];
        super.handleBarCodeScanResult(type, list);
    }

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_msy313_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        tvTotalQuantityCustom = (TextView) mView.findViewById(R.id.cqyt_tv_total_quantity_custom);
        //打开接收批次，默认与发出批次一致，不允许修改
        llRecBatch.setVisibility(View.VISIBLE);
        etRecBatchFlag.setEnabled(false);
        //发出工厂改为工厂
        sendWorkName.setText("工厂");
    }

    @Override
    public void initData() {

    }

    /**
     * 设置单据行信息之前，过滤掉
     *
     * @param refLines
     */
    @Override
    public void setupRefLineAdapter(ArrayList<String> refLines) {
        if (!TextUtils.isEmpty(mLineNumForFilter)) {
            //过滤掉重复行号
            ArrayList<String> lines = new ArrayList<>();
            for (String refLine : refLines) {
                if(refLine.equalsIgnoreCase(mLineNumForFilter)) {
                    lines.add(refLine);
                }
            }
            if(lines.size() == 0) {
                showMessage("未获取到条码的单据行信息");
            }
            super.setupRefLineAdapter(lines);
            return;
        }
        //如果单据中没有过滤行信息那么直接显示所有的行信息
        super.setupRefLineAdapter(refLines);
    }


    @Override
    public void bindCommonCollectUI() {
        mSelectedRefLineNum = mRefLines.get(spRefLine.getSelectedItemPosition());
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        if (!TextUtils.isEmpty(lineData.dangerFlag)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("温馨提示").setMessage(lineData.dangerFlag)
                    .setPositiveButton("继续采集", (dialog, which) -> {
                        super.bindCommonCollectUI();
                        etRecBatchFlag.setText(getString(etSendBatchFlag));
                        dialog.dismiss();
                    })
                    .setNegativeButton("放弃采集", (dialog, which) -> {
                        dialog.dismiss();
                    }).show();
            return;
        }
        super.bindCommonCollectUI();
        //接收批次与接收批次一致
        etRecBatchFlag.setText(getString(etSendBatchFlag));
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
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
            RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
            ResultEntity result = new ResultEntity();
            InventoryQueryParam param = provideInventoryQueryParam();
            result.invType = param.invType;
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
            result.recWorkId = lineData.recWorkId;
            result.recInvId = lineData.recInvId;
            result.invId = mInvDatas.get(spSendInv.getSelectedItemPosition()).invId;
            result.materialId = lineData.materialId;
            result.batchFlag = CommonUtil.toUpperCase(getString(etSendBatchFlag));
            result.recBatchFlag = CommonUtil.toUpperCase(getString(etRecBatchFlag));
            result.recLocation = CommonUtil.toUpperCase(getString(etRecLoc));
            result.unit = TextUtils.isEmpty(lineData.recordUnit) ? lineData.materialUnit : lineData.recordUnit;
            result.unitRate = Float.compare(lineData.unitRate, 0.0f) == 0 ? 1.f : lineData.unitRate;
            result.quantity = getString(etQuantity);
            int locationPos = spSendLoc.getSelectedItemPosition();
            result.location = mInventoryDatas.get(locationPos).location;
            result.specialInvFlag = mInventoryDatas.get(locationPos).specialInvFlag;
            result.specialInvNum = mInventoryDatas.get(locationPos).specialInvNum;
            result.specialConvert = !TextUtils.isEmpty(result.specialInvFlag) && !TextUtils.isEmpty(result.specialInvNum) ?
                    "Y" : "N";
            result.modifyFlag = "N";
            //件数
            result.quantityCustom = getString(etQuantityCustom);
            emitter.onNext(result);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER).compose(TransformerHelper.io2main())
                .subscribe(result -> mPresenter.uploadCollectionDataSingle(result));
    }

    @Override
    public void saveCollectedDataSuccess(String message) {
        super.saveCollectedDataSuccess(message);
        if (!cbSingle.isChecked()) {
            etQuantityCustom.setText("");
        }
        tvTotalQuantityCustom.setText(String.valueOf(ArithUtil.add(getString(etQuantityCustom),
                getString(tvTotalQuantityCustom))));
    }

    @Override
    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etQuantityCustom, tvTotalQuantityCustom);
    }
}
