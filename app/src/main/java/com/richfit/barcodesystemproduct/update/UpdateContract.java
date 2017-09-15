package com.richfit.barcodesystemproduct.update;

import com.richfit.common_lib.lib_mvp.BaseView;
import com.richfit.common_lib.lib_mvp.IPresenter;

import zlc.season.rxdownload2.entity.DownloadStatus;

/**
 * Created by monday on 2017/9/5.
 */

public interface UpdateContract {

    interface View extends BaseView {
        //下载最新的app
        void loadLatestAppFail(String message);
        void showLoadAppProgress(DownloadStatus status);
        void loadAppComplete();

    }

    interface Presenter extends IPresenter<View> {
        /**
         * 下载最新的app
         * @param url：下载网址
         * @param saveName：保存的名字
         * @param savePath：保存的路径
         */
        void loadLatestApp(String url, String saveName, String savePath);
    }
}
