package com.richfit.barcodesystemproduct.login;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.barcodesystemproduct.BarcodeSystemApplication;
import com.richfit.common_lib.lib_crash.CrashLogUtil;
import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.common_lib.utils.L;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.data.net.http.RetrofitModule;
import com.richfit.data.repository.DataInjection;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.UpdateEntity;
import com.richfit.domain.bean.UserEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

/**
 * Created by monday on 2016/10/27.
 */

public class LoginPresenterImp extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    LoginContract.View mView;



    public LoginPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void login(final String userName, final String password) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            mView.loginFail("用户名或者密码为空");
            return;
        }
        ResourceSubscriber<UserEntity> subscriber =
                mRepository.login(userName, password)
                        .doOnNext(userEntity -> mRepository.saveUserInfo(userEntity))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<UserEntity>(mContext, "正在登陆...") {
                            @Override
                            public void _onNext(UserEntity userInfo) {
                                Global.LOGIN_ID = userInfo.loginId;
                                Global.USER_ID = userInfo.userId;
                                Global.USER_NAME = userInfo.userName;
                                Global.COMPANY_ID = userInfo.companyId;
                                Global.COMPANY_CODE = userInfo.companyCode;
                                Global.AUTH_ORG = userInfo.authOrgs;
                                Global.BATCHMANAGERSTATUS = userInfo.batchFlag;
                                Global.WMFLAG = userInfo.wmFlag;
                            }

                            @Override
                            public void _onNetWorkConnectError(String message) {
                                if (mView != null) {
                                    mView.networkConnectError(Global.RETRY_LOGIN_ACTION);
                                }
                            }

                            @Override
                            public void _onCommonError(String message) {
                                if (mView != null) {
                                    mView.loginFail(message);
                                }
                            }

                            @Override
                            public void _onServerError(String code, String msg) {
                                if (mView != null) {
                                    mView.loginFail(msg);
                                }
                            }

                            @Override
                            public void _onComplete() {
                                if (mView != null) {
                                    mView.toHome();
                                }
                            }
                        });

        addSubscriber(subscriber);
    }

    @Override
    public void readUserInfos() {
        mView = getView();
        mRepository.readUserInfo("", "")
                .filter(list -> list != null && list.size() > 0)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<ArrayList<String>>() {
                    @Override
                    public void onNext(ArrayList<String> list) {
                        if (mView != null) {
                            mView.showUserInfos(list);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.loadUserInfosFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void uploadCrashLogFiles() {
        mView = getView();
        final File crashLogFileDir = CrashLogUtil.getCrashLogFileDir(mContext.getApplicationContext());
        ResourceSubscriber<String> subscriber = Flowable.just(crashLogFileDir)
                .map((Function<File, List<ResultEntity>>) dir -> {
                    final ArrayList<ResultEntity> results = new ArrayList<>();
                    if (dir == null) {
                        return results;
                    }

                    File[] files = dir.listFiles(crashFile -> crashFile != null && crashFile.getName().endsWith(".txt"));

                    if (files == null || files.length == 0) {
                        return results;
                    }
                    for (File file : files) {
                        if (file == null || file.length() == 0)
                            continue;
                        ResultEntity result = new ResultEntity();
                        result.imagePath = file.getAbsolutePath();
                        result.transFileToServer = "DEBUG";
                        results.add(result);
                    }
                    return results;
                })
                .filter(res -> res != null && res.size() > 0)
                .flatMap(res -> mRepository.uploadMultiFiles(res))
                .doOnComplete(() -> CrashLogUtil.deleteCashLogDir(crashLogFileDir))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        L.e("s = " + s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        L.e("error = " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        L.e("奔溃日志上传完毕");
                    }
                });
        addSubscriber(subscriber);
    }


    @Override
    public void getMappingInfo() {
        mView = getView();
        ResourceSubscriber<String> subscriber =
                mRepository.getMappingInfo()
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<String>(mContext, "正在检查是否已经注册...") {
                            @Override
                            public void _onNext(String s) {
//                                if(mView != null) {
//                                    mView.updateDbSource(s);
//                                }
                            }

                            @Override
                            public void _onNetWorkConnectError(String message) {
                                if (mView != null) {
                                    mView.networkConnectError(Global.RETRY_REGISTER_ACTION);
                                }
                            }

                            @Override
                            public void _onCommonError(String message) {
                                if (mView != null) {
                                    mView.unRegister(message);
                                }
                            }

                            @Override
                            public void _onServerError(String code, String message) {
                                if (mView != null) {
                                    mView.unRegister(message);
                                }
                            }

                            @Override
                            public void _onComplete() {
                                if (mView != null) {
                                    mView.registered();
                                }
                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getAppVersion() {
        mView = getView();
        mRepository.getAppVersion()
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<UpdateEntity>(mContext, "正在检查更新...") {
                    @Override
                    public void _onNext(UpdateEntity updateEntity) {
                        if (mView != null) {
                            mView.checkAppVersion(updateEntity);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {

                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.getUpdateInfoFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.getUpdateInfoFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {

                    }
                });
    }

    @Override
    public void setupUrl(String url) {
        mView = getView();

        ResourceSubscriber<String> subscriber = Flowable.just(url)
                .map(baseUrl -> {
                    RetrofitModule.setRequestApi(null);
                    DataInjection.setRepository(null);
                    BarcodeSystemApplication.baseUrl = baseUrl;
                    DataInjection.getRepository(BarcodeSystemApplication.getAppContext(), baseUrl);
                    SPrefUtil.saveData("base_url", baseUrl);
                    SPrefUtil.saveData(Global.IS_APP_FIRST_KEY, true);
                    SPrefUtil.saveData(Global.IS_INITED_FRAGMENT_CONFIG_KEY, false);
                    return baseUrl;
                })
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<String>() {
                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) {
                            mView.setupUrlComplete();
                        }
                    }
                });
        addSubscriber(subscriber);

    }



}
