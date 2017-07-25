package com.richfit.module_cqyt.module_cwtz;

import android.widget.EditText;

import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_cwtz.collect.LACollectFragment;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by monday on 2017/7/20.
 */

public class CQYTLACollectFragment extends LACollectFragment {

    EditText etQuantityCustom;

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_la_collect;
    }

    @Override
    public void initView() {
        super.initView();
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
    }


    @Override
    public void saveCollectedData() {
        if (!checkCollectedDataBeforeSave()) {
            return;
        }
        Flowable.create((FlowableOnSubscribe<ResultEntity>) emitter -> {
            ResultEntity result = new ResultEntity();
            result.businessType = mRefData.bizType;
            int position = spSpecialInv.getSelectedItemPosition();
            //如果没有打开，那么返回服务给出的默认批次
            result.batchFlag = !isOpenBatchManager ? CommonUtil.toUpperCase(mInventoryDatas.get(position).batchFlag)
                    : CommonUtil.toUpperCase(getString(etBatchFlag));
            result.workId = mRefData.workId;
            result.invId = mRefData.invId;
            result.materialId = CommonUtil.Obj2String(etMaterialNum.getTag());
            result.location = CommonUtil.toUpperCase(getString(etSendLocation));
            result.recLocation = CommonUtil.toUpperCase(getString(etRecLocation));
            result.quantity = getString(etRecQuantity);
            result.userId = Global.USER_ID;
            result.invType = mInventoryDatas.get(position).invType;
            result.invFlag = mInventoryDatas.get(position).invFlag;
            result.specialInvFlag = mInventoryDatas.get(position).specialInvFlag;
            result.specialInvNum = mInventoryDatas.get(position).specialInvNum;
            result.quantityCustom = getString(etQuantityCustom);
            emitter.onNext(result);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER).compose(TransformerHelper.io2main())
                .subscribe(result -> mPresenter.uploadCollectionDataSingle(result));
    }

}
