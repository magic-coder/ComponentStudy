package com.richfit.sdk_wzrk.base_as_head.imp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.sdk_wzrk.base_as_head.IASHeadPresenter;
import com.richfit.sdk_wzrk.base_as_head.IASHeadView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2016/11/11.
 */

public class ASHeadPresenterImp extends BaseHeadPresenterImp<IASHeadView>
        implements IASHeadPresenter {

    protected IASHeadView mView;

    public ASHeadPresenterImp(Context context) {
        super(context);
    }


    @Override
    public void getReference(@NonNull String refNum, @NonNull String refType, @NonNull String bizType,
                             @NonNull String moveType, @NonNull String refLineId, @NonNull String userId) {
        mView = getView();

        if (TextUtils.isEmpty(refNum) && mView != null) {
            mView.getReferenceFail("单据号为空,请重新输入");
            return;
        }

        if ((TextUtils.isEmpty(refType) || "-1".equals(refType)) && mView != null) {
            mView.getReferenceFail("请选选择单据类型");
            return;
        }
        RxSubscriber<ReferenceEntity> subscriber =
                mRepository.getReference(refNum, refType, bizType, moveType, refLineId, userId)
                        .filter(refData -> refData != null && refData.billDetailList != null && refData.billDetailList.size() > 0)
                        .flatMap(refData -> Flowable.just(addBatchManagerStatus(refData)))
                        .map(refData -> addTreeInfo(refData))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<ReferenceEntity>(mContext) {
                            @Override
                            public void _onNext(ReferenceEntity refData) {
                                if (mView != null) {
                                    mView.getReferenceSuccess(refData);
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

                            }
                        });
        addSubscriber(subscriber);
    }


    @Override
    public void getWorks(int flag) {
        mView = getView();
        ResourceSubscriber<ArrayList<WorkEntity>> subscriber = mRepository.getWorks(flag)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<ArrayList<WorkEntity>>() {
                    @Override
                    public void onNext(ArrayList<WorkEntity> works) {
                        if (mView != null) {
                            mView.showWorks(works);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.loadWorksFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) {
                            mView.loadWorksComplete();
                        }
                    }
                });
        addSubscriber(subscriber);
    }

    @Override
    public void getInvsByWorkId(String workId, int flag) {
        mView = getView();
        if (TextUtils.isEmpty(workId) && mView != null) {
            mView.loadInvsFail("单据中不存在工厂信息");
            return;
        }
        ResourceSubscriber<ArrayList<InvEntity>> subscriber = mRepository.getInvsByWorkId(workId, flag)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<ArrayList<InvEntity>>() {
                    @Override
                    public void onNext(ArrayList<InvEntity> invs) {
                        if (mView != null) {
                            mView.showInvs(invs);
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
                        if (mView != null) {
                            mView.loadInvsComplete();
                        }
                    }
                });
        addSubscriber(subscriber);
    }

    @Override
    public void getDictionaryData(String... codes) {
        mView = getView();
        mRepository.getDictionaryData(codes)
                .filter(data -> data != null && data.size() > 0)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<Map<String,List<SimpleEntity>>>() {
                    @Override
                    public void onNext(Map<String,List<SimpleEntity>> data) {
                        if (mView != null) {
                            mView.loadDictionaryDataSuccess(data);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.loadDictionaryDataFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void deleteCollectionData(String refNum, String transId, String refCodeId, String refType, String bizType,
                                     String userId, String companyCode) {
        mView = getView();
        ResourceSubscriber<String> subscriber = mRepository.deleteCollectionData(refNum, transId, refCodeId,
                refType, bizType, userId, companyCode)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext) {
                    @Override
                    public void _onNext(String s) {

                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_DELETE_TRANSFERED_CACHE_ACTION);
                        }
                    }


                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.deleteCacheFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.deleteCacheFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.deleteCacheSuccess();
                        }
                    }
                });
        addSubscriber(subscriber);
    }

    @Override
    public void getTransferInfo(final ReferenceEntity refData, String refCodeId, String bizType, String refType) {
        mView = getView();
        ResourceSubscriber<ReferenceEntity> subscriber = mRepository.getTransferInfo("", refCodeId, bizType, refType, "", "", "", "", "",null)
                .zipWith(Flowable.just(refData), (cache, data) -> createHeaderByCache(cache, data))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<ReferenceEntity>() {

                    @Override
                    public void onNext(ReferenceEntity data) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.getTransferInfoFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) {
                            mView.bindCommonHeaderUI();
                        }
                    }
                });
        addSubscriber(subscriber);
    }

    /**
     * 将抬头的缓存写入单据中
     *
     * @param cache：缓存单据数据
     * @param data：原始单据数据
     * @return
     */
    private ReferenceEntity createHeaderByCache(ReferenceEntity cache, ReferenceEntity data) {
        data.voucherDate = cache.voucherDate;
        // 长庆油田增加提货单缓存
        data.deliveryOrder = cache.deliveryOrder;
        // 报检单位
        data.declaredUnit = cache.declaredUnit;
        // 班
        data.team = cache.team;
        // 岗位
        data.post = cache.post;
        // 生产厂家
        data.manufacture = cache.manufacture;
        //检验单位
        data.inspectionUnit = cache.inspectionUnit;
        // 备注
        data.remark = cache.remark;
        // 检验标准及特殊要求
        data.inspectionStandard = cache.inspectionStandard;
        return data;
    }
}
