package com.richfit.module_mcq.module_ascx.detail;

import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailPresenter;

/**
 * Created by monday on 2017/8/29.
 */

public interface IASCXDetailPresenter extends IBaseDetailPresenter<IASCXDetailView>{


    void getReference(String refNum, String refType, String bizType,
                      String moveType, String refLineId, String userId);
}
