package com.richfit.sdk_wzrs.base_rsn_collect.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzrs.base_rsn_collect.IRSNCollectPresenter;
import com.richfit.sdk_wzrs.base_rsn_collect.IRSNCollectView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/3/2.
 */

public class RSNCollectPresenterImp extends BasePresenter<IRSNCollectView>
        implements IRSNCollectPresenter {

    IRSNCollectView mView;

    public RSNCollectPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getInventoryInfo(String queryType, String workId, String invId, String workCode, String invCode, String storageNum,
                                 String materialNum, String materialId, String location, String batchFlag,
                                 String specialInvFlag, String specialInvNum, String invType, String deviceId,
                                 Map<String,Object> extraMap,boolean isDropDown) {
        mView = getView();
        if(isLocal())
            return;
        ResourceSubscriber<List<String>> subscriber;
        if ("04".equals(queryType)) {
            subscriber = mRepository.getStorageNum(workId, workCode, invId, invCode)
                    .filter(num -> !TextUtils.isEmpty(num))
                    .flatMap(num -> mRepository.getInventoryInfo(queryType, workId, invId,
                            workCode, invCode, num, materialNum, materialId, "", "", batchFlag, location,
                            specialInvFlag, specialInvNum, invType, deviceId,extraMap))
                    .filter(list -> list != null && list.size() > 0)
                    .map(list -> changeInv2Locations(list))
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new InventorySubscriber(isDropDown));

        } else {
            subscriber = mRepository.getInventoryInfo(queryType, workId, invId,
                    workCode, invCode, storageNum, materialNum, materialId, "", "", batchFlag, location,
                    specialInvFlag, specialInvNum, invType, deviceId,extraMap)
                    .filter(list -> list != null && list.size() > 0)
                    .map(list -> changeInv2Locations(list))
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new InventorySubscriber(isDropDown));
        }
        addSubscriber(subscriber);
    }

    private List<String> changeInv2Locations(List<InventoryEntity> invs) {
        List<String> locations = new ArrayList<>();
        HashSet<String> set = new HashSet<>();
        for (InventoryEntity data : invs) {
            if(!set.contains(data.location)) {
                set.add(data.location);
                locations.add(data.location);
            }
        }
        return locations;
    }

    protected class InventorySubscriber extends ResourceSubscriber<List<String>> {

        private boolean isDropDown;

        public InventorySubscriber(boolean isDropDown) {
            this.isDropDown = isDropDown;
        }

        @Override
        public void onNext(List<String> list) {
            if (mView != null) {
                mView.showInventory(list);
            }
        }


        @Override
        public void onError(Throwable e) {
            if (mView != null) {
                mView.loadInventoryFail(e.getMessage());
            }
        }



        @Override
        public void onComplete() {
            if(mView != null) {
                mView.loadInventoryComplete(isDropDown);
            }
        }
    }

    @Override
    public void getInvsByWorks(String workId, int flag) {
        mView = getView();
        ResourceSubscriber<ArrayList<InvEntity>> subscriber =
                mRepository.getInvsByWorkId(workId, flag)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<ArrayList<InvEntity>>() {
                            @Override
                            public void onNext(ArrayList<InvEntity> list) {
                                if (mView != null) {
                                    mView.showInvs(list);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.loadInvsFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getTransferSingleInfo(String bizType, String materialNum, String userId, String workId,
                                      String invId, String recWorkId, String recInvId, String batchFlag,
                                      String refDoc, int refDocItem) {
        mView = getView();
        RxSubscriber<ReferenceEntity> subscriber = mRepository.getTransferInfoSingle("", "", bizType, "",
                workId, invId, recWorkId, recInvId, materialNum, batchFlag, "", refDoc, refDocItem, userId)
                .filter(refData -> refData != null && refData.billDetailList.size() > 0)
                .flatMap(refData -> Flowable.just(addBatchManagerStatus(refData,workId)))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<ReferenceEntity>(mContext) {
                    @Override
                    public void _onNext(ReferenceEntity refData) {
                        if (mView != null) {
                            mView.onBindCommonUI(refData, batchFlag);
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
                            mView.loadTransferSingleInfoFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.loadTransferSingleInfoFail(message);
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
                mRepository.uploadCollectionDataSingle(result)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<String>(mContext) {
                            @Override
                            public void _onNext(String s) {

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
                                if (mView != null) {
                                    mView.saveCollectedDataSuccess();
                                }
                            }
                        });
        addSubscriber(subscriber);
    }
    protected ReferenceEntity addBatchManagerStatus(ReferenceEntity refData, String workId) {
        if ("Y".equalsIgnoreCase(Global.BATCHMANAGERSTATUS)) {
            addBatchManagerStatus(refData.billDetailList, true);
        } else if ("N".equalsIgnoreCase(Global.BATCHMANAGERSTATUS)) {
            addBatchManagerStatus(refData.billDetailList, false);
        } else if ("T".equalsIgnoreCase(Global.BATCHMANAGERSTATUS)) {
            List<RefDetailEntity> list = refData.billDetailList;
            for (RefDetailEntity data : list) {
                String batchManagerStatus = mRepository.getBatchManagerStatus(workId, data.materialId);
                //如果是X那么表示打开了批次管理
                data.batchManagerStatus = "X".equalsIgnoreCase(batchManagerStatus);
            }
        }
        return refData;
    }

}
