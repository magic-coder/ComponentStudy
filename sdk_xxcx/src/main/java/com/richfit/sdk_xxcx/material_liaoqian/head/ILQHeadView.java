package com.richfit.sdk_xxcx.material_liaoqian.head;

import com.richfit.common_lib.lib_mvp.BaseView;
import com.richfit.domain.bean.MaterialEntity;

/**
 * Created by monday on 2017/3/16.
 */

public interface ILQHeadView extends BaseView {
    void querySuccess(MaterialEntity entity);
    void queryFail(String message);
}
