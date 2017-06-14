package com.richfit.barcodesystemproduct.splash;


import com.richfit.common_lib.lib_mvp.BaseView;

/**
 * Created by monday on 2016/12/2.
 */

public interface ISplashView extends BaseView {

    void networkAvailable();
    void networkNotAvailable(String message);

    void toLogin();
    void syncDataError(String message);

    void downDBComplete();
    void downDBFail(String message);

}
