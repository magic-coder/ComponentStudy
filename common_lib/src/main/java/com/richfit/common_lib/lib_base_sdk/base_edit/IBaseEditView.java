package com.richfit.common_lib.lib_base_sdk.base_edit;


import com.richfit.common_lib.lib_mvp.BaseView;

/**
 * Created by monday on 2017/4/6.
 */

public interface IBaseEditView extends BaseView {
    void saveEditedDataSuccess(String message);
    void saveEditedDataFail(String message);
}
