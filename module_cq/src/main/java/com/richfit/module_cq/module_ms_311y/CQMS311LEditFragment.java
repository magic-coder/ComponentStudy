package com.richfit.module_cq.module_ms_311y;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzyk.base_ms_edit.BaseMSEditFragment;
import com.richfit.sdk_wzyk.base_ms_edit.imp.MSEditPresenterImp;

/**
 * Created by monday on 2017/4/14.
 */

public class CQMS311LEditFragment extends BaseMSEditFragment<MSEditPresenterImp> {

    RichEditText etSendLocation;
    EditText etSpecialInvFlag;
    EditText etSpecialInvNum;

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_ms311l_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MSEditPresenterImp(mActivity);
    }

    @Override
    public void initEvent() {
        etSendLocation.setOnRichEditTouchListener((view, locationCombine) -> {
            hideKeyboard(view);
            mPresenter.getTransferInfoSingle(mRefData.refCodeId, mRefData.refType,
                    mRefData.bizType, mRefLineId, getString(tvBatchFlag), locationCombine
                    , "", -1, Global.USER_ID);
        });
    }

    @Override
    public void initView() {
        etSendLocation = (RichEditText) mView.findViewById(R.id.et_send_location);
        etSpecialInvFlag = (EditText) mView.findViewById(R.id.et_special_inv_flag);
        etSpecialInvNum = (EditText) mView.findViewById(R.id.et_special_inv_num);
        spLocation.setEnabled(false);
        llRecLocation.setVisibility(View.VISIBLE);
        llRecBatch.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        etSendLocation.setText(mSelectedLocationCombine);
        etSpecialInvFlag.setText(mSpecialInvFlag);
        etSpecialInvNum.setText(mSpecialInvNum);

        //获取缓存
        String locationCombine = mSelectedLocationCombine;
        mPresenter.getTransferInfoSingle(mRefData.refCodeId, mRefData.refType,
                mRefData.bizType, mRefLineId, getString(tvBatchFlag), locationCombine
                , "", -1, Global.USER_ID);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        //检查是否合理，可以保存修改后的数据
        if (TextUtils.isEmpty(getString(etQuantity))) {
            showMessage("请输入入库数量");
            return false;
        }

        if (Float.parseFloat(getString(etQuantity)) <= 0.0f) {
            showMessage("输入出库数量不合理,请重新输入");
            return false;
        }

        if(TextUtils.isEmpty(getString(etSendLocation))) {
            showMessage("请输入的发出仓位");
            return false;
        }

        final String specialInvFlag = getString(etSpecialInvFlag);
        final String specialInvNum = getString(etSpecialInvNum);
        if(!TextUtils.isEmpty(specialInvFlag) && "K".equalsIgnoreCase(specialInvFlag)
                && TextUtils.isEmpty(specialInvNum)) {
            showMessage("请先输入特殊库存编号");
            return false;
        }


        //是否满足本次录入数量+累计数量-上次已经录入的出库数量<=应出数量
        float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        float totalQuantityV = CommonUtil.convertToFloat(getString(tvTotalQuantity), 0.0f);
        float collectedQuantity = CommonUtil.convertToFloat(mQuantity, 0.0f);
        //修改后的出库数量
        float quantityV = CommonUtil.convertToFloat(getString(etQuantity), 0.0f);
        float residualQuantity = totalQuantityV - collectedQuantity + quantityV;//减去已经录入的数量
        if (Float.compare(residualQuantity, actQuantityV) > 0.0f) {
            showMessage("输入移库数量有误");
            etQuantity.setText("");
            return false;
        }

        mQuantity = quantityV + "";
        mTotalQuantity = residualQuantity;
        return true;
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.location = getString(etSendLocation);
        result.specialInvFlag = getString(etSpecialInvFlag);
        result.specialInvNum = getString(etSpecialInvNum);
        return result;
    }


    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.queryType = "01";
        return queryParam;
    }

}
