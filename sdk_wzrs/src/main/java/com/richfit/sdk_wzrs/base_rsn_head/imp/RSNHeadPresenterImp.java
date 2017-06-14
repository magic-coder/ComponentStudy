package com.richfit.sdk_wzrs.base_rsn_head.imp;

import android.content.Context;

import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.sdk_wzrs.base_rsn_head.IRSNHeadPresenter;
import com.richfit.sdk_wzrs.base_rsn_head.IRSNHeaderView;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/3/2.
 */

public class RSNHeadPresenterImp extends BaseHeadPresenterImp<IRSNHeaderView>
        implements IRSNHeadPresenter {

    public RSNHeadPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void deleteCollectionData(String refType, String bizType, String userId,
                                     String companyCode) {
        mView = getView();
        RxSubscriber<String> subscriber = mRepository.deleteCollectionData("", "", "", refType, bizType,
                userId, companyCode)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在删除缓存...") {
                    @Override
                    public void _onNext(String message) {
                        if (mView != null) {
                            mView.deleteCacheSuccess(message);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {

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

                    }
                });
        addSubscriber(subscriber);
    }

    @Override
    public void getAutoCompleteList(String workCode, String keyWord, int defaultItemNum, int flag,
                                    String bizType) {
        mView = getView();

        if (("46".equals(bizType) && "47".equals(bizType))) {
            mView.loadAutoCompleteFail("未找到合适业务类型");
            return;
        }

        final Flowable<ArrayList<SimpleEntity>> flowable = "46".equals(bizType) ? mRepository.getCostCenterList(workCode, keyWord, defaultItemNum, flag)
                : mRepository.getProjectNumList(workCode, keyWord, defaultItemNum, flag);

        ResourceSubscriber<ArrayList<String>> subscriber =
                flowable.filter(list -> list != null && list.size() > 0)
                        .map(list -> wrapper2Str(list))
                        .subscribeWith(new ResourceSubscriber<ArrayList<String>>() {
                            @Override
                            public void onNext(ArrayList<String> suppliers) {
                                if (mView != null) {
                                    mView.showAutoCompleteList(suppliers);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.loadAutoCompleteFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
        addSubscriber(subscriber);
    }
}
