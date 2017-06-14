package com.richfit.common_lib.lib_base_sdk.edit;

import com.richfit.common_lib.lib_mvp.BaseView;
import com.richfit.common_lib.lib_mvp.IPresenter;
import com.richfit.domain.bean.BizFragmentConfig;

/**
 * Created by monday on 2017/1/13.
 */

public interface IEditContract {
    interface View extends BaseView {
        void showEditFragment(BizFragmentConfig fragmentConfig);

        void initEditFragmentFail(String message);
    }

    interface Presenter extends IPresenter<View> {
        void setupEditFragment(String bizType, String refType, int fragmentType);
    }
}
