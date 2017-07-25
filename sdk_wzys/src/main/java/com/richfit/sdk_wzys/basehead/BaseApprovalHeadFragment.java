package com.richfit.sdk_wzys.basehead;

import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadFragment;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.sdk_wzys.R;
import com.richfit.sdk_wzys.R2;
import com.richfit.sdk_wzys.basehead.imp.ApprovalHeadPresenterImp;

import butterknife.BindView;

/**
 * 物资验收抬头基类
 * Created by monday on 2016/11/23.
 */

public abstract class BaseApprovalHeadFragment extends BaseHeadFragment<ApprovalHeadPresenterImp>
        implements IApprovalHeadView {

    private static final String[] APPROVAL_TYPES = {"仓库验收", "检测验收"};

    @BindView(R2.id.et_ref_num)
    protected RichEditText etRefNum;
    @BindView(R2.id.tv_ref_num)
    protected TextView tvRefNum;
    @BindView(R2.id.ll_inspection_type)
    protected LinearLayout llInspectionType;
    @BindView(R2.id.sp_approval_type)
    Spinner spApprovalType;
    @BindView(R2.id.tv_supplier)
    protected TextView tvSupplier;
    @BindView(R2.id.tv_approval_name)
    TextView tvApprovalName;
    @BindView(R2.id.et_approval_date)
    protected RichEditText etApprovalDate;
    @BindView(R2.id.ll_arrival_date)
    protected LinearLayout llArrivalDate;
    @BindView(R2.id.et_arrival_date)
    protected RichEditText erArrivalDate;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        if (list != null && list.length >= 1) {
            getRefData(list[0]);
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.wzys_fragment_base_approval_header;
    }

    @Override
    public void initVariable(Bundle savedInstanceState) {
        mRefData = null;
    }

    @Override
    protected void initView() {
        etApprovalDate.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE1));
        erArrivalDate.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE1));
    }

    /**
     * 绑定事件。
     */
    @Override
    public void initEvent() {
        //请求单据信息
        etRefNum.setOnRichEditTouchListener((view, refNum) -> {
            hideKeyboard(view);
            getRefData(refNum);
        });

        //修改验收日期
        etApprovalDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etApprovalDate, Global.GLOBAL_DATE_PATTERN_TYPE1));

        //到货日期
        erArrivalDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etApprovalDate, Global.GLOBAL_DATE_PATTERN_TYPE1));
    }

    @Override
    public void initData() {
        ArrayAdapter adapter = new ArrayAdapter(mActivity, R.layout.item_simple_sp, APPROVAL_TYPES);
        spApprovalType.setAdapter(adapter);
    }

    protected void getRefData(String refNum) {
        mRefData = null;
        clearAllUI();
        mPresenter.getReference(refNum, mRefType, mBizType, getMoveType(), "", Global.USER_ID);
    }

    @Override
    public void getReferenceSuccess(ReferenceEntity refData) {
        //过账标识，如果已经过账，那么不允许在明细刷新数据，也不运行在采集界面采集数据
        SPrefUtil.saveData(mBizType + mRefType, "0");
        refData.bizType = mBizType;
        refData.moveType = getMoveType();
        refData.refType = mRefType;
        mRefData = refData;
        cacheProcessor(mRefData.tempFlag, mRefData.transId, mRefData.refCodeId, mRefData.recordNum,
                mRefData.refType, mRefData.bizType);
    }

    @Override
    public void getReferenceFail(String message) {
        showMessage(message);
        mRefData = null;
        //清除所有控件绑定的数据
        clearAllUI();
    }

    /**
     * 检查数据库是否存在该历史数据.
     * 如果有缓存提示用户是否删除缓存。
     * 如果用户点击确定删除那么删除缓存，并刷新界面；
     * 如果用户点击取消删除那么直接刷新界面，在采集界面和明细界面会重新获取缓存
     *
     * @param cacheFlag：缓存标志。有可能是Y,N或者TransId标识
     * @param transId：缓存id,用于删除缓存
     * @param refNum：单据号
     * @param bizType：业务类型
     */
    @Override
    public void cacheProcessor(String cacheFlag, String transId, String refCodeId, String refNum, String refType, String bizType) {
        if (!TextUtils.isEmpty(cacheFlag) && "Y".equals(cacheFlag)) {
            android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(mActivity);
            dialog.setTitle("提示");
            dialog.setIcon(R.mipmap.icon_tips);
            dialog.setMessage(getString(R.string.msg_has_history));
            dialog.setPositiveButton("确定", (dia, which) -> {
                dia.dismiss();
                mPresenter.deleteCollectionData(refNum, transId, refCodeId, refType, bizType, Global.USER_ID, mCompanyCode);
            });
            dialog.setNegativeButton("取消", (dia, which) -> {
                dia.dismiss();
                bindCommonHeaderUI();
            });
            dialog.show();
        } else {
            bindCommonHeaderUI();
        }
    }

    @Override
    public void deleteCacheSuccess() {
        showMessage("缓存删除成功");
        bindCommonHeaderUI();
    }

    @Override
    public void deleteCacheFail(String message) {
        showMessage(message);
        bindCommonHeaderUI();
    }

    @Override
    public void bindCommonHeaderUI() {
        if (mRefData != null) {
            //更新UI
            tvRefNum.setText(mRefData.recordNum);
            tvSupplier.setText(mRefData.supplierNum + "_" + mRefData.supplierDesc);
            tvApprovalName.setText(Global.LOGIN_ID);
        }
    }

    @Override
    public void clearAllUI() {
        clearCommonUI(tvRefNum, tvSupplier, tvApprovalName);
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {
        clearAllUI();
        clearCommonUI(etRefNum);
        mRefData = null;
    }


    @Override
    public void _onPause() {
        super._onPause();
        //再次检查用户是否输入的额外字段而且必须输入的字段（情景是用户请求单据之前没有输入该字段，回来填上后，但是没有请求单据而是直接）
        //切换了页面
        if (mRefData != null) {
            mRefData.voucherDate = getString(etApprovalDate);
            mRefData.inspectionType = spApprovalType.getSelectedItemPosition() + 1;
        }
    }

    /**
     * 网络错误重试
     *
     * @param action
     */
    @Override
    public void retry(String action) {
        switch (action) {
            case Global.RETRY_LOAD_REFERENCE_ACTION:
                mPresenter.getReference(getString(etRefNum), mRefType, mBizType, getMoveType(), "", Global.LOGIN_ID);
                break;
        }
        super.retry(action);
    }

    @Override
    public boolean isNeedShowFloatingButton() {
        return false;
    }


    /*返回移动类型*/
    @CheckResult
    @NonNull
    protected abstract String getMoveType();
}
