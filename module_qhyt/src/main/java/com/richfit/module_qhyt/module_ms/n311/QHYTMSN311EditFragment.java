package com.richfit.module_qhyt.module_ms.n311;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzyk.base_msn_edit.BaseMSNEditFragment;
import com.richfit.sdk_wzyk.base_msn_edit.imp.MSNEditPresenterImp;

/**
 * Created by monday on 2017/2/17.
 */

public class QHYTMSN311EditFragment extends BaseMSNEditFragment<MSNEditPresenterImp> {

    String specialConvert = "N";

    /**
     * 加载发出库存完毕
     */
    @Override
    public void loadInventoryComplete() {
       //do nothing
    }

    @Override
    public void showOperationMenuOnCollection(final String companyCode) {
        //每一次保存之前需要重置该字段
        specialConvert = "N";
        boolean isTurn = false;
        final int position = spSendLoc.getSelectedItemPosition();
        if (position >= 0 && !TextUtils.isEmpty(mInventoryDatas.get(position).specialInvFlag)
                && !TextUtils.isEmpty(mInventoryDatas.get(position).specialInvNum)) {
            isTurn = true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("温馨提示");
        String message = isTurn ? "检测到有寄售库存,您是否要进行寄售转自有" : "您真的确定要保存本次修改的数据?";
        builder.setMessage(message);
        //  第一个按钮
        builder.setPositiveButton("直接修改", (dialog, which) -> {
            dialog.dismiss();
            specialConvert = "N";
            saveCollectedData();
        });
        if (isTurn) {
            builder.setNeutralButton("寄售转自有", (dialog, which) -> {
                dialog.dismiss();
                specialConvert = "Y";
                saveCollectedData();
            });
        }
        //  第三个按钮
        builder.setNegativeButton("取消修改", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }


    @Override
    public void initPresenter() {
        mPresenter = new MSNEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        final String recLocation = getString(autoRecLoc);
        if(TextUtils.isEmpty(recLocation) || recLocation.length() > 10) {
            showMessage("您输入的接收仓位不合理");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.specialConvert = specialConvert;
        return result;
    }
}
