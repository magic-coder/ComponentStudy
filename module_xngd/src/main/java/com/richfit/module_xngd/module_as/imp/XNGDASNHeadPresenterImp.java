package com.richfit.module_xngd.module_as.imp;

import android.content.Context;

import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzrk.base_asn_head.imp.ASNHeadPresenterImp;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/6/23.
 */

public class XNGDASNHeadPresenterImp extends ASNHeadPresenterImp {

    public XNGDASNHeadPresenterImp(Context context) {
        super(context);
    }


    @Override
    public void getMoveTypeList(int flag) {
        mView = getView();
        Flowable.just(flag)
                .map(a -> {
                    ArrayList<String> moveTypes = new ArrayList<>();
                    moveTypes.add("Z91");
                    return moveTypes;
                }).compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<ArrayList<String>>() {
                    @Override
                    public void onNext(ArrayList<String> moveTypes) {
                        if (mView != null) {
                            mView.showMoveTypes(moveTypes);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.loadMoveTypesFail(t.getMessage());
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
