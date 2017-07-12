package com.richfit.module_qhyt.module_ds.dsn;


import android.text.TextUtils;

import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzck.base_dsn_edit.BaseDSNEditFragment;
import com.richfit.sdk_wzck.base_dsn_edit.imp.DSNEditPresenterImp;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by monday on 2017/3/27.
 */

public class QHYTDSNEditFragment extends BaseDSNEditFragment<DSNEditPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new DSNEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

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
            ResultEntity result = new ResultEntity();
            result.businessType = mRefData.bizType;
            result.voucherDate = mRefData.voucherDate;
            result.moveType = mRefData.moveType;
            result.userId = Global.USER_ID;
            result.workId = mRefData.workId;
            result.locationId = mLocationId;
            result.invId = CommonUtil.Obj2String(tvInv.getTag());
            result.recWorkId = mRefData.recWorkId;
            result.recInvId = mRefData.recInvId;
            result.materialId = tvMaterialNum.getTag().toString();
            result.batchFlag = getString(tvBatchFlag);
            result.costCenter = mRefData.costCenter;
            result.projectNum = mRefData.projectNum;
            final int position = spLocation.getSelectedItemPosition();
            result.location = mInventoryDatas.get(position).location;
            result.specialInvFlag = mInventoryDatas.get(position).specialInvFlag;
            result.specialInvNum = mInventoryDatas.get(position).specialInvNum;
            result.specialConvert = !TextUtils.isEmpty(result.specialInvFlag) && !TextUtils.isEmpty(result.specialInvNum) ?
                    "Y" : "N";
            result.quantity = getString(etQuantity);
            result.invType = "1";
            result.invFlag = mRefData.invFlag;
            result.zzzdy9 = mRefData.zzzdy9;
            result.zzzxlb = mRefData.zzzxlb;
            result.zzzxnr = mRefData.zzzxnr;
            result.modifyFlag = "Y";
            emitter.onNext(result);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER)
                .compose(TransformerHelper.io2main())
                .subscribe(result -> mPresenter.uploadCollectionDataSingle(result));
    }
}
