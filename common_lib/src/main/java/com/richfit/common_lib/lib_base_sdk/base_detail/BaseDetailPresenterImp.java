package com.richfit.common_lib.lib_base_sdk.base_detail;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_eventbus.Event;
import com.richfit.common_lib.lib_eventbus.EventBusUtil;
import com.richfit.common_lib.lib_eventbus.EventCode;
import com.richfit.common_lib.lib_interface.IBarcodeSystemMain;
import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;


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
                                String userId, String workId, String invId, String recWorkId, String recInvId,
                                Map<String, Object> extraMap) {

        mView = getView();
        ResourceSubscriber<ArrayList<RefDetailEntity>> subscriber =
                mRepository.getTransferInfo("", refCodeId, bizType, refType,
                        "", "", "", "", "", extraMap)
                        .zipWith(Flowable.just(refData), (cache, data) -> createNodesByCache(data, cache))
                        .flatMap(nodes -> sortNodes(nodes))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<ArrayList<RefDetailEntity>>() {
                            @Override
                            public void onNext(ArrayList<RefDetailEntity> nodes) {
                                if (mView != null) {
                                    mView.showNodes(nodes);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.setRefreshing(true, t.getMessage());
                                    //展示抬头获取的数据，没有缓存
                                    //注意获取无参考的整单缓存时，单据数据refData为null
                                    if (refData != null && refData.billDetailList != null)
                                        mView.showNodes(refData.billDetailList);
                                }
                            }

                            @Override
                            public void onComplete() {
                                if (mView != null) {
                                    mView.refreshComplete();
                                }
                            }
                        });
        addSubscriber(subscriber);
    }


    @Override
    public void deleteNode(String lineDeleteFlag, String transId, String transLineId,
                           String locationId, String refType, String bizType,
                           String refLineId, String userId, int position, String companyCode) {
        RxSubscriber<String> subscriber =
                mRepository.deleteCollectionDataSingle(lineDeleteFlag, transId, transLineId,
                        locationId, refType, bizType, refLineId, userId, position, companyCode)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<String>(mContext) {
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
        addSubscriber(subscriber);
    }

    @Override
    public void editNode(ArrayList<String> sendLocations, ArrayList<String> recLocations,
                         ReferenceEntity refData, RefDetailEntity node, String companyCode,
                         String bizType, String refType, String subFunName, int position) {

    }

    //注意这里默认实现的两步，第一步uploadCollectedData,第二步transferCollectionData
    @Override
    public void submitData2BarcodeSystem(String refCodeId, String transId, String bizType,
                                         String refType, String userId, String voucherDate,
                                         String transToSap, Map<String, Object> extraHeaderMap) {
        mView = getView();
        RxSubscriber<String> subscriber = Flowable.concat(mRepository.uploadCollectionData(refCodeId, transId, bizType, refType, -1, voucherDate, "", userId, extraHeaderMap),
                mRepository.transferCollectionData(transId, bizType, refType, userId, voucherDate, transToSap, extraHeaderMap))
                .doOnError(str -> saveData2Spre(bizType,refType,0))
                .doOnComplete(() -> saveData2Spre(bizType,refType,1))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在过账...") {
                    @Override
                    public void _onNext(String message) {
                        if (mView != null) {
                            mView.saveMsgFowShow(message);
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
                            mView.submitBarcodeSystemFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.submitBarcodeSystemFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.submitBarcodeSystemSuccess();
                        }
                    }
                });
        addSubscriber(subscriber);
    }

    @Override
    public void submitData2SAP(String transId, String bizType, String refType, String userId,
                               String voucherDate, String transToSap, Map<String, Object> extraHeaderMap) {
        mView = getView();
        RxSubscriber<String> subscriber = mRepository.transferCollectionData(transId, bizType, refType,
                userId, voucherDate, transToSap, extraHeaderMap)
//                .retryWhen(new RetryWhenNetworkException(3, 3000))
                .doOnComplete(() ->  saveData2Spre(bizType,refType,0))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在上传数据...") {
                    @Override
                    public void _onNext(String message) {
                        if (mView != null) {
                            mView.saveMsgFowShow(message);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_UPLOAD_DATA_ACTION);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.submitSAPFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.submitSAPFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.submitSAPSuccess();
                        }
                    }
                });
        addSubscriber(subscriber);
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

    /**
     * 通过抬头获取的单据数据和缓存数据生成新的单据数据。
     * 注意我们的目的是将这两部分数据完全分离，这样有利于处理。
     *
     * @param refData：塔头获取的原始单据数据
     * @param cache：缓存单据数据
     * @return
     */
    protected ArrayList<RefDetailEntity> createNodesByCache(ReferenceEntity refData, ReferenceEntity cache) {
        ArrayList<RefDetailEntity> nodes = new ArrayList<>();
        List<RefDetailEntity> list = refData.billDetailList;
        //1.形成父节点数据集合
        for (RefDetailEntity data : list) {
            RefDetailEntity cachedData = getLineDataByRefLineId(data, cache);
            if (cachedData == null) {
                //说明该还没有缓存
                nodes.add(data);
            } else {
                //如果有缓存，那么将缓存作为父节点，注意注意此时应当将原始单据的部分字段信息赋值给缓存。
                //这里我们不适用原始单据信息作为缓存这是因为单据信息是全局的,另外就是修改等针对该节点的操作需要缓存数据
                //将原始单据的物料信息赋值给缓存
                cachedData.lineNum = data.lineNum;
                cachedData.materialNum = data.materialNum;
                cachedData.materialId = data.materialId;
                cachedData.materialDesc = data.materialDesc;
                cachedData.materialGroup = data.materialGroup;
                cachedData.unit = data.unit;
                cachedData.unitCustom = data.unitCustom;
                cachedData.actQuantityCustom = data.actQuantityCustom;
                cachedData.actQuantity = data.actQuantity;
                cachedData.refDoc = data.refDoc;
                cachedData.refDocItem = data.refDocItem;
                //注意单据中有lineNum105
                cachedData.lineNum105 = data.lineNum105;
                cachedData.insLot = data.insLot;
                nodes.add(cachedData);
            }
        }
        //2.形成父节点结构
        addTreeInfo(nodes);
        ArrayList<RefDetailEntity> result = new ArrayList<>();
        result.addAll(nodes);
        //3.生成子节点
        for (RefDetailEntity parentNode : nodes) {
            List<LocationInfoEntity> locationList = parentNode.locationList;
            if (locationList == null || locationList.size() == 0) {
                //说明是原始单据的父节点
                continue;
            }
            //首先去除之前所有父节点的子节点
            parentNode.getChildren().clear();
            parentNode.setHasChild(false);

            for (LocationInfoEntity location : locationList) {
                RefDetailEntity childNode = new RefDetailEntity();
                childNode.refLineId = parentNode.refLineId;
                childNode.invId = parentNode.invId;
                childNode.invCode = parentNode.invCode;
                childNode.remark = parentNode.remark;
                childNode.totalQuantity = parentNode.totalQuantity;
                childNode.location = location.location;
                childNode.batchFlag = location.batchFlag;
                childNode.quantity = location.quantity;
                childNode.transId = location.transId;
                childNode.transLineId = location.transLineId;
                childNode.locationId = location.id;
                childNode.locationCombine = location.locationCombine;
                childNode.specialInvFlag = location.specialInvFlag;
                childNode.specialInvNum = location.specialInvNum;
                childNode.specialConvert = location.specialConvert;
                childNode.quantityCustom = location.quantityCustom;
                childNode.locationType = location.locationType;
                addTreeInfo(parentNode, childNode, result);
            }
        }
        return result;
    }

    private void saveData2Spre(String bizType,String refType,int value) {
        String key = TextUtils.isEmpty(refType) ? bizType : bizType + refType;
        SPrefUtil.saveData(key, value);
    }
}
