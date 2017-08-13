package com.richfit.module_cqyt.module_ms.y315;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.common_lib.utils.ArithUtil;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

import java.util.ArrayList;

/**
 * 注意这里使用的313的布局文件，因为长庆的移库都是在标准的基础上增加了一个件数的字段
 * Created by monday on 2017/6/30.
 */

public class CQYTMSY315CollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    EditText etQuantityCustom;
    TextView tvTotalQuantityCustom;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        mLineNumForFilter = list[list.length - 1];
        super.handleBarCodeScanResult(type, list);
    }

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


    /**
     * 设置单据行信息之前，过滤掉
     *
     * @param refLines
     */
    @Override
    public void setupRefLineAdapter(ArrayList<String> refLines) {
        if (!TextUtils.isEmpty(mLineNumForFilter)) {
            //过滤掉重复行号
            ArrayList<String> lines = new ArrayList<>();
            for (String refLine : refLines) {
                if(refLine.equalsIgnoreCase(mLineNumForFilter)) {
                    lines.add(refLine);
                }
            }
            if(lines.size() == 0) {
                showMessage("未获取到条码的单据行信息");
            }
            super.setupRefLineAdapter(lines);
            return;
        }
        //如果单据中没有过滤行信息那么直接显示所有的行信息
        super.setupRefLineAdapter(refLines);
    }

    @Override
    public void bindCommonCollectUI() {
        mSelectedRefLineNum = mRefLines.get(spRefLine.getSelectedItemPosition());
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        if (!TextUtils.isEmpty(lineData.dangerFlag)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("温馨提示").setMessage(lineData.dangerFlag)
                    .setPositiveButton("继续采集", (dialog, which) -> {
                        super.bindCommonCollectUI();
                        dialog.dismiss();
                    })
                    .setNegativeButton("放弃采集", (dialog, which) -> {
                        dialog.dismiss();
                    }).show();
            return;
        }
        super.bindCommonCollectUI();
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
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.quantityCustom = getString(etQuantityCustom);
        return result;
    }

    @Override
    public void saveCollectedDataSuccess(String message) {
        super.saveCollectedDataSuccess(message);
        if (!cbSingle.isChecked()) {
            etQuantityCustom.setText("");
        }
        tvTotalQuantityCustom.setText(String.valueOf(ArithUtil.add(getString(etQuantityCustom),
                getString(tvTotalQuantityCustom))));
    }

    @Override
    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etQuantityCustom, tvTotalQuantityCustom);
    }
}
