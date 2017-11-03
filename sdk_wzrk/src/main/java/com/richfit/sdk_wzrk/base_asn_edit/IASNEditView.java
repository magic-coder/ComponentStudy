package com.richfit.sdk_wzrk.base_asn_edit;


import com.richfit.common_lib.lib_base_sdk.base_edit.IBaseEditView;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.SimpleEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2016/12/1.
 */

public interface IASNEditView extends IBaseEditView {
    /**
     * 输入物料获取缓存后，刷新界面
     *
     * @param refData
     * @param batchFlag
     */
    void onBindCommonUI(ReferenceEntity refData, String batchFlag);

    void loadTransferSingleInfoFail(String message);

    void loadTransferSingeInfoComplete();
}
