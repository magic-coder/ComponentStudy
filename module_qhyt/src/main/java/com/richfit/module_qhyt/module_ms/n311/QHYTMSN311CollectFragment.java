package com.richfit.module_qhyt.module_ms.n311;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzyk.base_msn_collect.BaseMSNCollectFragment;
import com.richfit.sdk_wzyk.base_msn_collect.imp.MSNCollectPresenterImp;

/**
 * 工厂内311移库，不需要接收批次。接收仓位默认与发出仓位一直，允许修改，但是不提供拉下选择和搜索。
 * 增加用户控制是否寄售转自有的权限
 * Created by monday on 2017/2/16.
 */

public class QHYTMSN311CollectFragment extends BaseMSNCollectFragment<MSNCollectPresenterImp> {

    //默认都是进行寄售转自有的
    private String specialConvert = "N";


    @Override
    public void initPresenter() {
        mPresenter = new MSNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        //工厂内移库不需要接收批次
        llRecBatch.setVisibility(View.GONE);
    }

    @Override
    public void initData() {

    }

    /**
     * 如果打开了WM那么需要检查仓库号是否一致。
     * 对于工厂内的转储，没有接收工厂，那么接收工厂Id默认为发出工厂
     */
    @Override
    protected void checkWareHouseNum(int position) {
        if (position <= 0) {
            return;
        }
        final String workId = mRefData.workId;
        final String invCode = mSendInvs.get(position).invCode;
        final String recInvCode = mRefData.recInvCode;
        if (isOpenWM) {
            //没有打开WM，不需要检查ERP仓库号是否一致
            isWareHouseSame = true;
            return;
        }
        if (TextUtils.isEmpty(workId)) {
            showMessage("工厂为空");
            return;
        }
        if (TextUtils.isEmpty(invCode)) {
            showMessage("发出库位为空");
            return;
        }

        if (TextUtils.isEmpty(recInvCode)) {
            showMessage("接收库位为空");
            return;
        }
        mPresenter.checkWareHouseNum(isOpenWM, workId, invCode, workId, recInvCode, getOrgFlag());
    }

    @Override
    protected boolean checkHeaderData() {
        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先选择工厂");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.invId)) {
            showMessage("请先选择发出库位");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.recInvId)) {
            showMessage("请先选择接收库位");
            return false;
        }
        return true;
    }

    /**
     * 加载发出库存完毕,不需要加载接收仓位的库存和历史
     */
    @Override
    public void loadInventoryComplete() {
        //do nothing
    }


    @Override
    protected void getTransferSingle(int position) {
        //调用父类方法，将缓存中的发出仓位数量匹配出来
        super.getTransferSingle(position);
        //接收仓位默认为发出仓位
//        autoRecLoc.setText(mInventoryDatas.get(position).location);
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
        //发出工厂
        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先选择工厂");
            return false;
        }
        //检查发出批次
        if (mHistoryDetailList != null) {
            RefDetailEntity lineData = mHistoryDetailList.get(0);
            manageBatchFlagStatus(etSendBatchFlag, lineData.batchManagerStatus);
            if (isOpenBatchManager && TextUtils.isEmpty(getString(etSendBatchFlag))) {
                showMessage("批次为空");
                return false;
            }
        }

        if (spSendLoc.getSelectedItemPosition() <= 0) {
            showMessage("请先选择发出仓位");
            return false;
        }
        //检查接收仓位
        final String recLocation = getString(autoRecLoc);
        if (TextUtils.isEmpty(recLocation) || recLocation.length() > 10) {
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

    @Override
    protected boolean getWMOpenFlag() {
        return false;
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
