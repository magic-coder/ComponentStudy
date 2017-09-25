package com.richfit.module_mcq.module_dscx.detail;

import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailPresenter;
import com.richfit.module_mcq.module_ascx.detail.IASCXDetailView;

import java.util.Map;

/**
 * Created by monday on 2017/9/6.
 */

public interface IDSCXDetailPresenter extends IBaseDetailPresenter<IDSCSDetailView> {

    void getArrivalInfo(String createdBy,String creationDate,Map<String,Object> extraMap);
}

