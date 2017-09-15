package com.richfit.module_mcq.module_ds;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_mcq.R;
import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by monday on 2017/9/12.
 */

public class MCQLDSEditFragment extends BaseDSEditFragment<DSEditPresenterImp> {

    //副计量单位
    TextView tvMaterialUnitCustom;
    //副计量单位应发数量
    TextView tvActQuantityCustom;
    //副计量单位仓位数量
    TextView tvLocQuantityCustom;
    //副计量单位实发数量
    EditText etQuantityCustom;
    //副计量单位累计数量
    TextView tvTotalQuantityCustom;

    RichEditText etLocation;

    String mQuantityCustom;
    float mTotalQuantityCustom;


    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_lds_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        tvMaterialUnitCustom = mView.findViewById(R.id.mcq_tv_material_unit_custom);
        tvActQuantityCustom = mView.findViewById(R.id.mcq_tv_act_quantity_custom);
        etQuantityCustom = mView.findViewById(R.id.mcq_et_quantity_custom);
        tvTotalQuantityCustom = mView.findViewById(R.id.mcq_tv_total_quantity_custom);
        tvLocQuantityCustom = mView.findViewById(R.id.mcq_tv_location_quantity_custom);
        etLocation = mView.findViewById(R.id.et_location);
        //隐藏批次
        mView.findViewById(R.id.ll_batch_flag).setVisibility(View.GONE);
        //主计量单位
        TextView tvMaterialUniName = mView.findViewById(R.id.mcq_tv_material_unit_name);
        tvMaterialUniName.setText("主计量单位");

        //主计量单位应收数量
        TextView tvActQuantityName = mView.findViewById(R.id.act_quantity_name);
        tvActQuantityName.setText("主计量单位应发数量");

        //主计量单位实收数量
        TextView tvQuantityName = mView.findViewById(R.id.quantity_name);
        tvQuantityName.setText("主计量单位实发数量");

        //主计量单位库存数量
        TextView tvInvQuantityName = mView.findViewById(R.id.mcq_tv_inv_quantity_name);
        tvInvQuantityName.setText("主计量单位库存数量");


        //主计量单位仓位数量
        TextView tvLocationQuantityName = mView.findViewById(R.id.mcq_tv_location_quantity_name);
        tvLocationQuantityName.setText("主计量单位仓位数量");

