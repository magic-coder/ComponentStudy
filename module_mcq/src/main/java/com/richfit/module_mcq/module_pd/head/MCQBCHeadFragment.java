package com.richfit.module_mcq.module_pd.head;

import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_mcq.R;
import com.richfit.sdk_wzpd.blind.head.BCHeadFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存级盘点。默认仓库号写死为1Q0
 * Created by monday on 2017/8/29.
 */

public class MCQBCHeadFragment extends BCHeadFragment {

    private final String storageNum = "1Q0";

    Spinner spLocationType;
    List<SimpleEntity> mLocationTypes;

    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_pd_head;
    }

    @Override
    public void initView() {
        resetUI();
        super.initView();
        mRefData = null;
        rbWarehouseLevel.setChecked(true);
        llWarehouseLevel.setVisibility(View.VISIBLE);
        llCheckGuide.setVisibility(View.GONE);

        //仓储类型
        spLocationType = mView.findViewById(R.id.sp_location_type);
    }

    @Override
    public void loadInvsComplete() {
        //库存地点加载完毕
        mPresenter.getDictionaryData("locationType");
    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        Log.e("yff","======");
        List<SimpleEntity> locationTypes = data.get("locationType");
        if (locationTypes != null) {
            if (mLocationTypes == null) {
                mLocationTypes = new ArrayList<>();
            }
            mLocationTypes.clear();
            mLocationTypes.addAll(locationTypes);
            SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, mLocationTypes, false);
            spLocationType.setAdapter(adapter);
        }
    }

    @Override
    protected void startCheck() {
        //请求抬头信息
        mRefData = null;
        if (rbWarehouseLevel.isChecked()) {
            //库位级盘点
            if (mWorkDatas == null || mWorkDatas.size() <= 0) {
                showMessage("工厂未初始化");
                return;
            }
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("locationType", mLocationTypes.get(spLocationType.getSelectedItemPosition()).code);
            mPresenter.getCheckInfo(Global.USER_ID, mBizType, "02",
                    DEFAULT_SPECIAL_FLAG, "",
                    mWorkDatas.get(spWork.getSelectedItemPosition()).workId,
                    mInvDatas.get(spInv.getSelectedItemPosition()).invId,
                    getString(etTransferDate),extraMap);
        }
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            mRefData.storageNum = storageNum;
        }
    }
}
