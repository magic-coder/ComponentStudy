package com.richfit.barcodesystemproduct.welcome;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.richfit.barcodesystemproduct.home.HomeActivity;
import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.utils.LocalFileUtil;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.BizFragmentConfig;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;


/**
 * Created by monday on 2016/11/8.
 */

public class WelcomePresenterImp extends BasePresenter<WelcomeContract.View>
        implements WelcomeContract.Presenter {

    WelcomeContract.View mView;

    public WelcomePresenterImp(Context context) {
        super(context);
    }

    @Override
    public void loadFragmentConfig(String companyI, String configFileName) {
        mView = getView();
        if (TextUtils.isEmpty(configFileName) && mView != null) {
            mView.loadFragmentConfigFail("未找到合适Fragment也只文件名称");
            return;
        }

        if (TextUtils.isEmpty(Global.COMPANY_ID) && mView != null) {
            mView.loadFragmentConfigFail("未获取到公司id");
            return;
        }

//        boolean initedFragmentConfig = (boolean) SPrefUtil.getData(Global.IS_INITED_FRAGMENT_CONFIG_KEY,false);
//        if(initedFragmentConfig && mView != null) {
//            mView.loadFragmentConfigSuccess();
//            return;
//        }
        ResourceSubscriber<Boolean> subscriber =
                Flowable.just(configFileName)
                        .map(name -> LocalFileUtil.getStringFormAsset(mContext, name))
                        .map(json -> parseJson(json))
                        .filter(list -> list != null && list.size() > 0)
                        .flatMap(list -> mRepository.saveBizFragmentConfig(list))
                        .doOnComplete(() -> SPrefUtil.saveData(Global.IS_INITED_FRAGMENT_CONFIG_KEY, true))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<Boolean>() {
                            @Override
                            public void onNext(Boolean aBoolean) {

                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.loadFragmentConfigFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {
                                if (mView != null)
                                    mView.loadFragmentConfigSuccess();
                            }
                        });
        addSubscriber(subscriber);
    }

    private ArrayList<BizFragmentConfig> parseJson(final String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Gson gson = new Gson();
        ArrayList<BizFragmentConfig> list =
                gson.fromJson(json, new TypeToken<ArrayList<BizFragmentConfig>>() {
                }.getType());
        return list;
    }

    @Override
    public void toHome(int mode) {
        switch (mode) {
            case Global.ONLINE_MODE:
                mRepository.setLocal(false);
                break;
            case Global.OFFLINE_MODE:
                mRepository.setLocal(true);
                break;
        }
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        AppCompatActivity activity = (AppCompatActivity) mContext;
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
        activity.finish();
    }
}
