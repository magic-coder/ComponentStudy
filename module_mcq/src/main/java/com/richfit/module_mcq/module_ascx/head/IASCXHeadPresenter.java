package com.richfit.module_mcq.module_ascx.head;

import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadPresenter;

import java.util.Map;

/**
 * Created by monday on 2017/8/28.
 */

public interface IASCXHeadPresenter extends IBaseHeadPresenter<IASCXHeadView> {

    void getArrivalInfo(String createdBy,String creationDate,Map<String,Object> extraMap);
}
