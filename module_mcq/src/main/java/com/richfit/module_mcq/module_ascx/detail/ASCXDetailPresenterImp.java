package com.richfit.module_mcq.module_ascx.detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/8/29.
 */

public class ASCXDetailPresenterImp extends BaseDetailPresenterImp<IASCXDetailView>
        implements IASCXDetailPresenter {

    public ASCXDetailPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getReference(@NonNull String refNum, @NonNull String refType, @NonNull String bizType,
                             @NonNull String moveType, @NonNull String refLineId, @NonNull String userId) {
        mView = getView();

        if (TextUtils.isEmpty(refNum) && mView != null) {
            mView.setRefreshing(true, "单据号为空,请重新输入");
            return;
        }

        if ((TextUtils.isEmpty(refType) || "-1".equals(refType)) && mView != null) {
            mView.setRefreshing(true, "请选选择单据类型");
            return;
        }

        ResourceSubscriber<ReferenceEntity> subscriber =
                mRepository.getReference(refNum, refType, bizType, moveType, refLineId, userId)
                        .filter(refData -> refData != null && refData.billDetailList != null && refData.billDetailList.size() > 0)
                        .map(refData->{
                            //将抬头的部分数据赋值给明细
                            List<RefDetailEntity> list = refData.billDetailList;
                            for (RefDetailEntity item : list) {
                                item.recordNum = refData.recordNum;
                                item.supplierCode = refData.supplierNum;
                                item.supplierName = refData.supplierDesc;
                                item.creator = refData.recordCreator;
                            }
                            return refData;
                        })
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<ReferenceEntity>() {
                            @Override
                            public void onNext(ReferenceEntity refData) {
                                if (mView != null) {
                                    mView.getReferenceSuccess(refData);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.setRefreshing(true, t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {
                                if (mView != null) {
                                    mView.setRefreshing(true, "获取单据信息成功!!!");
                                }
                            }
                        });
        addSubscriber(subscriber);
    }
}
