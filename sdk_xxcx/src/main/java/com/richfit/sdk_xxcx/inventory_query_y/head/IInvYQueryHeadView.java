package com.richfit.sdk_xxcx.inventory_query_y.head;

import com.richfit.common_lib.lib_mvp.BaseView;
import com.richfit.domain.bean.ReferenceEntity;

/**
 * Created by monday on 2017/5/25.
 */

public interface IInvYQueryHeadView extends BaseView {

    /**
     * 读取单据数据
     *
     * @param refData:单据数据
     */
    void getReferenceSuccess(ReferenceEntity refData);

    /**
     * 读取单据数据失败
     *
     * @param message
     */
    void getReferenceFail(String message);

    void getReferenceComplete();

    /**
     * 请求单据数据，清除抬头必要的控件信息
     */
    void clearAllUI();
}
