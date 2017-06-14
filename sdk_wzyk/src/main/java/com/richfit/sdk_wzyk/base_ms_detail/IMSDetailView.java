package com.richfit.sdk_wzyk.base_ms_detail;


import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailView;

/**
 * Created by monday on 2017/2/10.
 */

public interface IMSDetailView<T> extends IBaseDetailView<T> {
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
