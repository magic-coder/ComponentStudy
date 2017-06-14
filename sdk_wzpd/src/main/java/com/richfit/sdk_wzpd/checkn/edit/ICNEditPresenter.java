package com.richfit.sdk_wzpd.checkn.edit;


import com.richfit.common_lib.lib_base_sdk.base_edit.IBaseEditPresenter;
import com.richfit.domain.bean.ResultEntity;

/**
 * Created by monday on 2016/12/6.
 */

public interface ICNEditPresenter extends IBaseEditPresenter<ICNEditView> {
    /**
     * 保存本次采集的数据
     *
     * @param result:用户采集的数据(json格式)
     */
    void uploadCheckDataSingle(ResultEntity result);
}
