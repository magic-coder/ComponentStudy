package com.richfit.module_mcq.module_cwtz;

import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.module_mcq.R;
import com.richfit.sdk_cwtz.collect.LACollectFragment;

/**
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
        tvMainMaterialUnitName.setText("主计量单位");
        tvMainInvQuantityName.setText("主计量单位库存数量");
        tvMainQuantityName.setText("主计量单位调整数量");

    }

    @Override
    public void initEvent() {
        super.initEvent();
        //选择下拉,同时更新主计量单位库存数量和副计量单位库存数量
        RxAdapterView.itemSelections(spSpecialInv)
                .filter(pos -> pos > 0)
                .subscribe(pos -> {
                    tvSendInvQuantity.setText(mInventoryDatas.get(pos).invQuantity);
                    tvInvQuantityCustom.setText(mInventoryDatas.get(pos).invQuantityCustom);
                });
    }

    @Override
    public void getMaterialInfoSuccess(MaterialEntity data) {
        super.getMaterialInfoSuccess(data);
        //副计量单位
        tvMaterialUnitCustom.setText(data.unitCustom);
        isOpenBatchManager = false;
        etBatchFlag.setEnabled(false);
    }



    @Override
    public boolean checkCollectedDataBeforeSave() {
        String secondQuantity = getString(etQuantityCustom);
        if (Float.valueOf(secondQuantity) <= 0.0f) {
            showMessage("输入副计量单位调整数量不合理");
            return false;
        }
        //副计量单位的库存数量
        final float secondInvQunatityV = CommonUtil.convertToFloat(getString(tvInvQuantityCustom), 0.0f);
        final float quantityV = CommonUtil.convertToFloat(secondQuantity, 0.0f);
        if (Float.compare(quantityV, secondInvQunatityV) > 0.0f) {
            showMessage("输入副计量单位调整数量有误，请重新输入");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }

}
