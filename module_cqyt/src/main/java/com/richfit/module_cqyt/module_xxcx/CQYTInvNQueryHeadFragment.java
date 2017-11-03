package com.richfit.module_cqyt.module_xxcx;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_xxcx.inventory_query_n.header.InvNQueryHeaderFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 无仓考库存查询
 * Created by monday on 2017/8/4.
 */

public class CQYTInvNQueryHeadFragment extends InvNQueryHeaderFragment {

    EditText etMaterialNum;
    EditText etLocation;


    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        super.handleBarCodeScanResult(type,list);
        etMaterialNum.clearFocus();
        etLocation.clearFocus();
        if (list != null && list.length > 12) {
            String materialNum = list[Global.MATERIAL_POS];
            clearCommonUI(etMaterialNum);
            etMaterialNum.setText(materialNum);
            //处理仓位
        } else if (list != null && list.length == 1) {
            String location = list[0];
            clearCommonUI(etLocation);
            etLocation.setText(location);
        } else if (list != null && list.length == 2 && isOpenLocationType) {
            //如果扫描的是带仓储类型的仓位条码，依然要处理仓位
            String location = list[0];
            clearCommonUI(etLocation);
            etLocation.setText(location);
        }
    }

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_invn_head;
    }

    @Override
    public void initView() {
        super.initView();
        llMaterialClass.setVisibility(View.GONE);
        llMaterialDesc.setVisibility(View.GONE);
        etLocation = mView.findViewById(R.id.et_location);
        etMaterialNum = mView.findViewById(R.id.et_material_num);
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            mRefData.materialNum = getString(etMaterialNum);
            mRefData.location = getString(etLocation);
        }
    }

    @Override
    public boolean isNeedShowFloatingButton() {
        return false;
    }

}
