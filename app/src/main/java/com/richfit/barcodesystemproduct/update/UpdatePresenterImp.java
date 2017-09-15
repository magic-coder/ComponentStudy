package com.richfit.barcodesystemproduct.update;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.barcodesystemproduct.BarcodeSystemApplication;
import com.richfit.common_lib.lib_mvp.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

/**
 * Created by monday on 2017/9/5.
 */

public class UpdatePresenterImp extends BasePresenter<UpdateContract.View> implements
        UpdateContract.Presenter {

    UpdateContract.View mView;

    RxDownload mRxDownload;
    Disposable mUpdateDisposable;

    @Override
    protected void onStart() {
        mView = getView();
        mRxDownload = RxDownload.getInstance(mContext)
                .maxThread(1)
                .maxRetryCount(3);
    }


    public UpdatePresenterImp(Context context) {
        super(context);
    }


    @Override
    public void loadLatestApp(String url, String saveName, String savePath) {
        mView = getView();
        if (TextUtils.isEmpty(url) && mView != null) {
            mView.loadLatestAppFail("下载地址为空");
            return;
        }

        if (TextUtils.isEmpty(saveName) && mView != null) {
            mView.loadLatestAppFail("未获取到保存文件的名称");
            return;
        }

        //2017年06月修改下载的url，获取到服务器的下载url后需要动态的将其ip修改系统设置的url
        //获取http://后面第一次出现/字符的位置
        int indexOf = url.indexOf("/", 7);
        String currentUrl = BarcodeSystemApplication.baseUrl;
        int pos = currentUrl.indexOf("/", 7);
        if (indexOf < 0 || pos < 0) {
            mView.loadLatestAppFail("服务器地址有误!");
            return;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(currentUrl.substring(0, pos))
                .append(url.substring(indexOf));

        mRxDownload.download(sb.toString(), saveName, savePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceObserver<DownloadStatus>() {

                    @Override
                    public void onNext(DownloadStatus status) {
                        if (mView != null) {
                            mView.showLoadAppProgress(status);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) {
                            mView.loadLatestAppFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) {
                            mView.loadAppComplete();
                        }
                    }
                });
    }


    @Override
    public void detachView() {
        super.detachView();
        if (mUpdateDisposable != null && !mUpdateDisposable.isDisposed()) {
            mUpdateDisposable.dispose();
        }
    }

}
