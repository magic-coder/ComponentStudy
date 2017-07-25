package com.richfit.module_xngd.module_as;

import android.text.TextUtils;
import android.widget.EditText;

import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzrk.base_asn_collect.BaseASNCollectFragment;
import com.richfit.sdk_wzrk.base_asn_collect.imp.ASNCollectPresenterImp;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by monday on 2017/6/23.
 */

public class XNGDASNCollectFragment extends BaseASNCollectFragment<ASNCollectPresenterImp> {

    EditText etMoney;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_asn_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        etMoney = (EditText) mView.findViewById(R.id.xngd_et_money);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initDataLazily() {
        etMaterialNum.setEnabled(false);
        if (mRefData == null) {
            showMessage("请先在抬头界面输入必要的数据");
            return;
        }

        if ("1".equals(mRefData.invType) && TextUtils.isEmpty(mRefData.projectNum)) {
            showMessage("请现在抬头输入项目编号");
            return;
        }
        super.initDataLazily();
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (!isNLocation && TextUtils.isEmpty(getString(etLocation))) {
            showMessage("请先输入上架仓位");
            return false;
        }
        if (!isNLocation && getString(etLocation).split("\\.").length != 4) {
            showMessage("您输入的仓位格式不正确");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }

    public void saveCollectedData() {
        if (!checkCollectedDataBeforeSave()) {
            return;
        }
        Flowable.create((FlowableOnSubscribe<ResultEntity>) emitter -> {
            ResultEntity result = new ResultEntity();
            InventoryQueryParam param = provideInventoryQueryParam();
            result.businessType = mRefData.bizType;
            result.voucherDate = mRefData.voucherDate;
            result.moveType = mRefData.moveType;
            result.userId = Global.USER_ID;
            result.workId = mRefData.workId;
            result.invId = mInvs.get(spInv.getSelectedItemPosition()).invId;
            result.recWorkId = mRefData.recWorkId;
            result.recInvId = mRefData.recInvId;
            result.materialId = etMaterialNum.getTag().toString();
            result.batchFlag = !isOpenBatchManager ? "20170101" : getString(etBatchFlag);
            result.location = !isNLocation ? getString(etLocation) : "barcode";
            result.quantity = getString(etQuantity);
            //将抬头的库存类型和项目编号给服务器
            result.specialInvFlag = getString(tvSpecialInvFlag);
            result.projectNum = mRefData.projectNum;
            result.supplierId = mRefData.supplierId;
            result.invType = param.invType;
            result.modifyFlag = "N";
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

    @Override
    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etMoney);
    }
}
