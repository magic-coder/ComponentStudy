package com.richfit.module_mcq.module_as;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Spinner;

import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_mcq.R;
import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * 物资上架抬头界面
 * Created by monday on 2017/8/30.
 */

public class MCQASHeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    //供应商评价
    Spinner spSupplierEvaluation;
    List<SimpleEntity> items;

    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_as_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }


    @Override
    public void initView() {
        super.initView();
        spSupplierEvaluation = mView.findViewById(R.id.mcq_sp_supplier_evaluation);
        //工厂
        llSendWork.setVisibility(View.VISIBLE);
        tvSendWorkName.setText("工厂");
        //创建人
        llCreator.setVisibility(View.VISIBLE);
        //供应商
        llSupplier.setVisibility(View.VISIBLE);

    }

    @Override
    public void initData() {
        super.initData();
        //初始化供应商评价
        items  = new ArrayList<>();
        SimpleEntity simpleEntity = new SimpleEntity();
        simpleEntity.name = "很好";
        simpleEntity.code = "01";
        items.add(simpleEntity);

        simpleEntity = new SimpleEntity();
        simpleEntity.name = "好";
        simpleEntity.code = "02";
        items.add(simpleEntity);


        simpleEntity = new SimpleEntity();
        simpleEntity.name = "一般";
        simpleEntity.code = "03";
        items.add(simpleEntity);

        simpleEntity = new SimpleEntity();
        simpleEntity.name = "差";
        simpleEntity.code = "04";
        items.add(simpleEntity);

        simpleEntity = new SimpleEntity();
        simpleEntity.name = "很差";
        simpleEntity.code = "05";
        items.add(simpleEntity);

        SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, items,false);
        spSupplierEvaluation.setAdapter(adapter);

        //// TODO: 2017/8/31 测试使用的单据号
        etRefNum.setText("1-20160816-00065");
    }

    @Override
    public void initDataLazily() {

    }


    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }

    @Override
    public void _onPause() {
        super._onPause();
        if(mRefData != null) {
            mRefData.supplierEvaluation = items.get(spSupplierEvaluation.getSelectedItemPosition()).code;
        }
    }
}
