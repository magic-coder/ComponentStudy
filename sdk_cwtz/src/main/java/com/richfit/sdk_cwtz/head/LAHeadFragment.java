package com.richfit.sdk_cwtz.head;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Spinner;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.lib_adapter.WorkAdapter;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.sdk_cwtz.R;
import com.richfit.sdk_cwtz.R2;
import com.richfit.sdk_cwtz.head.imp.LAHeadPresenterImp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 仓位调整抬头界面
 * Created by monday on 2017/2/7.
 */

public class LAHeadFragment extends BaseFragment<LAHeadPresenterImp>
        implements ILAHeadView {

    @BindView(R2.id.sp_work)
    protected Spinner spWork;
    @BindView(R2.id.sp_inv)
    protected Spinner spInv;

    protected List<WorkEntity> mWorks;
    protected  List<InvEntity> mInvs;

    WorkAdapter mWorkAdapter;
    InvAdapter mInvAdapter;

    @Override
    protected int getContentId() {
        return R.layout.cwtz_fragment_la_header;
    }

    @Override
    public void initPresenter() {
        mPresenter = new LAHeadPresenterImp(mActivity);
    }


    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        mRefData = null;
        mWorks = new ArrayList<>();
        mInvs = new ArrayList<>();
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initEvent() {
        RxAdapterView.itemSelections(spWork)
                .filter(position -> position.intValue() > 0)
                .subscribe(position -> mPresenter.getInvsByWorkId(mWorks.get(position.intValue()).workId,0));

        /*2017年06月14日，增加WMFlag字段控制是否打开WM仓位。如果打开了采取获取StorageNum*/
        RxAdapterView.itemSelections(spInv)
                .filter(position -> position.intValue() > 0)
                .filter(position -> "Y".equalsIgnoreCase(Global.WMFLAG))
                .subscribe(a -> mPresenter.getStorageNum(mWorks.get(spWork.getSelectedItemPosition()).workId,
                        mWorks.get(spWork.getSelectedItemPosition()).workCode,
                        mInvs.get(spInv.getSelectedItemPosition()).invId,
                        mInvs.get(spInv.getSelectedItemPosition()).invCode));
    }

    @Override
    public void initData() {
        //获取发出工厂列表
        mPresenter.getWorks(0);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void showWorks(List<WorkEntity> works) {
        mWorks.clear();
        mWorks.addAll(works);
        if (mWorkAdapter == null) {
            mWorkAdapter = new WorkAdapter(mActivity, R.layout.item_simple_sp, mWorks);
            spWork.setAdapter(mWorkAdapter);
        } else {
            mWorkAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadWorksFail(String message) {
        showMessage(message);
    }

    @Override
    public void showInvs(List<InvEntity> invs) {
        mInvs.clear();
        mInvs.addAll(invs);
        if (mInvAdapter == null) {
            mInvAdapter = new InvAdapter(mActivity, R.layout.item_simple_sp, mInvs);
            spInv.setAdapter(mInvAdapter);
        } else {
            mInvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadInvsFail(String message) {
        showMessage(message);
    }

    @Override
    public void loadInvsComplete() {

    }

    @Override
    public void showProjectNums(ArrayList<String> projectNums) {

    }

    @Override
    public void loadProjectNumsFail(String message) {

    }

    @Override
    public void getStorageNumSuccess(String storageNum) {
        if (mRefData == null)
            mRefData = new ReferenceEntity();
        mRefData.storageNum = storageNum;
    }

    @Override
    public void getStorageNumFail(String message) {
        showMessage(message);
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (checkData()) {
            if (mRefData == null)
                mRefData = new ReferenceEntity();

            if (mWorks != null && mWorks.size() > 0 && spWork.getAdapter() != null) {
                final int position = spWork.getSelectedItemPosition();
                mRefData.workCode = mWorks.get(position).workCode;
                mRefData.workName = mWorks.get(position).workName;
                mRefData.workId = mWorks.get(position).workId;
            }

            if (mInvs != null && mInvs.size() > 0 && spInv.getAdapter() != null) {
                final int position = spInv.getSelectedItemPosition();
                mRefData.invCode = mInvs.get(position).invCode;
                mRefData.invName = mInvs.get(position).invName;
                mRefData.invId = mInvs.get(position).invId;
            }
            mRefData.bizType = mBizType;
        } else {
            mRefData = null;
        }
    }

    protected boolean checkData() {
        //检查是否填写了必要的字段
        if (spWork.getSelectedItemPosition() == 0)
            return false;
        if (spInv.getSelectedItemPosition() == 0)
            return false;
        return true;
    }

    @Override
    public void clearAllUI() {
        spWork.setSelection(0);
        spInv.setSelection(0);
    }

    @Override
    public boolean isNeedShowFloatingButton() {
        return false;
    }
}
