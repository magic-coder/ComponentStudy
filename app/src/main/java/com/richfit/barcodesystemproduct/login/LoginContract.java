package com.richfit.barcodesystemproduct.login;


import com.richfit.common_lib.lib_mvp.BaseView;
import com.richfit.common_lib.lib_mvp.IPresenter;

import java.util.ArrayList;

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
    }

    interface Presenter extends IPresenter<View> {
        void login(String userName, String password);

        void readUserInfos();

        void uploadCrashLogFiles();
        void setupUrl(String url);

        void getMappingInfo();
    }
}
