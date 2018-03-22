package com.richfit.module_cqyt.module_inv;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.RefDetailEntity;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;
import rx.Subscriber;

/**
 * Created by monday on 2018/3/19.
 */

public class CQInvYPresenterImpl extends BasePresenter<ICQDSInvYView>
        implements ICQDSInvYPresenter {

    ICQDSInvYView mView;

    public CQInvYPresenterImpl(Context context) {
        super(context);
    }

    @Override
    public void getReference(String refNum, String refType, String bizType, String moveType, String refLineId, String userId) {
        mView = getView();

        if (TextUtils.isEmpty(refNum) && mView != null) {
            mView.getReferenceFail("单据号为空,请重新输入");
            return;
        }

        if ((TextUtils.isEmpty(refType) || "-1".equals(refType)) && mView != null) {
            mView.getReferenceFail("请选选择单据类型");
            return;
        }
        mRepository.getReference(refNum, refType, bizType, moveType, refLineId, userId)
                .filter(refData -> refData != null && refData.billDetailList != null && refData.billDetailList.size() > 0)
                .map(refData -> addTreeInfo(refData))
                .flatMap(refData -> Flowable.fromIterable(refData.billDetailList))
                .map(parentNode -> {
                    parentNode.invId = "D2B0CEFF456E91BC084D9B70F969B97C";
                    parentNode.invCode = "1802";
                    return parentNode;
                })
                .flatMap(parentNode -> mRepository.getStorageNum(parentNode.workId, parentNode.workCode, parentNode.invId,
                            parentNode.invCode)
                            .delay(100, TimeUnit.MILLISECONDS)
                            .filter(num -> !TextUtils.isEmpty(num))
                            .flatMap(num -> mRepository.getInventoryInfo("04", parentNode.workId, parentNode.invId,
                                    parentNode.workCode, parentNode.invCode, num, parentNode.materialNum,
                                    parentNode.materialId, "", "", parentNode.batchFlag,
                                    "", "", "", "1", null))
                            .onErrorReturnItem(new ArrayList<>())
                            .map(invs -> {
                                ArrayList<RefDetailEntity> result = new ArrayList<>();
                                result.add(parentNode);
                                //首先去除之前所有父节点的子节点
                                parentNode.getChildren().clear();
                                parentNode.setHasChild(false);
                                for (InventoryEntity item : invs) {
                                    RefDetailEntity childNode = new RefDetailEntity();
                                    childNode.invCode = item.invCode;
                                    childNode.invId = item.invId;
                                    childNode.location = item.location;
                                    childNode.batchFlag = item.batchFlag;
                                    childNode.invQuantity = item.invQuantity;
                                    addTreeInfo(parentNode, childNode, result);
                                }
                                return result;
                            }))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<List<RefDetailEntity>>(mContext) {
                    @Override
                    public void _onNext(List<RefDetailEntity> datas) {
                        if (mView != null) {
                            mView.getReferenceSuccess(datas);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_LOAD_REFERENCE_ACTION);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.getReferenceFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.getReferenceFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.loadReferenceComplete();
                        }
                    }

                });
    }
}