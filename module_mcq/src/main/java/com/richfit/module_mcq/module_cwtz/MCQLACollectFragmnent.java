package com.richfit.module_mcq.module_cwtz;

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
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_mcq.R;
import com.richfit.sdk_cwtz.collect.LACollectFragment;
import com.richfit.sdk_cwtz.collect.imp.LACollectPresenterImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 副计量单位等信息自有在获取库存和获取单据的时候才能获取到值
 * Created by monday on 2017/8/28.
 */

public class MCQLACollectFragmnent extends LACollectFragment {

    //主计量单位
    TextView tvMainMaterialUnitName;

    //副计量单位
    TextView tvMaterialUnitCustom;

    //主计量单位库存数量
    TextView tvMainInvQuantityName;

    //副计量单位库存数量
    TextView tvInvQuantityCustom;

    //主计量单位调整数量
    TextView tvMainQuantityName;

    //副计量单位调整数量
    EditText etQuantityCustom;


    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_cwtz_collect;
    }


    @Override
    public void initView() {
        tvMainMaterialUnitName = mView.findViewById(R.id.mcq_tv_main_material_unit_name);
        tvMaterialUnitCustom = mView.findViewById(R.id.mcq_tv_material_unit_custom);
        tvMainInvQuantityName = mView.findViewById(R.id.mcq_tv_send_inv_quantity_name);
        tvInvQuantityCustom = mView.findViewById(R.id.mcq_tv_inv_quantity_custom);
        tvMainQuantityName = mView.findViewById(R.id.mcq_tv_quantity_name);
        etQuantityCustom = mView.findViewById(R.id.mcq_et_quantity_custom);
        tvMainMaterialUnitName.setText("主计量单位");
        tvMainInvQuantityName.setText("主计量单位库存数量");
        tvMainQuantityName.setText("主计量单位调整数量");
        //隐藏批次
        mView.findViewById(R.id.mcq_ll_batch_flag).setVisibility(View.GONE);

        //打开仓储类型
        llLocationType.setVisibility(View.VISIBLE);
    }

    @Override
    public void initEvent() {
        super.initEvent();

        //选择下拉,同时更新主计量单位库存数量和副计量单位库存数量，以及副计量单位
        RxAdapterView.itemSelections(spSpecialInv)
                .filter(pos -> pos > 0)
                .subscribe(pos -> {
                    tvMaterialUnitCustom.setText(mInventoryDatas.get(pos).unitCustom);
                    tvInvQuantityCustom.setText(mInventoryDatas.get(pos).invQuantityCustom);
                    tvSendInvQuantity.setText(mInventoryDatas.get(pos).invQuantity);
                });
    }


    @Override
    public void getMaterialInfoSuccess(MaterialEntity data) {
        super.getMaterialInfoSuccess(data);
        isOpenBatchManager = false;
        etBatchFlag.setEnabled(false);
    }

    @Override
    protected void loadInventoryInfo(String location) {
        //清除副计量单位
        tvInvQuantityCustom.setText("");
        super.loadInventoryInfo(location);
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        String quantityCustom = getString(etQuantityCustom);
        if (CommonUtil.convertToFloat(quantityCustom,.0F) <= 0.0f) {
            showMessage("输入副计量单位调整数量不合理");
            return false;
        }
        //副计量单位的库存数量
        final float invQuantityCustomV = CommonUtil.convertToFloat(getString(tvInvQuantityCustom), 0.0f);
        final float quantityV = CommonUtil.convertToFloat(quantityCustom, 0.0f);
        if (Float.compare(quantityV, invQuantityCustomV) > 0.0f) {
            showMessage("输入副计量单位调整数量有误，请重新输入");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        //提交副计量单位调整数量
        result.quantityCustom = getString(etQuantityCustom);
        return result;
    }
}
