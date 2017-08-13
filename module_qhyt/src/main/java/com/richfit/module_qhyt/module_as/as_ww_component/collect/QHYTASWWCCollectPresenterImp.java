package com.richfit.module_qhyt.module_as.as_ww_component.collect;

import android.content.Context;

import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.RefDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/3/10.
 */

public class QHYTASWWCCollectPresenterImp extends BaseCollectPresenterImp<QHYTASWWCCollectContract.QingHaiWWCCollectView>
        implements QHYTASWWCCollectContract.QingHaiWWCCollectPresenter {


    public QHYTASWWCCollectPresenterImp(Context context) {
        super(context);
    }


    @Override
    public void getInventoryInfo(String queryType, String workId, String invId, String workCode, String invCode, String storageNum,
                                 String materialNum, String materialId, String location, String batchFlag,
                                 String specialInvFlag, String specialInvNum, String invType, String deviceId,
                                 Map<String,Object> extraMap) {
        mView = getView();

        RxSubscriber<List<InventoryEntity>> subscriber =
                mRepository.getInventoryInfo(queryType, workId, invId,
                        workCode, invCode, storageNum, materialNum, materialId, "", "", batchFlag,
                        location, specialInvFlag, specialInvNum, invType, deviceId,extraMap)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<List<InventoryEntity>>(mContext, "正在获取库存...") {
                            @Override
                            public void _onNext(List<InventoryEntity> list) {
                                if (mView != null) {
                                    mView.showInventory(list);
                                }
                            }

                            @Override
                            public void _onNetWorkConnectError(String message) {
                                if (mView != null) {
                                    mView.networkConnectError(Global.RETRY_LOAD_INVENTORY_ACTION);
                                }
                            }

                            @Override
                            public void _onCommonError(String message) {
                                if (mView != null) {
                                    mView.loadInventoryFail(message);
                                }
                            }

                            @Override
                            public void _onServerError(String code, String message) {
                                if (mView != null) {
                                    mView.loadInventoryFail(message);
                                }
                            }

                            @Override
                            public void _onComplete() {

                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getTransferInfoSingle(String refCodeId, String refType, String bizType,
                                      String refLineId, String batchFlag, String location,
                                      String refDoc, int refDocItem, String userId) {
        mView = getView();
        RxSubscriber<RefDetailEntity> subscriber =
                mRepository.getTransferInfoSingle(refCodeId, refType, bizType, refLineId, "", "", "",
                        "", "", batchFlag, location, refDoc, refDocItem, userId)
                        .filter(refData -> refData != null && refData.billDetailList != null)
                        .flatMap(refData -> getMatchedLineData(refLineId, refData))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<RefDetailEntity>(mContext) {
                            @Override
                            public void _onNext(RefDetailEntity cache) {
                                //获取缓存数据
                                if (mView != null) {
                                    mView.onBindCache(cache, batchFlag, location);
                                }
                            }

                            @Override
                            public void _onNetWorkConnectError(String message) {
                                if (mView != null) {
                                    mView.networkConnectError(Global.RETRY_LOAD_SINGLE_CACHE_ACTION);
                                }
                            }

                            @Override
                            public void _onCommonError(String message) {
                                if (mView != null) {
                                    mView.loadCacheFail(message);
                                }
                            }

                            @Override
                            public void _onServerError(String code, String message) {
                                if (mView != null) {
                                    mView.loadCacheFail(message);
                                }
                            }

                            @Override
                            public void _onComplete() {
                                if (mView != null) {
                                    mView.loadCacheSuccess();
                                }
                            }
                        });
        addSubscriber(subscriber);
    }
}
