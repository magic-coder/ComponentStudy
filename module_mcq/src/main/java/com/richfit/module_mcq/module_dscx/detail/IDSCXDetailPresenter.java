package com.richfit.module_mcq.module_dscx.detail;

import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailPresenter;
import com.richfit.module_mcq.module_ascx.detail.IASCXDetailView;

/**
 * Created by monday on 2017/9/6.
 */

public interface IDSCXDetailPresenter extends IBaseDetailPresenter<IDSCSDetailView> {


    void getReference(String refNum, String refType, String bizType,
                      String moveType, String refLineId, String userId);
}

