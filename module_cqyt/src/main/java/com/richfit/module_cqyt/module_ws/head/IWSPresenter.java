package com.richfit.module_cqyt.module_ws.head;

import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadPresenter;

/**
 * Created by monday on 2017/9/21.
 */

public interface IWSPresenter extends IBaseHeadPresenter<IWSHeadView> {
    /**
     * 获取发出工厂列表
     */
    void getWorks(int flag);
}
