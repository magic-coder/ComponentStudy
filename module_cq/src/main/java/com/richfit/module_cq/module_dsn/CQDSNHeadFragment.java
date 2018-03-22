package com.richfit.module_cq.module_dsn;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzck.base_dsn_head.BaseDSNHeadFragment;
import com.richfit.sdk_wzck.base_dsn_head.imp.DSNHeadPresenterImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by monday on 2017/12/8.
 */

public class CQDSNHeadFragment extends BaseDSNHeadFragment<DSNHeadPresenterImp> {

    Spinner spMoveCause;
    Spinner spRefCompany;
    TextView tvCreator;
    EditText etOrderNum;
    AutoCompleteTextView etProjectNum;

    List<SimpleEntity> mMoveCauseList;
    List<SimpleEntity> mRefCompanyList;
    List<String> mProjectNumList;

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        super.initVariable(savedInstanceState);
        mMoveCauseList = new ArrayList<>();
        mRefCompanyList = new ArrayList<>();
    }

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_dsn_head;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DSNHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        spMoveCause = mView.findViewById(R.id.sp_move_cause);
        spRefCompany = mView.findViewById(R.id.cqzt_sp_req_company);
        tvCreator = mView.findViewById(R.id.tv_creator);
        tvCreator.setText(Global.LOGIN_ID);
        etOrderNum = mView.findViewById(R.id.et_order_num);
        etProjectNum = mView.findViewById(R.id.et_project_num);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        //选择工厂，初始化成本中心和编号
        RxAdapterView.itemSelections(spWork)
                .filter(position -> position.intValue() > 0)
                .doOnNext(a -> mDropDown.put(etAutoComp.getId(), false))
                .doOnNext(a -> mDropDown.put(etProjectNum.getId(), false))
                .subscribe(position ->
                        mPresenter.getAutoComList(mWorks.get(position).workCode,null,
                                getString(etAutoComp), 100, getOrgFlag(),
                                Global.COST_CENTER_DATA, Global.PROJECT_NUM_DATA)
                );
        //点击自动提示控件，显示默认列表
        RxView.clicks(etProjectNum)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .filter(a -> mProjectNumList != null && mProjectNumList.size() > 0)
                .subscribe(a -> showAutoCompleteConfig(etProjectNum));

        //用户选择自动提示控件的某一条数据，隐藏输入法
        RxAutoCompleteTextView.itemClickEvents(etProjectNum)
                .subscribe(a -> hideKeyboard(etProjectNum));

        //修改自动提示控件，说明用户需要用关键字进行搜索，如果默认的列表中存在，那么不在向数据库进行查询
        RxTextView.textChanges(etProjectNum)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(str -> !TextUtils.isEmpty(str) && !filterKeyWord(str, mProjectNumList))
                .doOnNext(a -> mDropDown.put(etProjectNum.getId(), true))
                .subscribe(a ->  mPresenter.getAutoComList(mWorks.get(spWork.getSelectedItemPosition()).workCode,null,
                        getString(etProjectNum), 100, getOrgFlag(), Global.PROJECT_NUM_DATA));

    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    public void loadWorkComplete() {
        //加载移动原因和领料单位
        mPresenter.getDictionaryData("moveCause","reqCompany");
    }

    @Override
    public void showAutoCompleteList(Map<String,List<SimpleEntity>> map) {
        super.showAutoCompleteList(map);
        List<SimpleEntity> projectNumList = map.get(Global.PROJECT_NUM_DATA);
        if(projectNumList != null && projectNumList.size() > 0) {
            List<String> projectNums=  CommonUtil.toStringArray(projectNumList,true);
            if(mProjectNumList == null) {
                mProjectNumList = new ArrayList<>();
            }
            mProjectNumList.clear();
            mProjectNumList.addAll(projectNums);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity,
                    android.R.layout.simple_dropdown_item_1line, mProjectNumList);
            etProjectNum.setAdapter(adapter);
            setAutoCompleteConfig(etProjectNum);
            boolean isDropDownGlAccount = CommonUtil.convertToBoolean(mDropDown.get(etProjectNum.getId()), false);
            if (isDropDownGlAccount) {
                showAutoCompleteConfig(etProjectNum);
            }
        }
    }

    @Override
    public void loadAutoCompleteFail(String message) {
       super.loadAutoCompleteFail(message);
       hideKeyboard(etProjectNum);
    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        super.loadDictionaryDataSuccess(data);
        List<SimpleEntity> moveCauseList = data.get("moveCause");
        if (moveCauseList != null) {
            mMoveCauseList.clear();
            mMoveCauseList.addAll(moveCauseList);
            SimpleAdapter inspectionTypeAdapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp,
                    moveCauseList,false);
            spMoveCause.setAdapter(inspectionTypeAdapter);
        }

        List<SimpleEntity> refCompanyList = data.get("reqCompany");
        if (refCompanyList != null) {
            mRefCompanyList.clear();
            mRefCompanyList.addAll(refCompanyList);
            SimpleAdapter packageAdapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp,
                    refCompanyList,false);
            spRefCompany.setAdapter(packageAdapter);
        }
    }

    @Override
    public void _onPause() {
        super._onPause();
        if(mRefData != null) {
            //成本中心
            String costCenter = getString(etAutoComp);
            if (!TextUtils.isEmpty(costCenter)) {
                mRefData.costCenter = costCenter.split("_")[0];
            }
            //WBS编号
            String projectNum = getString(etProjectNum);
            if (!TextUtils.isEmpty(projectNum)) {
                mRefData.projectNum = projectNum.split("_")[0];
            }

            //orderNum
            mRefData.orderNum = getString(etOrderNum);

            //移动原因和领料单位
            if(mMoveCauseList != null && mMoveCauseList.size() > 0) {
                mRefData.moveCause = mMoveCauseList.get(spMoveCause.getSelectedItemPosition()).code;
            }
            if(mRefCompanyList != null && mRefCompanyList.size() > 0) {
                mRefData.reqCompany = mRefCompanyList.get(spRefCompany.getSelectedItemPosition()).code;
            }
        }
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    protected String getAutoComDataType() {
        return Global.COST_CENTER_DATA;
    }
}
