package com.richfit.module_cqyt.module_as105;


import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
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

import io.reactivex.Flowable;

/**
 * 注意该业务比较特别，每一个单据的所有的行明细都是由一行生成拆分出来的，这就意味着
 * 单据中所有的行的refLineId和lineNum都一致，所以不能将它作为唯一的表示来标识该行。
 * 这里采用的方式使用每一行明细的insLot字段来标识每一行的明细。
 * Created by monday on 2017/3/1.
 */

public class CQYTAS105CollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    EditText etReturnQuantity;
    EditText etMoveCauseDesc;
    Spinner spMoveCause;
    //件数
    EditText etQuantityCustom;
    //累计件数
    TextView tvTotalQuantityCustom;
    List<SimpleEntity> mMoveCauses;

    @Override
    protected int getContentId() {
        return R.layout.cqyt_fragment_as105_collect;
    }


    @Override
    public void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        //退货交货数量
        TextView tvRefLineNumName = (TextView) mView.findViewById(R.id.tv_ref_line_num_name);
        etReturnQuantity = (EditText) mActivity.findViewById(R.id.et_return_quantity);
        //如果输入的退货交货数量，那么移动原因必输，如果退货交货数量没有输入那么移动原因可输可不输
        etMoveCauseDesc = (EditText) mActivity.findViewById(R.id.et_move_cause_desc);
        spMoveCause = (Spinner) mActivity.findViewById(R.id.sp_move_cause);
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        tvTotalQuantityCustom = (TextView) mView.findViewById(R.id.cqyt_tv_total_quantity_custom);

        tvRefLineNumName.setText("检验批");
        llInsLostQuantity.setVisibility(View.VISIBLE);
        tvActQuantityName.setText("允许过账数量");
        tvQuantityName.setText("过账数量");
    }

    @Override
    public void initDataLazily() {
        super.initDataLazily();
        //这里让系统不能让用户手动输入批次，必须通过扫码的方式获取批次
        etBatchFlag.setEnabled(false);
    }

    @Override
    public void initData() {
        mPresenter.getDictionaryData("moveCause");
    }


    /**
     * 通过物料编码和批次匹配单据明细的行。这里我们返回的所有行的insLot集合
     *
     * @param materialNum
     * @param batchFlag
     * @return
     */
    @Override
    protected Flowable<ArrayList<String>> matchMaterialInfo(final String materialNum, final String batchFlag) {
        if (mRefData == null || mRefData.billDetailList == null ||
                mRefData.billDetailList.size() == 0 || TextUtils.isEmpty(materialNum)) {
            return Flowable.error(new Throwable("请先单据明细"));
        }
        ArrayList<String> insLosts = new ArrayList<>();
        List<RefDetailEntity> list = mRefData.billDetailList;
        for (RefDetailEntity entity : list) {
            if (entity.batchManagerStatus) {
                final String insLot = entity.insLot;
                //如果打开了批次，那么在看明细中是否有批次
                if (!TextUtils.isEmpty(entity.batchFlag) && !TextUtils.isEmpty(batchFlag)) {
                    if (materialNum.equalsIgnoreCase(entity.materialNum) &&
                            batchFlag.equalsIgnoreCase(entity.batchFlag) &&
                            !TextUtils.isEmpty(insLot))

                        insLosts.add(insLot);
                } else {
                    if (materialNum.equalsIgnoreCase(entity.materialNum) &&
                            !TextUtils.isEmpty(insLot))
                        insLosts.add(insLot);
                }
            } else {
                final String insLot = entity.insLot;
                //如果明细中没有打开了批次管理,那么只匹配物料编码
                if (materialNum.equalsIgnoreCase(entity.materialNum) && !TextUtils.isEmpty(insLot))
                    insLosts.add(insLot);
            }
        }
        if (insLosts.size() == 0) {
            return Flowable.error(new Throwable("未获取到匹配的物料"));
        }
        return Flowable.just(insLosts);
    }


    /**
     * 通过单据行的检验批得到该行在单据明细列表中的位置
     *
     * @param insLot:单据行的检验批
     * @return 返回该行号对应的行明细在明细列表的索引
     */
    @Override
    protected int getIndexByLineNum(String insLot) {
        int index = -1;
        if (TextUtils.isEmpty(insLot))
            return index;

        if (mRefData == null || mRefData.billDetailList == null
                || mRefData.billDetailList.size() == 0)
            return index;

        for (RefDetailEntity detailEntity : mRefData.billDetailList) {
            index++;
            if (insLot.equalsIgnoreCase(detailEntity.insLot))
                break;

        }
        return index;
    }

    @Override
    public void bindCommonCollectUI() {
        mSelectedRefLineNum = mRefLines.get(spRefLine.getSelectedItemPosition());
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        etReturnQuantity.setText(lineData.returnQuantity);
        etMoveCauseDesc.setText(lineData.moveCauseDesc);
        tvInsLotQuantity.setText(lineData.insLotQuantity);
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
            mMoveCauses.addAll(moveCauses);
            SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, mMoveCauses);
            spMoveCause.setAdapter(adapter);
        }
    }

    @Override
    public void onBindCache(RefDetailEntity cache, String batchFlag, String location) {
        super.onBindCache(cache,batchFlag,location);
        if (!isNLocation && cache != null) {
            tvTotalQuantityCustom.setText(cache.totalQuantityCustom);
        } else if (cache != null) {
            tvTotalQuantityCustom.setText(cache.totalQuantityCustom);
        }

        if (cache != null) {
            etReturnQuantity.setText(cache.returnQuantity);
            etMoveCauseDesc.setText(cache.moveCauseDesc);
            //移动原因
            UiUtil.setSelectionForSimpleSp(mMoveCauses, cache.moveCause, spMoveCause);
        }
    }

    /**
     * 检验逻辑:
     * 1.过账数量<=允许过账数量
     * 2.如果退货数量不为空，那么退货数量+过账数量<=允许过账数量
     *
     * @param quantity
     * @return
     */
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
            showMessage("过账数量不能大于允许过账数量");
            return false;
        }

        if (!TextUtils.isEmpty(getString(etReturnQuantity))) {
            //3.如果退货数量不为空
            float returnQuantityV = CommonUtil.convertToFloat(getString(etReturnQuantity), 0.0F);
            if (Float.compare(quantityV + returnQuantityV, actQuantityV) > 0.0f) {
                showMessage("过账数量+退货交货数量之和不能大于允许过账数量");
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        //如果退货数量不为0那么移动原因说明必须输入
        if ((!isEmpty(getString(etReturnQuantity)) && !"0".equals(getString(etReturnQuantity)))
                && isEmpty(getString(etMoveCauseDesc))) {
            showMessage("请输入移动原因说明");
            return false;
        }
        if (!isNLocation) {
            final String location = getString(etLocation);
            if (TextUtils.isEmpty(location)) {
                showMessage("请输入上架仓位");
                return false;
            }

            if (location.length() > 10) {
                showMessage("您输入的上架不合理");
                return false;
            }
        }

        final String quantityCustom = getString(etQuantityCustom);
        if (TextUtils.isEmpty(quantityCustom)) {
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
        //退货交货数量
        result.returnQuantity = getString(etReturnQuantity);
        //移动原因描述
        result.moveCauseDesc = getString(etMoveCauseDesc);
        //移动原因
        if (spMoveCause.getSelectedItemPosition() > 0) {
            result.moveCause = mMoveCauses.get(spMoveCause.getSelectedItemPosition()).code;
        }
        //件数
        result.quantityCustom = getString(etQuantityCustom);
        return result;
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    public void saveCollectedDataSuccess(String message) {
        super.saveCollectedDataSuccess(message);
        //累计件数
        tvTotalQuantityCustom.setText(String.valueOf(ArithUtil.add(getString(etQuantityCustom),
                getString(tvTotalQuantityCustom))));
        if (!cbSingle.isChecked()) {
            etQuantityCustom.setText("");
        }
    }

    @Override
    public void _onPause() {
        clearCommonUI(etReturnQuantity, etMoveCauseDesc);
        if (spMoveCause.getAdapter() != null) {
            spMoveCause.setSelection(0);
        }
        super._onPause();
    }
}
