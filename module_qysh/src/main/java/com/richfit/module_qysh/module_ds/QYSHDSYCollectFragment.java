package com.richfit.module_qysh.module_ds;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

/**
 * 如果是寄售（带k）必须有接收方，如果不是寄售库存，那么281页必须输入接收方
 * Created by monday on 2017/11/2.
 */

public class QYSHDSYCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {
    //默认都是进行寄售转自有的
    private String specialConvert = "N";


    @Override
    protected void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        super.initView();
        View batchFlag = mView.findViewById(R.id.ll_batch_flag);
        if (batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }


    @Override
    public void showOperationMenuOnCollection(final String companyCode) {
        //每一次保存之前需要重置该字段
        specialConvert = "N";
        boolean isTurn = false;
        final int position = spLocation.getSelectedItemPosition();
        if (position >= 0 && mInventoryDatas !=null
                && !TextUtils.isEmpty(mInventoryDatas.get(position).specialInvFlag)
                && !TextUtils.isEmpty(mInventoryDatas.get(position).specialInvNum)){
            isTurn = true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("温馨提示");
        String message = isTurn ? "检测到有寄售库存,您是否要进行寄售转自有" : "您真的确定要保存本次采集的数据?";
        builder.setMessage(message);
        //  第一个按钮
        builder.setPositiveButton("直接保存", (dialog, which) -> {
            dialog.dismiss();
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
        builder.setNegativeButton("取消保存", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (mRefData == null) {
            showMessage("请先获取单据信息");
            return false;
        }
        final int position = spLocation.getSelectedItemPosition();
        if (position >= 0 && mInventoryDatas != null) {
            String specialInvFlag = mInventoryDatas.get(position).specialInvFlag;
            if ("K".equalsIgnoreCase(specialInvFlag) && TextUtils.isEmpty(mRefData.deliveryTo)) {
                showMessage("请先在抬头界面输入接收方!!!");
                return false;
            }
        }

        if ("281".equalsIgnoreCase(mRefData.sapMoveType) && TextUtils.isEmpty(mRefData.deliveryTo)) {
            showMessage("请先在抬头界面输入接收方!!!");
            return false;
        }

        return super.checkCollectedDataBeforeSave();

    }


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.batchFlag = null;
        result.specialConvert = specialConvert;
        return result;
    }
}
