package com.richfit.module_cqyt.module_ys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/7/5.
 */

public class CQYTAOEditFragment extends BaseASEditFragment<ASEditPresenterImp> {

    TextView tvInsLotQuantity;
    //仓储类型
    Spinner spLocationType;
    List<SimpleEntity> mLocationTypes;

    @Override
    public void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        TextView tvRefLineNumName = (TextView) mView.findViewById(R.id.tv_ref_line_num_name);
        tvRefLineNumName.setText("检验批");
        LinearLayout llInsLotQuantity = (LinearLayout) mView.findViewById(R.id.ll_inslot_quantity);
        tvInsLotQuantity = (TextView) mView.findViewById(R.id.tv_insLot_quantity);
        llInsLotQuantity.setVisibility(View.VISIBLE);
        tvActQuantityName.setText("允许过账数量");
        tvQuantityName.setText("过账数量");
        //显示仓储类型
        mView.findViewById(R.id.ll_location_type).setVisibility(View.VISIBLE);
        spLocationType = mView.findViewById(R.id.sp_location_type);
    }

    @Override
    public void initData() {
        super.initData();
        final RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        tvInsLotQuantity.setText(lineData.orderQuantity);
        mPresenter.getDictionaryData("locationType");
    }

    @Override
    public void initDataLazily() {

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

            //默认选择缓存的数据
            Bundle arguments = getArguments();
            if (arguments != null) {
                String locationType = arguments.getString(Global.EXTRA_LOCATION_TYPE_KEY);
                UiUtil.setSelectionForSimpleSp(mLocationTypes, locationType, spLocationType);
            }
        }
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {

        if (!isNLocation && !isValidatedLocation(getString(etLocation))) {
            return false;
        }

        if(mLocationTypes == null || mLocationTypes.size() <= 0) {
            showMessage("未获取到仓储类型");
            return false;
        }

        if (TextUtils.isEmpty(getString(etQuantity))) {
            showMessage("请输入入库数量");
            return false;
        }

        float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        float totalQuantityV = CommonUtil.convertToFloat(getString(tvTotalQuantity), 0.0f);
        float collectedQuantity = CommonUtil.convertToFloat(mQuantity, 0.0f);
        float quantityV = CommonUtil.convertToFloat(getString(etQuantity), 0.0f);
        if (Float.compare(quantityV, 0.0f) <= 0.0f) {
            showMessage("输入数量不合理");
            etQuantity.setText("");
            return false;
        }
        float residualQuantity = totalQuantityV - collectedQuantity + quantityV;//减去已经录入的数量
        if (Float.compare(residualQuantity, actQuantityV) > 0.0f) {
            showMessage("输入实收数量不能大于应收数量");
            etQuantity.setText("");
            return false;
        }
        final float orderQuantityV = CommonUtil.convertToFloat(getString(tvInsLotQuantity), 0.0F);
        if (Float.compare(residualQuantity, orderQuantityV) > 0.0f) {
            showMessage("输入实收数量不能大于检验批数量");
            etQuantity.setText("");
            return false;
        }
        mQuantity = quantityV + "";
        mTotalQuantity = residualQuantity;
        return true;
    }


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        //仓储类型
        result.locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
        return result;
    }
}
