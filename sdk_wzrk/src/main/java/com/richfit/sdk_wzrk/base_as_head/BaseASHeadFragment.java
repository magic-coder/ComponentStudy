package com.richfit.sdk_wzrk.base_as_head;

import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadFragment;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.sdk_wzrk.R;
import com.richfit.sdk_wzrk.R2;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 物资入库抬头界面基类
 * Created by monday on 2016/11/11.
 */

public abstract class BaseASHeadFragment<P extends IASHeadPresenter> extends BaseHeadFragment<P>
        implements IASHeadView {

    @BindView(R2.id.et_ref_num)
    protected RichEditText etRefNum;
    @BindView(R2.id.tv_ref_num)
    TextView tvRefNum;
    @BindView(R2.id.et_transfer_date)
    RichEditText etTransferDate;
    @BindView(R2.id.tv_supplier)
    TextView tvSupplier;
    @BindView(R2.id.tv_send_work)
    TextView tvSendWork;
    @BindView(R2.id.ll_supplier)
    protected LinearLayout llSupplier;
    @BindView(R2.id.ll_send_work)
    protected LinearLayout llSendWork;
    @BindView(R2.id.tv_send_work_name)
    protected TextView tvSendWorkName;
    @BindView(R2.id.ll_creator)
    protected LinearLayout llCreator;
    @BindView(R2.id.tv_creator)
    TextView tvCreator;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        if (list != null && list.length == 1) {
            getRefData(list[0]);
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.wzrk_fragment_base_asy_head;
    }

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        mRefData = null;
    }

    @Override
    protected void initView() {

    }

    /**
     * 注册点击事件
     */
    @Override
    protected void initEvent() {
        //点击单号加载单据数据
        etRefNum.setOnRichEditTouchListener((view, refNum) -> {
            hideKeyboard(view);
            getRefData(refNum);
        });

        //选择过账日期
        etTransferDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etTransferDate, Global.GLOBAL_DATE_PATTERN_TYPE1));
    }

    @Override
    protected void initData() {
        etTransferDate.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE1));
        if (mUploadMsgEntity != null && mPresenter != null && mPresenter.isLocal() &&
                !TextUtils.isEmpty(mUploadMsgEntity.transId) && !TextUtils.isEmpty(mUploadMsgEntity.refNum)) {
            etRefNum.setText(mUploadMsgEntity.refNum);
            getRefData(mUploadMsgEntity.refNum);
            //如果是离线那么锁定所有的控件
            lockUIUnderEditState(etRefNum);
        }
    }

    protected void getRefData(String refNum) {
        mRefData = null;
        clearAllUI();
        mPresenter.getReference(refNum, mRefType, mBizType, getMoveType(), "", Global.USER_ID);
    }

    /**
     * 获取单据数据成功
     *
     * @param refData
     */
    @Override
    public void getReferenceSuccess(ReferenceEntity refData) {
        SPrefUtil.saveData(mBizType + mRefType, "0");
        refData.bizType = mBizType;
        refData.moveType = getMoveType();
        refData.refType = mRefType;
        //这里必须先判断该张单据是否全是必检物资
        boolean isQmFlag = false;
        for (RefDetailEntity item : refData.billDetailList) {
            isQmFlag = "X".equalsIgnoreCase(item.qmFlag);
            if (isQmFlag)
                break;
        }
        mRefData = refData;
        mRefData.qmFlag = isQmFlag;
        cacheProcessor(mRefData.transId, mRefData.transId, mRefData.recordNum,
                mRefData.refCodeId, mRefData.refType, mRefData.bizType);
    }

    /**
     * 获取单据数据失败
     *
     * @param message
     */
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
    public void cacheProcessor(String cacheFlag, String transId, String refNum,
                               String refCodeId, String refType, String bizType) {
        if (!TextUtils.isEmpty(cacheFlag)) {
            //如果是离线直接获取缓存，不能让用户删除缓存（因为有可能是过账已经成功，数据上传到sap时出错了，所以不允许删除）
            if (mUploadMsgEntity != null && mPresenter != null && mPresenter.isLocal()) {
                mPresenter.getTransferInfo(mRefData, refCodeId, bizType, refType);
                return;
            }
            android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(mActivity);
            dialog.setTitle("提示");
            dialog.setIcon(R.mipmap.icon_tips);
            dialog.setMessage(getString(R.string.msg_has_history));
            dialog.setPositiveButton("确定", (dia, which) -> {
                dia.dismiss();
                mPresenter.deleteCollectionData(refNum, transId, refCodeId, refType, bizType, Global.USER_ID,
                        mCompanyCode);
            });
            dialog.setNegativeButton("取消", (dia, which) -> {
                dia.dismiss();
                mPresenter.getTransferInfo(mRefData, refCodeId, bizType, refType);
            });
            dialog.show();
        } else {
            bindCommonHeaderUI();
        }
    }

    @Override
    public void getTransferInfoFail(String message) {
        showMessage(message);
    }

    /**
     * 删除整单缓存数据成功
     */
    @Override
    public void deleteCacheSuccess() {
        showMessage("缓存删除成功");
        bindCommonHeaderUI();
    }

    /**
     * 删除整单缓存数据失败
     *
     * @param message
     */
    @Override
    public void deleteCacheFail(String message) {
        showMessage(message);
        bindCommonHeaderUI();
    }

    /**
     * 为公共控件绑定数据
     */
    @Override
    public void bindCommonHeaderUI() {
        if (mRefData != null) {
            //单据号
            tvRefNum.setText(mRefData.recordNum);
            //供应商
            String supplier = !TextUtils.isEmpty(mRefData.supplierDesc) ? (mRefData.supplierNum + "_" + mRefData.supplierDesc)
                    : mRefData.supplierNum;
            tvSupplier.setText(supplier);
            //发出工厂
            tvSendWork.setText(mRefData.workCode);
            tvCreator.setText(Global.LOGIN_ID);
        }
    }

    @Override
    public void clearAllUI() {
        clearCommonUI(etRefNum, tvRefNum, tvSupplier, tvSendWork);
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {
        super.clearAllUIAfterSubmitSuccess();
        clearCommonUI(tvRefNum, tvSendWork, tvSupplier);
        mRefData = null;
    }

    @Override
    public void _onPause() {
        super._onPause();
        //再次检查用户是否输入的额外字段而且必须输入的字段（情景是用户请求单据之前没有输入该字段，回来填上后，但是没有请求单据而是直接）
        //切换了页面
        if (mRefData != null) {
            mRefData.voucherDate = getString(etTransferDate);
        }
    }


    @Override
    public void operationOnHeader(String companyCode) {
        mLocalHeaderResult = new ResultEntity();
        mLocalHeaderResult.refCode = mUploadMsgEntity.refNum;
        mLocalHeaderResult.transId = mUploadMsgEntity.transId;
        mLocalHeaderResult.businessType = mBizType;
        mLocalHeaderResult.voucherDate = getString(etTransferDate);
        super.operationOnHeader(companyCode);
    }

    @Override
    public void loadInvsFail(String message) {

    }

    @Override
    public void showInvs(List<InvEntity> list) {

    }

    @Override
    public void loadInvsComplete() {

    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {

    }

    @Override
    public void loadDictionaryDataFail(String message) {
        showMessage(message);
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
    public void showWorks(List<WorkEntity> works) {

    }

    @Override
    public void loadWorksFail(String message) {
        showMessage(message);
    }

    @Override
    public void loadWorksComplete() {

    }

    /*返回移动类型*/
    @CheckResult
    @NonNull
    protected abstract String getMoveType();
}
