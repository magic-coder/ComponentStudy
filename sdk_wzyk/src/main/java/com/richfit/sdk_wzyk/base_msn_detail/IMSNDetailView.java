package com.richfit.sdk_wzyk.base_msn_detail;


import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailView;

/**
 * Created by monday on 2016/11/20.
 */

public interface IMSNDetailView<T> extends IBaseDetailView<T> {
    /**
     * 寄售转自有成功
     */
    void turnOwnSuppliesSuccess();

    /**
     * 寄售转自有失败
     * @param message
     */
    void turnOwnSuppliesFail(String message);
}
