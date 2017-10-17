package com.richfit.module_mcq.module_as;

import android.media.tv.TvInputManager;
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
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_mcq.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 是否质检为Y时，单据状态为质检（Y/N）时才允许采集，是否质检为N时，
 * 单据状态为创建时允许采集；当副计量单位应收数量不为空时，副计量单位的实收数量必输；
 * 点击上架按钮提示入库通知单总共XXX项，此次录入并上传XXX项，请确认，
 * 点击确认按钮上传累计实收数量不为0的入库明细的上架信息至ERP系统，
 * 点击取消按钮可以继续在手持上进行操作，对于质检物资，必须保证应收数量等于累计实收数量才能进行上架。
 * Created by monday on 2017/8/30.
 */

public class MCQASCollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    //副计量单位
    TextView tvMaterialUnitCustom;
    //副计量单位应收数量
    TextView tvActQuantityCustom;
    //副计量单位仓位数量
    TextView tvLocQuantityCustom;
    //副计量单位实收数量
    EditText etQuantityCustom;
    //副计量单位累计数量
    TextView tvTotalQuantityCustom;

    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_as_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        tvMaterialUnitCustom = mView.findViewById(R.id.mcq_tv_material_unit_custom);
        tvActQuantityCustom = mView.findViewById(R.id.mcq_tv_act_quantity_custom);
        etQuantityCustom = mView.findViewById(R.id.mcq_et_quantity_custom);
        tvTotalQuantityCustom = mView.findViewById(R.id.mcq_tv_total_quantity_custom);
        tvLocQuantityCustom = mView.findViewById(R.id.mcq_tv_location_quantity_custom);
        //主计量单位
        TextView tvMaterialUniName = mView.findViewById(R.id.mcq_tv_material_unit_name);
        tvMaterialUniName.setText("主计量单位");

        //主计量单位应收数量
        tvActQuantityName.setText("主计量单位应收数量");

        //主计量单位实收数量
        tvQuantityName.setText("主计量单位实收数量");

        //主计量单位仓位数量
        TextView tvLocationQuantityName = mView.findViewById(R.id.mcq_tv_location_quantity_name);
        tvLocationQuantityName.setText("主计量单位仓位数量");

        //主计量单位累计数量
        TextView tvTotalQuantityName = mView.findViewById(R.id.mcq_total_quantity_name);
        tvTotalQuantityName.setText("主计量单位累计数量");

        //隐藏批次
        llBatchFlag.setVisibility(View.GONE);
        //隐藏特殊库存标识
        mView.findViewById(R.id.mcq_ll_special_inv).setVisibility(View.GONE);

        //打开仓储类型
        llLocationType.setVisibility(View.VISIBLE);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        etLocation.setOnRichAutoEditTouchListener((view, location) -> {
            hideKeyboard(etLocation);
            getTransferSingle(getString(etBatchFlag), location);
        });
    }

    @Override
    public void initData() {
        super.initData();
    }

    /**
     * 匹配到该物料后，将数据显示到UI
     */
    @Override
    public void bindCommonCollectUI() {
        mSelectedRefLineNum = mRefLines.get(spRefLine.getSelectedItemPosition());
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        //通过是否质检和单据状态进行分发
        //如果qm_flag是Y
        //单据状态 01:创建 03:质检
        if ("Y".equalsIgnoreCase(lineData.qmFlag) && !"03".equalsIgnoreCase(lineData.status)) {
            showMessage("该物料是质检物资，但是单据状态不是质检状态，不允许采集");
            return;
        }

        if ("N".equalsIgnoreCase(lineData.qmFlag) && !"01".equalsIgnoreCase(lineData.status)) {
            showMessage("该物料是非质检物资，但是单据状态不是创建状态，不允许采集");
            return;
        }

        super.bindCommonCollectUI();
        //不考虑批次
        etBatchFlag.setEnabled(false);
        isOpenBatchManager = false;

        //客户化字段
        tvMaterialUnitCustom.setText(lineData.unitCustom);
        tvActQuantityCustom.setText(lineData.actQuantityCustom);

    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        List<SimpleEntity> locationTypes = data.get("locationType");
        if (locationTypes != null) {
            if (mLocationTypes == null) {
                mLocationTypes = new ArrayList<>();
            }
            mLocationTypes.clear();
            mLocationTypes.addAll(locationTypes);
            SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp,
                    mLocationTypes, false);
            spLocationType.setAdapter(adapter);
        }
    }

    //重写该方法，在获取提示库存之前清除历史库存
    @Override
    public void loadLocationList(boolean isDropDown) {
        if (mLocationList != null && mLocationAdapter != null) {
            mLocationList.clear();
            mLocationAdapter.notifyDataSetChanged();
        }
        super.loadLocationList(isDropDown);
    }

    @Override
    public void onBindCache(RefDetailEntity cache, String batchFlag, String location) {
        super.onBindCache(cache,batchFlag,location);
        if (!isNLocation && cache != null) {
            tvTotalQuantityCustom.setText(cache.totalQuantityCustom);
        } else if (cache != null) {
            tvTotalQuantityCustom.setText(cache.totalQuantityCustom);
        }
    }

    /**
     * 如果未获取到缓存那么重写该方法，将副计量单位的仓位数量和累计数量设置默认值
     *
     * @param message
     */
    @Override
    public void loadCacheFail(String message) {
        showMessage(message);
        spInv.setEnabled(true);
        isBatchValidate = true;
        if (!isNLocation) {
            tvLocQuantity.setText("0");
            tvLocQuantityCustom.setText("0");
        }
        tvTotalQuantity.setText("0");
        tvTotalQuantityCustom.setText("0");
        if (cbSingle.isChecked() && checkCollectedDataBeforeSave()) {
            saveCollectedData();
        }
    }

    /**
     * 校验副计量单位的数量
     *
     * @param quantity
     * @return
     */
    @Override
    protected boolean refreshQuantity(final String quantity) {

        //校验副计量单位的实收数量
        if (!TextUtils.isEmpty(getString(tvMaterialUnitCustom))) {
            //如果副计量单位不为空
            String actQuantityCustom = getString(tvActQuantityCustom);
            String quantityCustom = getString(etQuantityCustom);
            if (TextUtils.isEmpty(quantityCustom)) {
                showMessage("请先输入副计量单位的实收数量");
                return false;
            }

            float actQuantityCustomV = CommonUtil.convertToFloat(actQuantityCustom, 0.0F);
            float quantityCustomV = CommonUtil.convertToFloat(quantityCustom, 0.0F);
            float totalQuantityCustomV = CommonUtil.convertToFloat(getString(tvTotalQuantityCustom), 0.0F);
            if (Float.compare(quantityCustomV, 0.0f) <= 0.0f) {
                showMessage("输入副计量实收数量不合理");
                return false;
            }
            if (Float.compare(quantityCustomV + totalQuantityCustomV, actQuantityCustomV) > 0.0f) {
                showMessage("输入副计量实收数量有误，请出现输入");
                if (!cbSingle.isChecked())
                    etQuantityCustom.setText("");
                return false;
            }
        }
        //校验主计量单位的实收数量
        return super.refreshQuantity(quantity);
    }


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.quantityCustom = getString(etQuantityCustom);
        result.batchFlag = !isOpenBatchManager ? null : getString(etBatchFlag);
        return result;
    }

    /**
     * 数据保存成功，更新副计量为的累计数量
     *
     * @param message
     */
    @Override
    public void saveCollectedDataSuccess(String message) {
        tvTotalQuantityCustom.setText(String.valueOf(ArithUtil.add(getString(etQuantityCustom), getString(tvTotalQuantityCustom))));
        if (!isNLocation) {
            tvLocQuantityCustom.setText(String.valueOf(ArithUtil.add(getString(etQuantityCustom), getString(tvLocQuantityCustom))));
        }
        if (!cbSingle.isChecked()) {
            etQuantityCustom.setText("");
        }
        super.saveCollectedDataSuccess(message);
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }


    @Override
    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(tvActQuantityCustom, tvLocQuantityCustom, tvMaterialUnitCustom, tvTotalQuantityCustom, etQuantityCustom);
    }
}
