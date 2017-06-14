package com.richfit.common_lib.lib_base_sdk.base_head;

import com.richfit.common_lib.lib_mvp.IPresenter;
import com.richfit.domain.bean.ResultEntity;

/**
 * 所有抬头界面的Presenter层接口
 * Created by monday on 2017/3/18.
 */

public interface IBaseHeadPresenter<V extends IBaseHeadView> extends IPresenter<V> {

    /**
     * 保存离线的抬头数据
     */
    void uploadEditedHeadData(ResultEntity resultEntity);
}
