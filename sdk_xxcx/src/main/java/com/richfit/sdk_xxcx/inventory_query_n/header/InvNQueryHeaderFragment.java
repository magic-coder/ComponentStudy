package com.richfit.sdk_xxcx.inventory_query_n.header;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.lib_adapter.WorkAdapter;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.sdk_xxcx.R;
import com.richfit.sdk_xxcx.R2;
import com.richfit.sdk_xxcx.inventory_query_n.header.imp.InvNQueryHeaderPresenterImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 物资库存信息查询-无参考
 * Created by monday on 2017/5/25.
 */

public class InvNQueryHeaderFragment extends BaseFragment<InvNQueryHeaderPresenterImp>
        implements IInvNQueryHeaderView {

    @BindView(R2.id.sp_work)
    Spinner spWork;
    @BindView(R2.id.ll_send_work)
    LinearLayout llSendWork;
    @BindView(R2.id.sp_inv)
    Spinner spInv;
    @BindView(R2.id.et_material_class)
    EditText etMaterialClass;
    @BindView(R2.id.et_material_desc)
    EditText etMaterialDesc;
    @BindView(R2.id.ll_material_class)
    protected LinearLayout llMaterialClass;
    @BindView(R2.id.ll_material_desc)
    protected LinearLayout llMaterialDesc;
    @BindView(com.richfit.common_lib.R2.id.ll_location_type)
    protected LinearLayout llLocationType;
    @BindView(com.richfit.common_lib.R2.id.sp_location_type)
    protected Spinner spLocationType;


    /*工厂*/
    WorkAdapter mWorkAdapter;
    List<WorkEntity> mWorks;

    /*库位*/
    InvAdapter mInvAdapter;
    List<InvEntity> mInvs;

    /*仓储类型*/
    protected List<SimpleEntity> mLocationTypes;
    /*是否启用仓储类型*/
    protected boolean isOpenLocationType = false;

    @Override
    protected int getContentId() {
        return R.layout.xxcx_fragment_invn_query_header;
    }

    @Override
    public void initPresenter() {
        mPresenter = new InvNQueryHeaderPresenterImp(mActivity);
    }


    public void initVariable(Bundle savedInstanceState) {
        mWorks = new ArrayList<>();
        mInvs = new ArrayList<>();
        mRefData = null;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initEvent() {
        RxAdapterView.itemSelections(spWork)
                .filter(position -> position.intValue() > 0)
                .subscribe(position -> mPresenter.getInvsByWorkId(mWorks.get(position.intValue()).workId, 0));
    }

    @Override
    public void initData() {
        isOpenLocationType = llLocationType.getVisibility() != View.GONE;
        mPresenter.getWorks(0);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void showWorks(List<WorkEntity> works) {
        mWorks.clear();
        mWorks.addAll(works);
    }

    @Override
    public void loadWorksFail(String message) {
        showMessage(message);
        mWorks.clear();
        if (mWorkAdapter != null) {
            mWorkAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadWorksComplete() {
        //绑定适配器
        if (mWorkAdapter == null) {
            mWorkAdapter = new WorkAdapter(mActivity, R.layout.item_simple_sp, mWorks);
            spWork.setAdapter(mWorkAdapter);
        } else {
            mWorkAdapter.notifyDataSetChanged();
        }

        //如果打开了仓储类型，在工厂初始化完毕后获取仓储类型数据源
        if (isOpenLocationType) {
            mPresenter.getDictionaryData("locationType");
        }
    }

    @Override
    public void showInvs(List<InvEntity> invs) {
        mInvs.clear();
        mInvs.addAll(invs);

    }

    @Override
    public void loadInvsFail(String message) {
        showMessage(message);
        mInvs.clear();
        if (mInvAdapter != null) {
            mInvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadInvsComplete() {
        if (mInvAdapter == null) {
            mInvAdapter = new InvAdapter(mActivity, R.layout.item_simple_sp, mInvs);
            spInv.setAdapter(mInvAdapter);
        } else {
            mInvAdapter.notifyDataSetChanged();
        }
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
    public void loadDictionaryDataFail(String message) {
        showMessage(message);
    }


    @Override
    public void _onPause() {
        super._onPause();
        if (spWork.getSelectedItemPosition() <= 0 || spInv.getSelectedItemPosition() <= 0)
            return;
        if (mRefData == null) {
            mRefData = new ReferenceEntity();
        }
        mRefData.bizType = mBizType;
        mRefData.materialGroup = getString(etMaterialClass);
        mRefData.materialDesc = getString(etMaterialDesc);
        mRefData.workId = mWorks.get(spWork.getSelectedItemPosition()).workId;
        mRefData.workCode = mWorks.get(spWork.getSelectedItemPosition()).workCode;
        mRefData.invId = mInvs.get(spInv.getSelectedItemPosition()).invId;
        mRefData.invCode = mInvs.get(spInv.getSelectedItemPosition()).invCode;
        //仓储类型
        if (isOpenLocationType)
            mRefData.locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
    }
}