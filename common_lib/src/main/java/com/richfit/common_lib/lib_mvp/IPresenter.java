package com.richfit.common_lib.lib_mvp;

/**
 * mvp的presenter层
 * Created by monday on 2016/9/30.
 */

public interface IPresenter<T extends IView> {
    /**
     * 绑定View层
     * @param view
     */
    void attachView(T view);

    /**
     * 解绑View层
     */
    void detachView();

    /**
     * 设置数据源
     * @param isLocal
     */
    void setLocal(boolean isLocal);
    boolean isLocal();
}
