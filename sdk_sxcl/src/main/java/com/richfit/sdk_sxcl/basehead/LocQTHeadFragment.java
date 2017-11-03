package com.richfit.sdk_sxcl.basehead;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadFragment;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.sdk_sxcl.R;
import com.richfit.sdk_sxcl.R2;
import com.richfit.sdk_sxcl.basehead.imp.LocQTHeadPresenterImp;

import butterknife.BindView;

/**
 * 其他上下架处理抬头界面
 * Created by monday on 2017/5/25.
 */

public class LocQTHeadFragment extends BaseHeadFragment<LocQTHeadPresenterImp>
        implements ILocQTHeadView {

    @BindView(R2.id.et_ref_num)
    RichEditText etRefNum;
    @BindView(R2.id.tv_creator)
    TextView tvCreator;
    @BindView(R2.id.tv_ref_num)
    TextView tvRefNum;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        if (list != null && list.length == 1) {
            getRefData(list[0]);
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.sxcl_fragment_loc_qt_header;
    }

    @Override
    public void initPresenter() {
        mPresenter = new LocQTHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    /**
     * 注册点击事件
     */
    @Override
    public void initEvent() {
        //点击单号加载单据数据
        etRefNum.setOnRichEditTouchListener((view, refNum) -> {
            hideKeyboard(view);
            getRefData(refNum);
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initDataLazily() {

    }

    protected void getRefData(String refNum) {
        mRefData = null;
        clearAllUI();
        mPresenter.getReference(refNum, mRefType, mBizType, "", "", Global.USER_ID);
    }

    @Override
    public void getReferenceSuccess(ReferenceEntity refData) {
        //需要注意上下架处理是否有单据类型
        SPrefUtil.saveData(mBizType + mRefType, "0");
        refData.bizType = mBizType;
        refData.refType = mRefType;
        mRefData = refData;
        cacheProcessor(mRefData.transId, mRefData.transId, mRefData.recordNum,
                mRefData.refCodeId, mRefData.refType, mRefData.bizType);
    }

    @Override
    public void getReferenceFail(String message) {
        showMessage(message);
        mRefData = null;
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
            //如果是离线直接获取缓存，不能让用户删除缓存
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
                mPresenter.deleteCollectionData(refNum, transId, refCodeId, refType, bizType,
                        Global.USER_ID, mCompanyCode);
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

    @Override
    public void getTransferInfoFail(String message) {
        showMessage(message);
        bindCommonHeaderUI();
    }

    @Override
    public void bindCommonHeaderUI() {
        if (mRefData != null) {
            //单据号
            tvRefNum.setText(mRefData.recordNum);
            //创建人
            tvCreator.setText(mRefData.recordCreator);
        }
    }

    @Override
    public void clearAllUI() {
        clearCommonUI(etRefNum, tvCreator, tvRefNum);
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {
        super.clearAllUIAfterSubmitSuccess();
        clearCommonUI(etRefNum, tvRefNum, tvCreator);
    }

}
