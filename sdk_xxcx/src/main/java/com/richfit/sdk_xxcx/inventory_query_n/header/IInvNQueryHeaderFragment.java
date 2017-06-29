package com.richfit.sdk_xxcx.inventory_query_n.header;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.lib_adapter.WorkAdapter;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.sdk_xxcx.R;
import com.richfit.sdk_xxcx.R2;
import com.richfit.sdk_xxcx.inventory_query_n.header.imp.IInvNQueryHeaderPresenterImp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 物资库存信息查询-无参考
 * Created by monday on 2017/5/25.
 */

public class IInvNQueryHeaderFragment extends BaseFragment<IInvNQueryHeaderPresenterImp>
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


    /*工厂*/
    WorkAdapter mWorkAdapter;
    List<WorkEntity> mWorks;

    /*库位*/
    InvAdapter mInvAdapter;
    List<InvEntity> mInvs;

    @Override
    protected int getContentId() {
        return R.layout.xxcx_fragment_invn_query_header;
    }

    @Override
    public void initPresenter() {
        mPresenter = new IInvNQueryHeaderPresenterImp(mActivity);
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
    }

    @Override
    public void initData() {
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
        mRefData.invId = mInvs.get(spInv.getSelectedItemPosition()).invCode;
        mRefData.invCode = mInvs.get(spInv.getSelectedItemPosition()).invId;
    }
}
