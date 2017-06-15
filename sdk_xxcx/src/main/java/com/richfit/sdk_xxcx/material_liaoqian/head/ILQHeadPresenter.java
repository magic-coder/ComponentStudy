package com.richfit.sdk_xxcx.material_liaoqian.head;


import com.richfit.common_lib.lib_mvp.IPresenter;

/**
 * Created by monday on 2017/3/16.
 */

public interface ILQHeadPresenter extends IPresenter<ILQHeadView> {
    void getMaterialInfo(String queryType, String materialNum);
}