        //主计量单位累计数量
        TextView tvTotalQuantityName = mView.findViewById(R.id.mcq_total_quantity_name);
        tvTotalQuantityName.setText("主计量单位累计数量");
    }

    @Override
    public void initData() {

        //初始化副计量单位的数据
        Bundle bundle = getArguments();
        if (bundle != null && mRefData != null) {
            //初始化仓位
            mSelectedLocationCombine = bundle.getString(Global.EXTRA_LOCATION_KEY);
            etLocation.setText(mSelectedLocationCombine);

            mPosition = bundle.getInt(Global.EXTRA_POSITION_KEY);
            //注意这里mPosition值此时还没有读取到
            RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
            //副计量单位
            tvMaterialUnitCustom.setText(lineData.unitCustom);
            //副计量单位应收数量
            tvActQuantityCustom.setText(lineData.actQuantityCustom);
            mQuantityCustom = bundle.getString(Global.EXTRA_QUANTITY_CUSTOM_KEY);
            //副计量单位实收数量
            etQuantityCustom.setText(mQuantityCustom);
            //副计量单位仓位数量
            tvLocQuantityCustom.setText(mQuantityCustom);
            //副计量单位累计数量（注意这里是父子节点）
            String totalQuantityCustom = bundle.getString(Global.EXTRA_TOTAL_QUANTITY_CUSTOM_KEY);
            tvTotalQuantityCustom.setText(totalQuantityCustom);
        }
        super.initData();
    }

    @Override
    public void initEvent() {
        super.initEvent();
        etLocation.setOnRichEditTouchListener((view, text) -> loadInventoryInfo());
    }

    @Override
    public void initDataLazily() {

    }


    @Override
    protected void loadInventoryInfo() {
        //不在加载库存，直接去加载缓存
        String location = getString(etLocation);
        //检测是否选择了发出库位
        if (TextUtils.isEmpty(location)) {
            tvLocQuantity.setText("");
            tvTotalQuantity.setText("");
            return;
        }
        mPresenter.getTransferInfoSingle(mRefData.refCodeId, mRefData.refType,
                mRefData.bizType, mRefLineId, getString(tvMaterialNum),
                getString(tvBatchFlag), location, "", -1, Global.USER_ID);
    }

    //绑定缓存
    @Override
    public void onBindCache(RefDetailEntity cache, String batchFlag, String location) {
        if (cache != null) {
            tvTotalQuantity.setText(cache.totalQuantity);
            tvTotalQuantityCustom.setText(cache.totalQuantityCustom);
            //查询该行的locationInfo
            List<LocationInfoEntity> locationInfos = cache.locationList;
            if (locationInfos == null || locationInfos.size() == 0) {
                //没有缓存
                tvLocQuantity.setText("0");
                tvLocQuantityCustom.setText("0");
                return;
            }
            //如果有缓存，但是可能匹配不上
            tvLocQuantity.setText("0");
            tvLocQuantityCustom.setText("0");
            //匹配每一个缓存
            for (LocationInfoEntity info : locationInfos) {
                if (isOpenBatchManager ? location.equalsIgnoreCase(info.locationCombine) &&
                        batchFlag.equalsIgnoreCase(info.batchFlag) : location.equalsIgnoreCase(info.locationCombine)) {
                    tvLocQuantity.setText(info.quantity);
                    tvLocQuantityCustom.setText(info.quantityCustom);
                    break;
                }
            }
        }
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        //检查是否合理，可以保存修改后的数据
        if (TextUtils.isEmpty(getString(etQuantity))) {
            showMessage("请输入实发数量");
            return false;
        }

        if (TextUtils.isEmpty(getString(etLocation))) {
            showMessage("请先输入下架仓位");
            return false;
        }


        if (Float.parseFloat(getString(etQuantity)) <= 0.0f) {
            showMessage("输入出库数量不合理,请重新输入");
            return false;
        }

        //如果副计量单位不为空
        if (!TextUtils.isEmpty(getString(tvMaterialUnitCustom))) {
            //检查副计量单位实发数量
            float totalQuantityCustomV = 0.0f;
            //副计量单位的累计数量
            totalQuantityCustomV += CommonUtil.convertToFloat(getString(tvTotalQuantityCustom), 0.0f);
            //副计量单位的应该数量
            float actQuantityCustomV = CommonUtil.convertToFloat(getString(tvActQuantityCustom), 0.0f);
            //本次副计量单位的出库数量
            float quantityCustomV = CommonUtil.convertToFloat(getString(etQuantityCustom), 0.0f);
            if (Float.compare(quantityCustomV + totalQuantityCustomV, actQuantityCustomV) > 0.0f) {
                showMessage("副计量单位实发数量输入有误，请重新输入");
                etQuantity.setText("");
                return false;
            }
        }

        //检查主计量单位实发数量
        String quantity = getString(etQuantity);
        if (Float.valueOf(quantity) <= 0.0f) {
            showMessage("输入主计量单位实发数量不合理");
            return false;
        }

        float totalQuantityV = 0.0f;
        //累计数量
        totalQuantityV += CommonUtil.convertToFloat(getString(tvTotalQuantity), 0.0f);
        //应该数量
        float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        //本次出库数量
        float quantityV = CommonUtil.convertToFloat(quantity, 0.0f);
        if (Float.compare(quantityV + totalQuantityV, actQuantityV) > 0.0f) {
            showMessage("主计量单位实发数量有误，请重新输入");
            etQuantity.setText("");
            return false;
        }
        return true;
    }


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.quantityCustom = getString(etQuantityCustom);
        result.batchFlag = !isOpenBatchManager ? null : getString(tvBatchFlag);
        result.location = getString(etLocation);
        return result;
    }

}
