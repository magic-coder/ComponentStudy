package com.richfit.module_cqyt.module_as;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InvEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/6/29.
 */

public class CQYTAS103HeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

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
    EditText etInspectionStandard;
    List<String> mInspectionUnits;
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
        etInspectionStandard = (EditText) mView.findViewById(R.id.cqyt_et_inspection_standard);

        llSupplier.setVisibility(View.VISIBLE);
        llSendWork.setVisibility(View.GONE);
    }


    @Override
    public void initEvent() {
        super.initEvent();
        etArrivalDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etArrivalDate, Global.GLOBAL_DATE_PATTERN_TYPE1));

        etInspectionDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etInspectionDate, Global.GLOBAL_DATE_PATTERN_TYPE1));
    }

    @Override
    public void initData() {
        super.initData();
        etArrivalDate.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE1));
        etInspectionDate.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE1));
        mInspectionUnits = new ArrayList<>();
        mInspectionUnits.add("物资供应处商检所");
        mInspectionUnits.add("中国石油天然气集团公司管材研究所");
        mInspectionUnits.add("长庆油田分公司技术监测中心");
        mInspectionUnits.add("西安摩尔石油工程实验室");
        mInspectionUnits.add("西安未来检测技术有限公司");
        mInspectionUnits.add("西安三维应力工程技术有限公司");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, mInspectionUnits);
        spInspectionUnit.setAdapter(adapter);
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
            mRefData.inspectionUnit = "00000000" + String.valueOf(spInspectionUnit.getSelectedItemPosition() + 1);
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
            etDeliveryOrder.setText(mRefData.deliveryOrder);
            //报检单位
            mPresenter.getInvsByWorkId(mRefData.billDetailList.get(0).workId, 0);
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
            UiUtil.setSelectionForSp(mInspectionUnits, mRefData.inspectionUnit, spInspectionUnit);
            //备注
            etRemark.setText(mRefData.remark);
            //检验标准
            etInspectionStandard.setText(mRefData.inspectionStandard);
        }
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
        //报检单位
        ArrayList<String> items = new ArrayList<>();
        for (InvEntity item : mDeclaredUnits) {
            items.add(item.invCode);
        }
        UiUtil.setSelectionForSp(items, mRefData.declaredUnit, spDeclaredUnit);
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
