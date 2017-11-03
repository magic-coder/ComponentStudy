package com.richfit.sdk_xxcx.inventory_query_n.header.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadPresenterImp;
import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.sdk_xxcx.inventory_query_n.header.IInvNQueryHeaderPresenter;
import com.richfit.sdk_xxcx.inventory_query_n.header.IInvNQueryHeaderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/5/25.
 */

public class InvNQueryHeaderPresenterImp extends BaseHeadPresenterImp<IInvNQueryHeaderView>
        implements IInvNQueryHeaderPresenter {

    IInvNQueryHeaderView mView;

    public InvNQueryHeaderPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getWorks(int flag) {
        mView = getView();
        ResourceSubscriber<ArrayList<WorkEntity>> subscriber =
                mRepository.getWorks(flag)
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
                                if(mView != null) {
                                    mView.loadWorksComplete();
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
    public void getInvsByWorkId(String workId, int flag) {
        mView = getView();
        if (TextUtils.isEmpty(workId) && mView != null) {
            mView.loadInvsFail("请先选择接收工厂");
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

}
