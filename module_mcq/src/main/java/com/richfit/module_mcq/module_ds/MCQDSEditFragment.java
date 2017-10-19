package com.richfit.module_mcq.module_ds;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_mcq.R;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;
import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by monday on 2017/8/31.
 */

public class MCQDSEditFragment extends BaseDSEditFragment<DSEditPresenterImp> {

    //副计量单位
    TextView tvMaterialUnitCustom;
    //副计量单位应发数量
    TextView tvActQuantityCustom;
    //副计量单位仓位数量
    TextView tvLocQuantityCustom;
    //副计量单位库存数量
    TextView tvInvQuantityCustom;
    //副计量单位实发数量
    EditText etQuantityCustom;
    //副计量单位累计数量
    TextView tvTotalQuantityCustom;

    String mQuantityCustom;
    float mTotalQuantityCustom;

    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_ds_edit;
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
        tvInvQuantityCustom = mView.findViewById(R.id.mcq_tv_inv_quantity_custom);
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

        //显示仓储类型
        llLocationType.setVisibility(View.VISIBLE);
    }


    @Override
    public void initDataLazily() {

    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null && mRefData != null) {
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
    }


    //增加副计量单位的赋值
    @Override
    protected void getTransferSingle(int position) {
        final String invQuantity = mInventoryDatas.get(position).invQuantity;
        final String invQuantityCustom = mInventoryDatas.get(position).invQuantityCustom;
        //检测是否选择了发出库位
        if (position <= 0) {
            spLocation.setSelection(0);
            tvInvQuantity.setText("");
            tvLocQuantity.setText("");
            tvTotalQuantity.setText("");
            //副计量单位
            tvInvQuantityCustom.setText("");
            tvLocQuantityCustom.setText("");
            tvTotalQuantityCustom.setText("");
            return;
        }
        //如果满足条件，那么先显示库存数量
        tvInvQuantity.setText(invQuantity);
        tvInvQuantityCustom.setText(invQuantityCustom);

        mPresenter.getTransferInfoSingle(mRefData.refCodeId, mRefData.refType,
                mRefData.bizType, mRefLineId, getString(tvMaterialNum),
                getString(tvBatchFlag),
                mInventoryDatas.get(position).locationCombine,
                "", -1, Global.USER_ID);
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
            boolean isMatch = false;
            String locationType = "";
            if(isOpenLocationType) {
                locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
            }

            //匹配每一个缓存
            for (LocationInfoEntity info : locationInfos) {
                if (isOpenLocationType) {
                    isMatch = isOpenBatchManager ? location.equalsIgnoreCase(info.locationCombine) &&
                            batchFlag.equalsIgnoreCase(info.batchFlag) && locationType.equalsIgnoreCase(info.locationType) :
                            location.equalsIgnoreCase(info.locationCombine) && locationType.equalsIgnoreCase(info.locationType);
                } else {
                    isMatch = isOpenBatchManager ? location.equalsIgnoreCase(info.locationCombine) &&
                            batchFlag.equalsIgnoreCase(info.batchFlag) : location.equalsIgnoreCase(info.locationCombine);
                }
                if (isMatch) {
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
        if (!TextUtils.isEmpty(getString(tvMaterialUnitCustom))) {
            if (TextUtils.isEmpty(getString(etQuantityCustom))) {
                showMessage("请输入副计量单位的实发数量");
                return false;
            }


            if (Float.parseFloat(getString(etQuantityCustom)) <= 0.0f) {
                showMessage("输入副计量单位的实发数量不合理,请重新输入");
                return false;
            }

            //是否满足本次录入数量+累计数量-上次已经录入的出库数量<=应出数量
            float actQuantityCustomV = CommonUtil.convertToFloat(getString(tvActQuantityCustom), 0.0f);
            float totalQuantityCustomV = CommonUtil.convertToFloat(getString(tvTotalQuantityCustom), 0.0f);
            float collectedQuantityCustomV = CommonUtil.convertToFloat(mQuantityCustom, 0.0f);
            //修改后的出库数量
            float quantityCustomV = CommonUtil.convertToFloat(getString(etQuantityCustom), 0.0f);
            float residualQuantityCustomV = totalQuantityCustomV - collectedQuantityCustomV + quantityCustomV;//减去已经录入的数量
            if (Float.compare(residualQuantityCustomV, actQuantityCustomV) > 0.0f) {
                showMessage("输入副计量单位的实发数量有误");
                etQuantity.setText("");
                return false;
            }

            //该仓位的库存数量
            final float invQuantityCustomV = CommonUtil.convertToFloat(getString(tvInvQuantityCustom), 0.0f);
            if (Float.compare(quantityCustomV, invQuantityCustomV) > 0.0f) {
                showMessage("输入副计量单位实发数量大于库存数量，请重新输入");
                etQuantity.setText("");
                return false;
            }

            mQuantityCustom = quantityCustomV + "";
            mTotalQuantityCustom = residualQuantityCustomV;
        }

        return true;
    }

    @Override
    public void saveEditedDataSuccess(String message) {
        super.saveEditedDataSuccess(message);
        tvLocQuantityCustom.setText(mQuantityCustom);
        tvTotalQuantityCustom.setText(String.valueOf(mTotalQuantityCustom));
    }


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.quantityCustom = getString(etQuantityCustom);
        result.batchFlag = !isOpenBatchManager ? null : getString(tvBatchFlag);
        return result;
    }
}
