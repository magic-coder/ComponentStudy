package com.richfit.module_mcq.module_ascx.detail;

import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailPresenter;

import java.util.Map;

/**
 * Created by monday on 2017/8/29.
 */

public interface IASCXDetailPresenter extends IBaseDetailPresenter<IASCXDetailView>{


    void getArrivalInfo(String createdBy,String creationDate,Map<String,Object> extraMap);
}
