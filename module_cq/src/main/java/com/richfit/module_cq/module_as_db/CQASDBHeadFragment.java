package com.richfit.module_cq.module_as_db;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.Spinner;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.lib_adapter.WorkAdapter;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * 增加收货工厂和收货库存地点，作为采集接界面的显示invCode,workCode
 * Created by monday on 2017/12/5.
 */

public class CQASDBHeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    Spinner spWork;
    Spinner spInv;
    //管理费率	glf
    EditText etGlf;
    //运杂费		lyf
    EditText etLyf;
    //仓储费		ckf
    EditText etCkf;
    //其他费用	yfhj
    EditText etYfhj;


    List<WorkEntity> mWorks;
    List<InvEntity> mInvs;

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        super.initVariable(savedInstanceState);
        mWorks = new ArrayList<>();
        mInvs = new ArrayList<>();
    }

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_asdb_head;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    public void initEvent() {
        super.initEvent();
        //发出工厂
        RxAdapterView.itemSelections(spWork)
                .filter(position -> position.intValue() > 0)
                .subscribe(position -> mPresenter.getInvsByWorkId(mWorks.get(position.intValue()).workId, 0));
    }

    @Override
    public void initView() {
        super.initView();
        spWork = mView.findViewById(R.id.sp_work);
        spInv = mView.findViewById(R.id.sp_inv);
        etGlf = mView.findViewById(R.id.cqzt_et_glf);
        etLyf = mView.findViewById(R.id.cqzt_et_lyf);
        etCkf = mView.findViewById(R.id.cqzt_et_ckf);
        etYfhj = mView.findViewById(R.id.cqzt_et_yfhj);
    }

    @Override
    public void initData() {
        super.initData();
        //获取收货工厂和收货库存地点
        mPresenter.getWorks(0);
    }

    @Override
    public void showWorks(List<WorkEntity> works) {
        mWorks.clear();
        mWorks.addAll(works);
        //绑定适配器
        WorkAdapter adapter = new WorkAdapter(mActivity, com.richfit.sdk_wzyk.R.layout.item_simple_sp, mWorks);
        spWork.setAdapter(adapter);
    }

    /**
     * 发出库存地点
     *
     * @param sendInvs
     */
    @Override
    public void showInvs(List<InvEntity> sendInvs) {
        mInvs.clear();
        mInvs.addAll(sendInvs);
        InvAdapter adapter = new InvAdapter(mActivity, R.layout.item_simple_sp, mInvs);
        spInv.setAdapter(adapter);
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            if(mWorks != null && mWorks.size() > 0 &&  spWork.getAdapter() != null && spWork.getSelectedItemPosition() > 0) {
                mRefData.workId = mWorks.get(spWork.getSelectedItemPosition()).workId;
                mRefData.workCode = mWorks.get(spWork.getSelectedItemPosition()).workCode;
                mRefData.workName = mWorks.get(spWork.getSelectedItemPosition()).workName;
            }
            if(mInvs != null && mInvs.size() > 0 &&  spInv.getAdapter() != null && spInv.getSelectedItemPosition() > 0) {
                mRefData.invId = mInvs.get(spInv.getSelectedItemPosition()).invId;
                mRefData.invCode = mInvs.get(spInv.getSelectedItemPosition()).invCode;
                mRefData.invName = mInvs.get(spInv.getSelectedItemPosition()).invName;
            }
            //将每一个行信息的工厂和库位修改
            if(mRefData.billDetailList != null && mRefData.billDetailList.size() > 0) {
                for (RefDetailEntity item : mRefData.billDetailList) {
                    item.workId =mRefData.workId ;
                    item.workCode =mRefData.workCode;
                    item.workName =mRefData.workName;
                    item.invId = mRefData.invId;
                    item.invCode = mRefData.invCode;
                    item.invName =mRefData.invName;
                }
            }

            mRefData.glf = getString(etGlf);
            mRefData.lyf = getString(etLyf);
            mRefData.ckf = getString(etCkf);
            mRefData.yfhj = getString(etYfhj);
        }
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }


}
