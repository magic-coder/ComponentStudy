package com.richfit.module_cqyt.module_ms.ubsto;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by monday on 2017/7/5.
 */

public class CQYTUbstoCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {

    EditText etQuantityCustom;
    TextView tvTotalQuantityCustom;

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_ubsto_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        tvTotalQuantityCustom = (TextView) mView.findViewById(R.id.tv_ref_line_num_name);
        TextView tvBatchFlagName = (TextView) mView.findViewById(R.id.tv_batch_flag_name);
        tvBatchFlagName.setText("发出批次");
    }

    @Override
    public void initData() {

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
            result.invId = mInvDatas.get(spInv.getSelectedItemPosition()).invId;
            result.materialId = lineData.materialId;
            result.batchFlag = getString(etBatchFlag);
            result.quantity = getString(etQuantity);
            result.unit = TextUtils.isEmpty(lineData.recordUnit) ? lineData.materialUnit : lineData.recordUnit;
            result.unitRate = Float.compare(lineData.unitRate, 0.0f) == 0 ? 1.f : lineData.unitRate;
            //库存相关的字段回传
            int locationPos = spLocation.getSelectedItemPosition();
            result.location = mInventoryDatas.get(locationPos).location;
            result.specialInvFlag = mInventoryDatas.get(locationPos).specialInvFlag;
            result.specialInvNum = mInventoryDatas.get(locationPos).specialInvNum;
            result.supplierNum = mRefData.supplierNum;
            //寄售转自有的标识
            result.specialConvert = !TextUtils.isEmpty(result.specialInvFlag) && !TextUtils.isEmpty(result.specialInvNum) ?
                    "Y" : "N";
            result.invType = param.invType;
            result.modifyFlag = "N";
            result.quantityCustom = getString(etQuantityCustom);
            emitter.onNext(result);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER).compose(TransformerHelper.io2main())
                .subscribe(result -> mPresenter.uploadCollectionDataSingle(result));
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
