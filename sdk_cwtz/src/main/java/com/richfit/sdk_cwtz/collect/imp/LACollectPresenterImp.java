package com.richfit.sdk_cwtz.collect.imp;

import android.content.Context;

import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_cwtz.collect.ILACollectPresenter;
import com.richfit.sdk_cwtz.collect.ILACollectView;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/2/7.
 */

public class LACollectPresenterImp extends BasePresenter<ILACollectView>
        implements ILACollectPresenter {

    ILACollectView mView;

    public LACollectPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getMaterialInfo(String queryType, String materialNum,String workId) {
        mView = getView();

        RxSubscriber<MaterialEntity> subscriber =
                mRepository.getMaterialInfo(queryType, materialNum)
                .flatMap(entity-> Flowable.just(addBatchManagerStatus(entity,workId)))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<MaterialEntity>(mContext, "正在获取物料信息...") {
                    @Override
                    public void _onNext(MaterialEntity materialEntity) {
                        if (mView != null) {
                            mView.getMaterialInfoSuccess(materialEntity);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_QUERY_MATERIAL_INFO);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.getMaterialInfoFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.getMaterialInfoFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {

                    }
                });
        addSubscriber(subscriber);
    }

    @Override
    public void getInventoryInfo(String queryType, String workId, String invId, String workCode,
                                 String invCode, String storageNum, String materialNum, String materialId,
                                 String materialGroup, String materialDesc,
                                 String batchFlag, String location,
                                 String specialInvFlag, String specialInvNum, String invType,
                                 String deviceId) {
        mView = getView();

        RxSubscriber<List<InventoryEntity>> subscriber =
                mRepository.getInventoryInfo(queryType, workId, invId, workCode, invCode, storageNum,
                        materialNum, materialId, materialGroup,
                        materialDesc, batchFlag, location, specialInvFlag, specialInvNum, invType, deviceId)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<List<InventoryEntity>>(mContext, "正在获取库存信息...") {
                            @Override
                            public void _onNext(List<InventoryEntity> inventoryEntities) {
                                if (mView != null) {
                                    mView.getInventorySuccess(inventoryEntities);
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
                                    mView.getInventoryFail(message);
                                }
                            }

                            @Override
                            public void _onServerError(String code, String message) {
                                if (mView != null) {
                                    mView.getInventoryFail(message);
                                }
                            }

                            @Override
                            public void _onComplete() {

                            }
                        });
        addSubscriber(subscriber);
    }


    @Override
    public void uploadCollectionDataSingle(ResultEntity result) {
        mView = getView();
        ResourceSubscriber<String> subscriber =
                mRepository.transferCollectionData(result)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<String>(mContext) {
                            @Override
                            public void _onNext(String s) {
                                if (mView != null) {
                                    mView.saveCollectedDataSuccess(s);
                                }
                            }

                            @Override
                            public void _onNetWorkConnectError(String message) {
                                if (mView != null) {
                                    mView.networkConnectError(Global.RETRY_SAVE_COLLECTION_DATA_ACTION);
                                }
                            }

                            @Override
                            public void _onCommonError(String message) {
                                if (mView != null) {
                                    mView.saveCollectedDataFail(message);
                                }
                            }

                            @Override
                            public void _onServerError(String code, String message) {
                                if (mView != null) {
                                    mView.saveCollectedDataFail(message);
                                }
                            }

                            @Override
                            public void _onComplete() {

                            }
                        });
        addSubscriber(subscriber);
    }

    protected MaterialEntity addBatchManagerStatus(MaterialEntity data,String workId) {
        if ("Y".equalsIgnoreCase(Global.BATCHMANAGERSTATUS)) {
            data.batchManagerStatus = true;
        } else if ("N".equalsIgnoreCase(Global.BATCHMANAGERSTATUS)) {
            data.batchManagerStatus = false;
        } else if ("T".equalsIgnoreCase(Global.BATCHMANAGERSTATUS)) {
            String batchManagerStatus = mRepository.getBatchManagerStatus(workId, data.id);
            //如果是X那么表示打开了批次管理
            data.batchManagerStatus = "X".equalsIgnoreCase(batchManagerStatus);
        }
        return data;
    }

}
