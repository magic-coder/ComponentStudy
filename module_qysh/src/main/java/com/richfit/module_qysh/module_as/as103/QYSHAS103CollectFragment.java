package com.richfit.module_qysh.module_as.as103;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHAS103CollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        super.initVariable(savedInstanceState);
        //不上架，不能输入仓位，单条缓存由库存地点触发
        isNLocation = true;
        //强制不打开仓储类型
        isOpenLocationType = false;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        super.initView();
        //没有库存地点
        View inv = mView.findViewById(R.id.ll_inv);
        if (inv != null) {
            inv.setVisibility(View.GONE);
        }
        llBatchFlag.setVisibility(View.GONE);
        //没有上架仓位
        llLocation.setVisibility(View.GONE);
        //没有仓位数量
        llLocationQuantity.setVisibility(View.GONE);
    }

    //注意这里没有库存地点
    @Override
    public void bindCommonCollectUI() {
        mSelectedRefLineNum = mRefLines.get(spRefLine.getSelectedItemPosition());
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        //再次复位批次管理标识
        isOpenBatchManager = false;
        etQuantity.setText("");
        //物资描述
        tvMaterialDesc.setText(lineData.materialDesc);
        tvMaterialUnit.setText(lineData.unit);
        //特殊库存标识
        tvSpecialInvFlag.setText(lineData.specialInvFlag);
        //检验批数量
        tvInsLotQuantity.setText(lineData.orderQuantity);
        //工厂
        tvWork.setText(lineData.workName);
        //应收数量
        tvActQuantity.setText(lineData.actQuantity);
    }

}
