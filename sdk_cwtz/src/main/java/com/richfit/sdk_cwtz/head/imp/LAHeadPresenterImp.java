package com.richfit.sdk_cwtz.head.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.sdk_cwtz.head.ILAHeadPresenter;
import com.richfit.sdk_cwtz.head.ILAHeadView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/2/7.
 */

public class LAHeadPresenterImp extends BasePresenter<ILAHeadView>
        implements ILAHeadPresenter {

    ILAHeadView mView;

    public LAHeadPresenterImp(Context context) {
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
                                    mView.loadInvsComplete();
                                }
                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getInvsByWorkId(String workId, int flag) {
        mView = getView();
        if (TextUtils.isEmpty(workId) && mView != null) {
            mView.loadInvsFail("请先选择工厂");
            return;
        }
        ResourceSubscriber<ArrayList<InvEntity>> subscriber =
                mRepository.getInvsByWorkId(workId, flag)
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

                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getStorageNum(String workId, String workCode, String invId, String invCode) {
        mView = getView();

        if (TextUtils.isEmpty(workId)) {
            mView.getStorageNumFail("工厂id为空");
            return;
        }

        if (TextUtils.isEmpty(invCode)) {
            mView.getStorageNumFail("库存地点为空");
            return;
        }

        mRepository.getStorageNum(workId, workCode, invId, invCode)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        if (mView != null) {
                            mView.getStorageNumSuccess(s);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.getStorageNumFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getProjectNumList(String workCode, String keyWord, int defaultItemNum, int flag, String bizType) {
        mView = getView();
        ResourceSubscriber<ArrayList<String>> subscriber =
                mRepository.getSupplierList(workCode, keyWord, defaultItemNum, flag)
                        .filter(list -> list != null && list.size() > 0)
                        .map(list -> convert2StrList(list))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<ArrayList<String>>() {
                            @Override
                            public void onNext(ArrayList<String> list) {
                                if (mView != null) {
                                    mView.showProjectNums(list);
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

    private ArrayList<String> convert2StrList(List<SimpleEntity> list) {
        ArrayList<String> strs = new ArrayList<>();
        for (SimpleEntity item : list) {
            strs.add(item.code + "_" + item.name);
        }
        return strs;
    }
}
