package com.richfit.module_cqyt.module_as;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 注意这里是获取单据数据后，通过单据里面的工厂信息初始化报检单位
 * Created by monday on 2017/6/29.
 */

public class CQYTAS103HeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    //数据字段
    private static final String INSPECTION_UNIT_KEY = "inspectionUnit";
    private static final String INS_STANDARD_KEY = "INS_STANDARD";

    EditText etDeliveryOrder;
    //报检单位
    Spinner spDeclaredUnit;
    EditText etTeam;
    EditText etPost;
    RichEditText etArrivalDate;
    RichEditText etInspectionDate;
    EditText etManufacture;
    //检验单位
    Spinner spInspectionUnit;
    EditText etRemark;
    //检验标准(必输)
    AutoCompleteTextView etInspectionStandard;

    List<SimpleEntity> mInspectionUnits;
    List<InvEntity> mDeclaredUnits;
    InvAdapter mInvAdapter;

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_as103_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }


    @Override
    public void initView() {
        super.initView();
        etDeliveryOrder = (EditText) mView.findViewById(R.id.cqyt_et_delivery_order);
        spDeclaredUnit = (Spinner) mView.findViewById(R.id.cqyt_sp_declared_unit);
        etTeam = (EditText) mView.findViewById(R.id.cqyt_et_class);
        etPost = (EditText) mView.findViewById(R.id.cqyt_et_post);
        etArrivalDate = (RichEditText) mView.findViewById(R.id.et_arrival_date);
        etInspectionDate = (RichEditText) mView.findViewById(R.id.cqyt_et_inspection_date);
        etManufacture = (EditText) mView.findViewById(R.id.cqyt_et_manufacturer);
        spInspectionUnit = (Spinner) mView.findViewById(R.id.cqyt_sp_inspection_unit);
        etRemark = (EditText) mView.findViewById(R.id.et_remark);
        etInspectionStandard = (AutoCompleteTextView) mView.findViewById(R.id.cqyt_et_inspection_standard);

        llSupplier.setVisibility(View.VISIBLE);
        llSendWork.setVisibility(View.GONE);
    }


    @Override
    public void initEvent() {
        super.initEvent();
        //提货单修改成日期格式，默认当天
        etDeliveryOrder.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE1));
        etArrivalDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etArrivalDate, Global.GLOBAL_DATE_PATTERN_TYPE1));

        etInspectionDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etInspectionDate, Global.GLOBAL_DATE_PATTERN_TYPE1));

        //选中上架仓位列表的item，关闭输入法,并且直接匹配出仓位数量
        RxAutoCompleteTextView.itemClickEvents(etInspectionStandard)
                .delay(50, TimeUnit.MILLISECONDS)
                .filter(a -> etInspectionStandard.getAdapter() != null)
                .subscribe(a -> hideKeyboard(etInspectionStandard));

        //点击自动提示控件，显示默认列表
        RxView.clicks(etInspectionStandard)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .filter(a -> etInspectionStandard.getAdapter() != null)
                .subscribe(a -> showAutoCompleteConfig(etInspectionStandard));
    }

    @Override
    public void initData() {
        super.initData();
        etArrivalDate.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE1));
        etInspectionDate.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE1));
        //直接初始化检验单位
        mPresenter.getDictionaryData(INSPECTION_UNIT_KEY, INS_STANDARD_KEY);
    }

    @Override
    public void loadInvsFail(String message) {
        showMessage(message);
    }

    @Override
    public void showInvs(List<InvEntity> list) {
        if (mDeclaredUnits == null) {
            mDeclaredUnits = new ArrayList<>();
        }
        mDeclaredUnits.clear();
        mDeclaredUnits.addAll(list);
        if (mInvAdapter == null) {
            mInvAdapter = new InvAdapter(mActivity, R.layout.item_simple_sp, mDeclaredUnits);
            spDeclaredUnit.setAdapter(mInvAdapter);
        } else {
            mInvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadInvsComplete() {
        //报检单位和检验标准，注意检验标准采用的是auto提示
        ArrayList<String> items = new ArrayList<>();
        for (InvEntity item : mDeclaredUnits) {
            items.add(item.invCode);
        }
        UiUtil.setSelectionForSp(items, mRefData.declaredUnit, spDeclaredUnit);
    }

    /**
     * 字典查询完毕，初始化检验单位
     *
     * @param data
     */
    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        //初始化检验单位
        if (mInspectionUnits == null) {
            mInspectionUnits = new ArrayList<>();
        }
        mInspectionUnits.clear();
        List<SimpleEntity> inspectionUnitList = data.get(INSPECTION_UNIT_KEY);
        if (inspectionUnitList != null && inspectionUnitList.size() > 0) {
            //增加请选择
            SimpleEntity tmp = new SimpleEntity();
            tmp.name = "请选择";
            mInspectionUnits.add(tmp);
            mInspectionUnits.addAll(inspectionUnitList);
        }
        SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, mInspectionUnits);
        spInspectionUnit.setAdapter(adapter);
        //初始化检验标准
        List<SimpleEntity> inspectionStandardList = data.get(INS_STANDARD_KEY);
        if (inspectionStandardList != null && inspectionStandardList.size() > 0) {
            ArrayList<String> items = new ArrayList<>();
            for (SimpleEntity simpleEntity : inspectionStandardList) {
                items.add(simpleEntity.code + "_" + simpleEntity.name);
            }
            ArrayAdapter<String> insStandardAdapter = new ArrayAdapter<>(mActivity,
                    android.R.layout.simple_dropdown_item_1line, items);
            etInspectionStandard.setAdapter(insStandardAdapter);
            setAutoCompleteConfig(etInspectionStandard);
        }
    }


    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            mRefData.deliveryOrder = getString(etDeliveryOrder);
            //报检单位
            mRefData.declaredUnit = mDeclaredUnits.get(spDeclaredUnit.getSelectedItemPosition()).invCode;
            //班
            mRefData.team = getString(etTeam);
            //岗位
            mRefData.post = getString(etPost);
            //生产厂家
            mRefData.manufacture = getString(etManufacture);
            //检验单位
            mRefData.inspectionUnit = mInspectionUnits.get(spInspectionUnit.getSelectedItemPosition()).code;
            //到货时间
            mRefData.arrivalDate = getString(etArrivalDate);
            //报检时间
            mRefData.inspectionDate = getString(etInspectionDate);
            //检验标准
            mRefData.inspectionStandard = getString(etInspectionStandard);
            //备注
            mRefData.remark = getString(etRemark);
        }
    }

    @Override
    public void bindCommonHeaderUI() {
        super.bindCommonHeaderUI();
        if (mRefData != null) {
            //提货单
            if (!TextUtils.isEmpty(mRefData.deliveryOrder)) {
                etDeliveryOrder.setText(mRefData.deliveryOrder);
            }
            //报检单位
            mPresenter.getInvsByWorkId("7A49176ABA82FBE4C5FAE60BA8A14079", 0);
            //班
            etTeam.setText(mRefData.team);
            //岗位
            etPost.setText(mRefData.post);
            //生产厂家
            if (TextUtils.isEmpty(mRefData.manufacture)) {
                etManufacture.setText(mRefData.supplierNum + "_" + mRefData.supplierDesc);
            } else {
                etManufacture.setText(mRefData.manufacture);
            }
            //检验单位
            UiUtil.setSelectionForSimpleSp(mInspectionUnits, mRefData.inspectionUnit, spInspectionUnit);
            //备注
            etRemark.setText(mRefData.remark);
            //检验标准
            etInspectionStandard.setText(mRefData.inspectionStandard);
        }
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {
        super.clearAllUIAfterSubmitSuccess();
        clearCommonUI(etDeliveryOrder, etTeam, etPost, etManufacture,
                etRemark, etInspectionStandard);
        if (spInspectionUnit.getAdapter() != null) {
            spInspectionUnit.setSelection(0);
        }
        if (spDeclaredUnit.getAdapter() != null) {
            spDeclaredUnit.setSelection(0);
        }
    }
}
