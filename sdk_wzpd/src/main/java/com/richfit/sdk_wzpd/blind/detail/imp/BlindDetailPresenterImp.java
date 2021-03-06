package com.richfit.sdk_wzpd.blind.detail.imp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.edit.EditActivity;
import com.richfit.common_lib.lib_interface.IBarcodeSystemMain;
import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.sdk_wzpd.blind.detail.IBlindDetailPresenter;
import com.richfit.sdk_wzpd.blind.detail.IBlindDetailView;

import java.util.List;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2016/12/6.
 */

public class BlindDetailPresenterImp extends BasePresenter<IBlindDetailView>
        implements IBlindDetailPresenter {

    IBlindDetailView mView;

    public BlindDetailPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getCheckTransferInfo(String checkId, String materialNum, String location,
                                     String isPageQuery, int pageNum, int pageSize, String bizType) {
        mView = getView();
        ResourceSubscriber<ReferenceEntity> subscriber =
                mRepository.getCheckTransferInfo(checkId, materialNum, location, isPageQuery, pageNum, pageSize, bizType)
                        .filter(refData -> refData != null && refData.checkList != null)
                        .map(refData -> setCheckFlag(refData))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<ReferenceEntity>() {
                            @Override
                            public void onNext(ReferenceEntity refData) {
                                if (mView != null) {
                                    mView.showCheckInfo(refData, pageNum);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.loadCheckInfoFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void deleteNode(String checkId, String checkLineId, String userId, int position, String bizType) {
        mView = getView();
        if (mView != null && TextUtils.isEmpty(checkLineId)) {
            mView.deleteNodeFail("目标明细ID为空");
            return;
        }
        mRepository.deleteCheckDataSingle(checkId, checkLineId, userId, bizType)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在删除...") {
                    @Override
                    public void _onNext(String s) {

                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {

                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.deleteNodeFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.deleteNodeFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.deleteNodeSuccess(position);
                        }
                    }
                });
    }

    @Override
    public void editNode(InventoryEntity node, String companyCode, String bizType,
                         String refType, String subFunName) {

        Intent intent = new Intent(mContext, EditActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString(Global.EXTRA_COMPANY_CODE_KEY, companyCode);
        bundle.putString(Global.EXTRA_BIZ_TYPE_KEY, bizType);
        bundle.putString(Global.EXTRA_REF_TYPE_KEY, refType);

        //该子节点的id
        bundle.putString(Global.EXTRA_REF_LINE_ID_KEY, node.checkLineId);
        //入库的子菜单的名称
        bundle.putString(Global.EXTRA_TITLE_KEY, "物资盘点-明细修改");
        //物料
        bundle.putString(Global.EXTRA_MATERIAL_NUM_KEY, node.materialNum);
        bundle.putString(Global.EXTRA_MATERIAL_ID_KEY, node.materialId);
        //物料描述
        bundle.putString(Global.EXTRA_MATERIAL_DESC_KEY, node.materialDesc);
        //物料组
        bundle.putString(Global.EXTRA_MATERIAL_GROUP_KEY, node.materialGroup);

        //工厂和库存地点
//        bundle.putString(Global.EXTRA_WORK_ID_KEY,node.workId);
//        bundle.putString(Global.EXTRA_INV_ID_KEY,node.invId);

        //库存
        bundle.putString(Global.EXTRA_INV_QUANTITY_KEY, node.invQuantity);
        //需要修改的字段
        bundle.putString(Global.EXTRA_QUANTITY_KEY, node.totalQuantity);
        bundle.putString(Global.EXTRA_LOCATION_KEY, node.location);
        //副计量单位盘点数量
        bundle.putString(Global.EXTRA_QUANTITY_CUSTOM_KEY,node.totalQuantityCustom);

        intent.putExtras(bundle);

        Activity activity = (Activity) mContext;
        activity.startActivity(intent);
    }

    /**
     * 设置该行是否已经盘点标志
     *
     * @param refData
     * @return
     */
    private ReferenceEntity setCheckFlag(ReferenceEntity refData) {
        List<InventoryEntity> checkList = refData.checkList;
        for (InventoryEntity entity : checkList) {
            if (!TextUtils.isEmpty(entity.totalQuantity) && !"0".equals(entity.totalQuantity)) {
                entity.isChecked = true;
            }
        }
        return refData;
    }

    @Override
    public void transferCheckData(String checkId, String userId, String bizType) {
        mView = getView();
        RxSubscriber<String> subscriber = mRepository.transferCheckData(checkId, userId, bizType)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在过账...") {
                    @Override
                    public void _onNext(String s) {
                        if (mView != null) {
                            mView.showTransferedNum(s);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_TRANSFER_DATA_ACTION);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.transferCheckDataFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.transferCheckDataFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.transferCheckDataSuccess();
                        }
                    }
                });
        addSubscriber(subscriber);
    }

    @Override
    public void showHeadFragmentByPosition(int position) {
        if (mContext instanceof IBarcodeSystemMain) {
            IBarcodeSystemMain activity = (IBarcodeSystemMain) mContext;
            activity.showFragmentByPosition(position);
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
