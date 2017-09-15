package com.richfit.barcodesystemproduct.login;


import com.richfit.common_lib.lib_mvp.BaseView;
import com.richfit.common_lib.lib_mvp.IPresenter;
import com.richfit.domain.bean.UpdateEntity;

import java.util.ArrayList;

import zlc.season.rxdownload2.entity.DownloadStatus;

/**
 * Created by monday on 2016/10/27.
 */

public interface LoginContract {

    interface View extends BaseView {
        //跳转到home界面
        void toHome();
        void loginFail(String message);
        void showUserInfos(ArrayList<String> list);
        void loadUserInfosFail(String message);
        void setupUrlComplete();

        void registered();
        void unRegister(String message);


        /**
         * 检查是否需要更新
         * @param info：服务器返回的更新信息
         */
        void checkAppVersion(UpdateEntity info);
        void getUpdateInfoFail(String message);


    }

    interface Presenter extends IPresenter<View> {
        void login(String userName, String password);

        void readUserInfos();

        void uploadCrashLogFiles();
        void setupUrl(String url);

        void getMappingInfo();

        /**
         * 获取最新版本的信息
         */
        void getAppVersion();


    }
}
