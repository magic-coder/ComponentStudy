package com.richfit.common_lib.lib_base_sdk.base_head;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.UploadMsgEntity;

/**
 * Created by monday on 2017/4/28.
 */

public abstract class BaseHeadFragment<P extends IBaseHeadPresenter> extends
        BaseFragment<P> implements IBaseHeadView {

    /*离线保存的抬头数据*/
    protected ResultEntity mLocalHeaderResult;
    /*离线修改需要的数据*/
    protected UploadMsgEntity mUploadMsgEntity;

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        mRefData = null;
        isOpenLocationType = Global.OPEN_LOCATION_TYPE.equals(Global.WMFLAG);
        isOpenRecLocationType = Global.OPEN_LOCATION_TYPE.equals(Global.WMFLAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUploadMsgEntity = arguments.getParcelable(Global.EXTRA_UPLOAD_MSG_KEY);
        }
    }

    @Override
    public boolean checkDataBeforeOperationOnHeader() {
        if (TextUtils.isEmpty(mBizType)) {
            showMessage("未获取到业务类型");
            return false;
        }
        if (mUploadMsgEntity != null && TextUtils.isEmpty(mUploadMsgEntity.transId)) {
            showMessage("缓存标识为空");
            return false;
        }
        return true;
    }


    @Override
    public void operationOnHeader(final String companyCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("温馨提示")
                .setMessage("您是否要保存修改的抬头数据")
                .setPositiveButton("修改", (dialog, which) -> {
                    dialog.dismiss();
                    mPresenter.uploadEditedHeadData(mLocalHeaderResult);

                }).setNegativeButton("取消", (dialog, which) -> dialog.dismiss()).show();
    }


    @Override
    public void uploadEditedHeadDataFail(String message) {
        showMessage(message);
    }

    @Override
    public void uploadEditedHeadComplete() {
        showMessage("保存成功!");
    }

    @Override
    public boolean isNeedShowFloatingButton() {
        //如果是离线，而且是修改过来的那么需要显示按钮
        if (mUploadMsgEntity != null && !TextUtils.isEmpty(mUploadMsgEntity.transId)) {
            return true;
        }
        return false;
    }

    /**
     * 网络错误重试
     *
     * @param action
     */
    @Override
    public void retry(String action) {
        switch (action) {
            case Global.RETRY_SAVE_COLLECTION_DATA_ACTION:
                mPresenter.uploadEditedHeadData(mLocalHeaderResult);
                break;
        }
        super.retry(action);
    }

    @Override
    public void onDestroyView() {
        //如果用户是从数据上传过来的，退出时需要将isLocalFlag设置回去
        if (mPresenter != null && mUploadMsgEntity != null && !TextUtils.isEmpty(mUploadMsgEntity.transId)) {
            mPresenter.setLocal(false);
        }
        super.onDestroyView();
    }

    protected void lockUIUnderEditState(View... views) {
        if (views.length == 0)
            return;
        for (View view : views) {
            view.setEnabled(false);
        }
    }

    /**
     * 当提交数据成功后，从明细界面跳转到抬头界面时回调该方法
     */
    @Override
    public void clearAllUIAfterSubmitSuccess() {
        //复位标志位
        if (TextUtils.isEmpty(mRefType)) {
            SPrefUtil.saveData(mBizType, "0");
        } else {
            SPrefUtil.saveData(mBizType + mRefType, "0");
        }
    }

}
