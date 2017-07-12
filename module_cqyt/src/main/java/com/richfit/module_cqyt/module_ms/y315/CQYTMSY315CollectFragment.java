package com.richfit.module_cqyt.module_ms.y315;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

import java.util.ArrayList;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * 注意这里使用的313的布局文件，因为长庆的移库都是在标准的基础上增加了一个件数的字段
 * Created by monday on 2017/6/30.
 */

public class CQYTMSY315CollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    EditText etQuantityCustom;
    TextView tvTotalQuantityCustom;

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_msy315_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        TextView tvInvName = (TextView) mView.findViewById(R.id.tv_inv_name);
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        tvTotalQuantityCustom = (TextView) mView.findViewById(R.id.cqyt_tv_total_quantity_custom);
        tvWorkName.setText("工厂");
        tvActQuantityName.setText("应收数量");
        tvInvName.setText("接收库位");
        tvLocationName.setText("接收仓位");
        tvBatchFlagName.setText("接收批次");
    }


    @Override
    public void initEvent() {
        super.initEvent();
        /*监听上架仓位点击事件(注意如果是必检物资该监听无效)*/
        etLocation.setOnRichAutoEditTouchListener((view, location) -> getTransferSingle(getString(etBatchFlag), location));
    }

    @Override
    public void initData() {

    }

    @Override
    public void showInvs(ArrayList<InvEntity> list) {
       super.showInvs(list);
        if(!isNLocation) {
            ArrayList<String> datas = new ArrayList<>();
            for (InvEntity item : list) {
                datas.add(item.invCode);
            }
            RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
            UiUtil.setSelectionForSp(datas, lineData.invCode, spInv);
        }
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
            result.unit = TextUtils.isEmpty(lineData.recordUnit) ? lineData.materialUnit : lineData.recordUnit;
            result.unitRate = Float.compare(lineData.unitRate, 0.0f) == 0 ? 1.f : lineData.unitRate;
            result.invId = mInvDatas.get(spInv.getSelectedItemPosition()).invId;
            result.materialId = lineData.materialId;
            result.location = isNLocation ? "barcode" : getString(etLocation);
            result.batchFlag = getString(etBatchFlag);
            result.quantity = getString(etQuantity);
            result.modifyFlag = "N";
            result.refDoc = lineData.refDoc;
            result.refDocItem = lineData.refDocItem;
            result.supplierNum = mRefData.supplierNum;
            result.quantityCustom = getString(etQuantityCustom);
            emitter.onNext(result);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER).compose(TransformerHelper.io2main())
                .subscribe(result -> mPresenter.uploadCollectionDataSingle(result));
    }

    @Override
    public void saveCollectedDataSuccess() {
        super.saveCollectedDataSuccess();
        float quantityCustomQ = CommonUtil.convertToFloat(getString(etQuantityCustom), 0.0F);
        float totalQuantityCustomQ = CommonUtil.convertToFloat(getString(tvTotalQuantityCustom), 0.0F);
        tvTotalQuantityCustom.setText(String.valueOf(quantityCustomQ + totalQuantityCustomQ));
    }

    @Override
    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etQuantityCustom, tvTotalQuantityCustom);
    }
}
