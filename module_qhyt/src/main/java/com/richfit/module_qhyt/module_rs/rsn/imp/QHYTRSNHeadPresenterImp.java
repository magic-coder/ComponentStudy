package com.richfit.module_qhyt.module_rs.rsn.imp;

import android.content.Context;

import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzrs.base_rsn_head.imp.RSNHeadPresenterImp;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/6/22.
 */

public class QHYTRSNHeadPresenterImp extends RSNHeadPresenterImp {

    public QHYTRSNHeadPresenterImp(Context context) {
        super(context);
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
