package com.richfit.common_lib.lib_base_sdk.base_collect;

import com.richfit.common_lib.lib_mvp.BaseView;
import com.richfit.domain.bean.ResultEntity;

/**
 * Created by monday on 2017/7/26.
 */

public interface IBaseCollectView extends BaseView {
    /**
     * 提供单条数据保存
     * @return
     */
    ResultEntity provideResult();

    /**
     * 单条数据保存成功
     * @param message
     */
    void saveCollectedDataSuccess(String message);

    /**
     * 单条数据保存失败
     * @param message
     */
    void saveCollectedDataFail(String message);
}
