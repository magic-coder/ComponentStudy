package com.richfit.sdk_wzrk.base_asn_head;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.richfit.common_lib.lib_adapter.WorkAdapter;
import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadFragment;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.sdk_wzrk.R;
import com.richfit.sdk_wzrk.R2;
import com.richfit.sdk_wzrk.adapter.MoveTypeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 无参考物资入库抬头(这里先不在封装成基类)
 * Created by monday on 2016/11/16.
 */

public abstract class BaseASNHeadFragment<P extends IASNHeadPresenter> extends BaseHeadFragment<P>
        implements IASNHeadView {

    @BindView(R2.id.sp_work)
    protected Spinner spWork;
    @BindView(R2.id.sp_move_type)
    Spinner spMoveType;
    @BindView(R2.id.ll_supplier)
    protected LinearLayout llSupplier;
    @BindView(R2.id.et_supplier)
    AutoCompleteTextView etSupplier;
    @BindView(R2.id.et_transfer_date)
    protected RichEditText etTransferDate;

    //工厂类型
    private WorkAdapter mWorkAdapter;
    private MoveTypeAdapter mMoveTypeAdapter;
    protected ArrayList<WorkEntity> mWorks;
    private ArrayList<String> mMoveTypes;
    private ArrayList<SimpleEntity> mSuppliers;

    @Override
    protected int getContentId() {
        return R.layout.wzrk_fragment_asn_head;
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        mWorks = new ArrayList<>();
        mMoveTypes = new ArrayList<>();
        mRefData = null;
    }

    @Override
    protected void initView() {
        etTransferDate.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE1));
    }

    @Override
    public void initEvent() {
        //选择日期
        etTransferDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etTransferDate, Global.GLOBAL_DATE_PATTERN_TYPE1));

        //选择工厂获取供应商
        RxAdapterView.itemSelections(spWork)
                .filter(position -> position.intValue() > 0 && llSupplier.getVisibility() != View.GONE)
                .subscribe(position -> mPresenter.getSupplierList(mWorks.get(position.intValue()).workCode,
                        getString(etSupplier), 20, 0));

        //供应商
        RxView.clicks(etSupplier)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> showAutoCompleteConfig(etSupplier));

        //选择供应商
        RxAutoCompleteTextView.itemClickEvents(etSupplier)
                .subscribe(a -> hideKeyboard(etSupplier));

        //删除缓存
        mPresenter.deleteCollectionData("", mBizType, Global.USER_ID, mCompanyCode);
    }

    @Override
    public void initData() {
        SPrefUtil.saveData(mBizType, "0");
        //初始化工厂
        mPresenter.getWorks(0);
    }

    @Override
    public void showWorks(ArrayList<WorkEntity> works) {
        if (mWorkAdapter == null) {
            mWorks.clear();
            mWorks.addAll(works);
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
    public void loadWorksComplete() {
        //初始化移动类型
        mPresenter.getMoveTypeList(0);
    }

    @Override
    public void showMoveTypes(ArrayList<String> moveTypes) {
        if (mMoveTypeAdapter == null) {
            mMoveTypes.clear();
            mMoveTypes.addAll(moveTypes);
            mMoveTypeAdapter = new MoveTypeAdapter(mActivity, R.layout.item_simple_sp, mMoveTypes);
            spMoveType.setAdapter(mMoveTypeAdapter);
        } else {
            mMoveTypeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadMoveTypesFail(String message) {
        showMessage(message);
    }

    @Override
    public void showSuppliers(Map<String, List<SimpleEntity>> map) {
        List<SimpleEntity> simpleEntities = map.get(Global.SUPPLIER_DATA);
        if (simpleEntities == null || simpleEntities.size() == 0) {
            return;
        }
        if (mSuppliers == null) {
            mSuppliers = new ArrayList<>();
        }
        List<String> suppliers = CommonUtil.toStringArray(simpleEntities);
        //注意后续需要供应商的id
        mSuppliers.clear();
        mSuppliers.addAll(simpleEntities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity,
                R.layout.simple_auto_edittext_item1, suppliers);
        etSupplier.setAdapter(adapter);
        setAutoCompleteConfig(etSupplier);
    }

    @Override
    public void loadSuppliersFail(String message) {
        showMessage(message);
    }

    @Override
    public void deleteCacheSuccess(String message) {
        showMessage(message);
    }

    @Override
    public void deleteCacheFail(String message) {
        showMessage(message);
    }

    @Override
    public void _onPause() {
        if (checkData()) {
            if (mRefData == null)
                mRefData = new ReferenceEntity();

            //发出工厂(工厂)
            if (mWorks != null && mWorks.size() > 0 && spWork.getAdapter() != null) {
                final int position = spWork.getSelectedItemPosition();
                mRefData.workCode = mWorks.get(position).workCode;
                mRefData.workName = mWorks.get(position).workName;
                mRefData.workId = mWorks.get(position).workId;
            }

            //移动类型
            if (mMoveTypes != null && mMoveTypes.size() > 0 && spMoveType.getAdapter() != null) {
                final int position = spMoveType.getSelectedItemPosition();
                mRefData.moveType = mMoveTypes.get(position);
            }
            //过账日期
            mRefData.voucherDate = getString(etTransferDate);
            //业务类型
            mRefData.bizType = mBizType;
            if (llSupplier.getVisibility() != View.GONE && mSuppliers != null && mSuppliers.size() > 0) {
                final String selectedSupplier = getString(etSupplier);
                if (!TextUtils.isEmpty(selectedSupplier)) {
                    final String supplierCode = selectedSupplier.split("_")[0];
                    for (SimpleEntity entity : mSuppliers) {
                        if (supplierCode.equalsIgnoreCase(entity.code)) {
                            mRefData.supplierId = entity.id;
                        }
                    }
                }
            }
        } else {
            mRefData = null;
        }
    }

    protected boolean checkData() {
        //检查是否填写了必要的字段
        if (spWork.getSelectedItemPosition() == 0)
            return false;
        return true;
    }

    @Override
    public boolean isNeedShowFloatingButton() {
        return false;
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {

    }
}
