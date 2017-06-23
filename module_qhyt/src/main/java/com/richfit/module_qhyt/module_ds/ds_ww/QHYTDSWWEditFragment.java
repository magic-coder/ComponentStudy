package com.richfit.module_qhyt.module_ds.ds_ww;

import android.text.TextUtils;

import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

/**
 * Created by monday on 2017/1/20.
 */

public class QHYTDSWWEditFragment extends BaseDSEditFragment<DSEditPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new DSEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {
        super.initData();
        RefDetailEntity entity = mRefData.billDetailList.get(mPosition);
        tvRefLineNum.setText(TextUtils.isEmpty(entity.batchFlag) ? entity.materialNum :
                entity.materialNum + "_" + entity.batchFlag);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        //检查是否合理，可以保存修改后的数据
        if (TextUtils.isEmpty(getString(etQuantity))) {
            showMessage("请输入实发数量");
            return false;
        }

        if (TextUtils.isEmpty(getString(tvInvQuantity))) {
            showMessage("请先获取库存");
            return false;
        }

        if (Float.parseFloat(getString(etQuantity)) <= 0.0f) {
            showMessage("输入出库数量不合理,请重新输入");
            return false;
        }
        /*lastFlag 委外出库行数量判断标识如果 lastFlag = 'X'  则累计录入数量不能大于应发数量*/
        RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        float quantityV = CommonUtil.convertToFloat(getString(etQuantity), 0.0f);
        if (lineData != null) {
            if (!"X".equalsIgnoreCase(lineData.lastFlag)) {
                //该仓位的库存数量
                final float invQuantityV = CommonUtil.convertToFloat(getString(tvInvQuantity), 0.0f);
                if (Float.compare(quantityV, invQuantityV) > 0.0f) {
                    showMessage("输入数量大于库存数量，请重新输入");
                    etQuantity.setText("");
                    return false;
                }
                return true;
            }
        }

        //是否满足本次录入数量+累计数量-上次已经录入的出库数量<=应出数量
        float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        float totalQuantityV = CommonUtil.convertToFloat(getString(tvTotalQuantity), 0.0f);
        float collectedQuantity = CommonUtil.convertToFloat(mQuantity, 0.0f);
        float residualQuantity = totalQuantityV - collectedQuantity + quantityV;//减去已经录入的数量
        if (Float.compare(residualQuantity, actQuantityV) > 0.0f) {
            showMessage("输入实发数量有误");
            etQuantity.setText("");
            return false;
        }

        mQuantity = quantityV + "";
        mTotalQuantity = residualQuantity;
        return true;
    }
}
