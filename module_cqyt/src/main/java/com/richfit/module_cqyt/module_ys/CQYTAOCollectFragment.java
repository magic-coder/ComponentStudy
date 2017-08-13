package com.richfit.module_cqyt.module_ys;


import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

import java.util.ArrayList;

/**
 * 注意该业务比较特别，每一个单据的所有的行明细都是由一行生成拆分出来的，这就意味着
 * 单据中所有的行的refLineId和lineNum都一致，所以不能将它作为唯一的表示来标识该行。
 * 这里采用的方式使用每一行明细的insLot字段来标识每一行的明细。
 * Created by monday on 2017/3/1.
 */

public class CQYTAOCollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    EditText etReturnQuantity;

    //移动原因
    Spinner spMoveCause;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        mLineNumForFilter = list[list.length - 1];
        super.handleBarCodeScanResult(type, list);
    }

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_ao_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        TextView tvRefLineNumName = (TextView) mView.findViewById(R.id.tv_ref_line_num_name);
        etReturnQuantity = mView.findViewById(R.id.et_return_quantity);
        spMoveCause = mView.findViewById(R.id.sp_move_cause);
        tvRefLineNumName.setText("检验批");
        llInsLostQuantity.setVisibility(View.VISIBLE);
        tvActQuantityName.setText("允许过账数量");
        tvQuantityName.setText("过账数量");
    }


    @Override
    public void initEvent() {
        super.initEvent();
        etLocation.setOnRichAutoEditTouchListener((view, location) -> getTransferSingle(getString(etBatchFlag), location));
    }

    @Override
    public void initData() {
        if (spMoveCause != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp,
                    getStringArray(R.array.cqyt_move_reasons));
            spMoveCause.setAdapter(adapter);
        }
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
                if (refLine.equalsIgnoreCase(mLineNumForFilter)) {
                    lines.add(refLine);
                }
            }
            if (lines.size() == 0) {
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
        if ("Y".equalsIgnoreCase(lineData.dangerFlag)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("温馨提示").setMessage("您正在采集的物料是危险品,是否还继续采集?")
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
    protected boolean refreshQuantity(final String quantity) {

        //1.实收数量必须大于0
        final float quantityV = CommonUtil.convertToFloat(quantity, 0.0f);
        if (Float.compare(quantityV, 0.0f) <= 0.0f) {
            showMessage("输入过账数量不合理");
            return false;
        }

        //2.实收数量必须小于合格数量(应收数量)
        final float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        if (Float.compare(quantityV, actQuantityV) > 0.0f) {
            showMessage("过账数量不能大于应收数量");
            return false;
        }

        // 3.过账数量 <= 检验批数量
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        final float orderQuantityV = CommonUtil.convertToFloat(lineData.orderQuantity, 0.0f);
        if (Float.compare(quantityV, orderQuantityV) > 0.0f) {
            showMessage("过账数量不能大于检验批数量");
            if (!cbSingle.isChecked())
                etQuantity.setText("");
            return false;
        }
        //4. 过账+退货 <= 应收
        final float returnQuantityV = CommonUtil.convertToFloat(getString(etReturnQuantity), 0.0F);
        if (Float.compare(quantityV + returnQuantityV, actQuantityV) > 0.0f) {
            showMessage("过账数量加采购退货数量不能大于应收数量");
            return false;
        }

        return true;
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if(!TextUtils.isEmpty(getString(etReturnQuantity))) {
            if(spMoveCause.getSelectedItemPosition() <=0) {
                showMessage("请先选择移动原因");
                return false;
            }
        }
        return super.checkCollectedDataBeforeSave();
    }

    @Override
    public void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etReturnQuantity);
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.returnQuantity = getString(etReturnQuantity);
        //移动原因
        Object selectedItem = spMoveCause.getSelectedItem();
        if (selectedItem != null)
            result.moveCause = selectedItem.toString().split("_")[0];
        return result;
    }
}
