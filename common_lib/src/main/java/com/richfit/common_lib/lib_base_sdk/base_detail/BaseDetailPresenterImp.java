package com.richfit.common_lib.lib_base_sdk.base_detail;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_eventbus.Event;
import com.richfit.common_lib.lib_eventbus.EventBusUtil;
import com.richfit.common_lib.lib_eventbus.EventCode;
import com.richfit.common_lib.lib_interface.IBarcodeSystemMain;
import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by monday on 2017/3/18.
 */

public class BaseDetailPresenterImp<V extends IBaseDetailView> extends BasePresenter<V> implements
        IBaseDetailPresenter<V> {

    protected V mView;

    public BaseDetailPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getTransferInfo(ReferenceEntity refData, String refCodeId, String bizType, String refType,
                                String userId, String workId, String invId, String recWorkId, String recInvId) {

    }

    @Override
    public void deleteNode(String lineDeleteFlag, String transId, String transLineId, String locationId, String refType, String bizType, int position, String companyCode) {

    }

    @Override
    public void editNode(ArrayList<String> sendLocations, ArrayList<String> recLocations,
                         ReferenceEntity refData, RefDetailEntity node, String companyCode,
                         String bizType, String refType, String subFunName, int position) {

    }

    @Override
    public void submitData2BarcodeSystem(String refCodeId,String transId, String bizType, String refType, String userId, String voucherDate, String transToSapFlag, Map<String, Object> extraHeaderMap) {

    }

    @Override
    public void submitData2SAP(String transId, String bizType, String refType, String userId, String voucherDate, String transToSapFlag, Map<String, Object> extraHeaderMap) {

    }

    @Override
    public void sapUpAndDownLocation(String transId, String bizType, String refType, String userId, String voucherDate, String transToSapFlag, Map<String, Object> extraHeaderMap, int submitFlag) {

    }

    /**
     * 子类不可以重写该方法，注意这里不让重写的目的是统一发送清除抬头UI控件的信号
     *
     * @param position
     */
    @Override
    final public void showHeadFragmentByPosition(int position) {
        if (mContext instanceof IBarcodeSystemMain) {
            IBarcodeSystemMain activity = (IBarcodeSystemMain) mContext;
            activity.showFragmentByPosition(position);
            Event<Boolean> event = new Event<>(EventCode.EVENT_CLEARHEAUI);
            event.setData(true);
            EventBusUtil.sendEvent(event);
        }
    }



    @Override
    public void setTransFlag(String bizType, String transId, String transFlag) {
        mView = getView();
        if (TextUtils.isEmpty(bizType) || TextUtils.isEmpty(transFlag) || TextUtils.isEmpty(transId)) {
            return;
        }
        RxSubscriber<String> subscriber = mRepository.setTransFlag(bizType, transId, transFlag)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在结束本次操作") {
                    @Override
                    public void _onNext(String s) {

                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_SET_TRANS_FLAG_ACTION);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.setTransFlagFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.setTransFlagFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.setTransFlagsComplete();
                        }
                    }
                });
        addSubscriber(subscriber);

    }
}
