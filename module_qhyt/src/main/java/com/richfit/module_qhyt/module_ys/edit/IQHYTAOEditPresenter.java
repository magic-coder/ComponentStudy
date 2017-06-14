package com.richfit.module_qhyt.module_ys.edit;


import com.richfit.common_lib.lib_base_sdk.base_edit.IBaseEditPresenter;

/**
 * Created by monday on 2017/3/1.
 */

public interface IQHYTAOEditPresenter extends IBaseEditPresenter<IQHYTAOEditView> {

    void getInvsByWorkId(String workId, int flag);

}
