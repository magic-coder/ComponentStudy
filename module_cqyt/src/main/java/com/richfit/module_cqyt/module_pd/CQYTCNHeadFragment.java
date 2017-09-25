package com.richfit.module_cqyt.module_pd;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzpd.checkn.head.CNHeadFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/8/7.
 */

public class CQYTCNHeadFragment extends CNHeadFragment {

    Spinner spLocationType;
    List<SimpleEntity> mLocationTypes;

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_cn_head;
    }

    @Override
    public void initView() {
        super.initView();
        LinearLayout llCheckGuide = mView.findViewById(R.id.ll_check_guide);
        llCheckGuide.setVisibility(View.GONE);

        mView.findViewById(R.id.ll_location_type).setVisibility(View.VISIBLE);
        spLocationType = mView.findViewById(R.id.sp_location_type);
    }


    @Override
    public void initData() {
        resetUI();
        super.initData();
        mRefData = null;
        rbStorageNumLevel.setChecked(true);
        llStorageNumLevel.setVisibility(View.VISIBLE);
        if (spStorageNum.getAdapter() == null) {
            mPresenter.getStorageNums(0);
        }
    }

    @Override
    public void loadStorageNumComplete() {
        mPresenter.getDictionaryData("locationType");
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
            SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, mLocationTypes, false);
            spLocationType.setAdapter(adapter);
        }
    }

    @Override
    protected void startCheck() {
        //请求抬头信息
        mRefData = null;
        if (rbStorageNumLevel.isChecked()) {
            //库位级盘点
            if (mStorageNums == null || mStorageNums.size() <= 0) {
                showMessage("仓库号未初始化");
                return;
            }
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("locationType", mLocationTypes.get(spLocationType.getSelectedItemPosition()).code);
            mPresenter.getCheckInfo(Global.USER_ID, mBizType, "01",
                    mSpecialFlag, mStorageNums.get(spStorageNum.getSelectedItemPosition()),
                    "", "", getString(etTransferDate), extraMap);
        }
    }
}
