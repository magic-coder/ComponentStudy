package com.richfit.module_xngd.module_as;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzrk.base_asn_edit.BaseASNEditFragment;
import com.richfit.sdk_wzrk.base_asn_edit.imp.ASNEditPresenterImp;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by monday on 2017/6/23.
 */

public class XNGDASNEditFragment extends BaseASNEditFragment<ASNEditPresenterImp> {

    public static final String EXTRA_MONEY_KEY = "extra_money";

    EditText etMoney;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_asn_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASNEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        etMoney = (EditText) mView.findViewById(R.id.xngd_et_money);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle arguments = getArguments();
        if (arguments != null) {
            String money = arguments.getString(EXTRA_MONEY_KEY);
            etMoney.setText(money);
        }
    }

    @Override
    public void initDataLazily() {

    }

    public void saveCollectedData() {
        if (!checkCollectedDataBeforeSave()) {
            return;
        }
        Flowable.create((FlowableOnSubscribe<ResultEntity>) emitter -> {
            ResultEntity result = new ResultEntity();
            InventoryQueryParam param = provideInventoryQueryParam();
            result.invType = param.invType;
            result.businessType = mRefData.bizType;
            result.voucherDate = mRefData.voucherDate;
            result.moveType = mRefData.moveType;
            result.userId = Global.USER_ID;
            result.workId = mRefData.workId;
            result.invId = tvInv.getTag().toString();
            result.recWorkId = mRefData.recWorkId;
            result.recInvId = mRefData.recInvId;
            result.materialId = tvMaterialNum.getTag().toString();
            result.batchFlag = !isOpenBatchManager ? "20170101" : getString(tvBatchFlag);
            result.location = isLocation ? getString(etLocation) : "barcode";
            result.quantity = getString(etQuantity);
            result.modifyFlag = "Y";
            result.money = getString(etMoney);
            emitter.onNext(result);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER)
                .compose(TransformerHelper.io2main())
                .subscribe(result -> mPresenter.uploadCollectionDataSingle(result));

    }

    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = "1";
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", mRefData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum", mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }

}
