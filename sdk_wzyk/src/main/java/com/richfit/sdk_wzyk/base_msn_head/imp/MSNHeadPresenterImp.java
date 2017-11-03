package com.richfit.sdk_wzyk.base_msn_head.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.sdk_wzyk.base_msn_head.IMSNHeadPresenter;
import com.richfit.sdk_wzyk.base_msn_head.IMSNHeadView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2016/11/20.
 */

public class MSNHeadPresenterImp extends BaseHeadPresenterImp<IMSNHeadView>
        implements IMSNHeadPresenter {


    public MSNHeadPresenterImp(Context context) {
        super(context);
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
    public void getRecInvsByWorkId(String workId, int flag) {
        mView = getView();
        if (TextUtils.isEmpty(workId) && mView != null) {
            mView.loadRecInvsFail("请先选择接收工厂");
            return;
        }
        ResourceSubscriber<ArrayList<InvEntity>> subscriber = mRepository.getInvsByWorkId(workId, flag)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<ArrayList<InvEntity>>() {
                    @Override
                    public void onNext(ArrayList<InvEntity> invs) {
                        if (mView != null) {
                            mView.showRecInvs(invs);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.loadRecInvsFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) {
                            mView.loadRecInvsComplete();
                        }
                    }
                });
        addSubscriber(subscriber);
    }

    @Override
    public void getSendInvsByWorkId(String workId, int flag) {
        mView = getView();
        if (TextUtils.isEmpty(workId) && mView != null) {
            mView.loadSendInvsFail("请先选择发出工厂");
            return;
        }
        ResourceSubscriber<ArrayList<InvEntity>> subscriber =
                mRepository.getInvsByWorkId(workId, flag)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<ArrayList<InvEntity>>() {
                            @Override
                            public void onNext(ArrayList<InvEntity> invs) {
                                if (mView != null) {
                                    mView.showSendInvs(invs);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.loadSendInvsFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {
                                if (mView != null) {
                                    mView.loadSendInvsComplete();
                                }
                            }
                        });
        addSubscriber(subscriber);
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
    public void getAutoComList(String workCode, Map<String, Object> extraMap, String keyWord,
                               int defaultItemNum, int flag, String... keys) {
        mView = getView();
        ResourceSubscriber<Map<String, List<SimpleEntity>>> subscriber =
                mRepository.getAutoComList(workCode, extraMap, keyWord, defaultItemNum, flag, keys)
                        .filter(map -> map != null && map.size() > 0)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<Map<String, List<SimpleEntity>>>() {
                            @Override
                            public void onNext(Map<String, List<SimpleEntity>> map) {
                                if (mView != null) {
                                    mView.showProjectNums(map);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.loadProjectNumsFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
        addSubscriber(subscriber);
    }


}
