package com.richfit.module_qhyt.module_rs.rsn;


import android.widget.ArrayAdapter;

import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_qhyt.module_rs.rsn.imp.QHYTRSNHeadPresenterImp;
import com.richfit.sdk_wzrs.base_rsn_head.BaseRSNHeadFragment;

import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/3/2.
 */

public class QHYTRSNHeadFragment extends BaseRSNHeadFragment<QHYTRSNHeadPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new QHYTRSNHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    protected void initView() {
        //如果BizType是46那么显示成本中心,否者显示项目编号
        if ("47".equalsIgnoreCase(mBizType)) {
            tvAutoCompName.setText("项目编号");
        }
    }

    @Override
    public void showAutoCompleteList(Map<String, List<SimpleEntity>> data) {
        if (data != null) {
            List<SimpleEntity> simpleEntities = null;
            if ("46".equals(mBizType)) {
                simpleEntities = data.get(Global.COST_CENTER_DATA);
            } else {
                simpleEntities = data.get(Global.PROJECT_NUM_DATA);
            }
            List<String> list = CommonUtil.toStringArray(simpleEntities, true);
            mAutoDatas.clear();
            mAutoDatas.addAll(list);
            if (mAutoAdapter == null) {
                mAutoAdapter = new ArrayAdapter<>(mActivity,
                        android.R.layout.simple_dropdown_item_1line, mAutoDatas);
                etAutoComp.setAdapter(mAutoAdapter);
                setAutoCompleteConfig(etAutoComp);
            } else {
                mAutoAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            if ("47".equalsIgnoreCase(mBizType)) {
                mRefData.projectNum = getString(etAutoComp).split("_")[0];
            } else {
                mRefData.costCenter = getString(etAutoComp).split("_")[0];
            }
        }
    }

}
