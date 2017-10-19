package com.richfit.module_cqyt.module_ys;


import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.ArithUtil;
import com.richfit.common_lib.utils.L;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    List<SimpleEntity> mMoveCauses;

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
        //显示仓储类型
        llLocationType.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        mPresenter.getDictionaryData("moveCause");
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
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        super.loadDictionaryDataSuccess(data);
        List<SimpleEntity> moveCauses = data.get("moveCause");
        if (moveCauses != null) {
            if (mMoveCauses == null) {
                mMoveCauses = new ArrayList<>();
            }
            mMoveCauses.clear();
            SimpleEntity tmp = new SimpleEntity();
            tmp.name = "请选择";
            mMoveCauses.add(tmp);
            mMoveCauses.addAll(moveCauses);
            SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp,
                    mMoveCauses);
            spMoveCause.setAdapter(adapter);
        }
    }

    @Override
    public void onBindCache(RefDetailEntity cache, String batchFlag, String location) {
       super.onBindCache(cache,batchFlag,location);
        if (cache != null) {
            etReturnQuantity.setText(cache.returnQuantity);
            //移动原因
            UiUtil.setSelectionForSimpleSp(mMoveCauses, cache.moveCause, spMoveCause);
        }
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
        if (!TextUtils.isEmpty(getString(etReturnQuantity))) {
            if (spMoveCause.getSelectedItemPosition() <= 0) {
                showMessage("请先选择移动原因");
                return false;
            }
        }

        if(mLocationTypes == null || mLocationTypes.size() <= 0) {
            showMessage("未获取到仓储类型");
            return false;
        }

        return super.checkCollectedDataBeforeSave();
    }

    @Override
    public void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etReturnQuantity);
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.returnQuantity = getString(etReturnQuantity);
        //移动原因
        if (spMoveCause.getSelectedItemPosition() > 0) {
            result.moveCause = mMoveCauses.get(spMoveCause.getSelectedItemPosition()).code;
        }
        return result;
    }
}
