package com.richfit.sdk_wzrs.base_rsn_edit;

import com.richfit.common_lib.lib_base_sdk.base_edit.IBaseEditView;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.SimpleEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/3/2.
 */

public interface IRSNEditView extends IBaseEditView {

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
