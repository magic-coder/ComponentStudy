package com.richfit.common_lib.lib_interface;


import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;

/**
 * Created by monday on 2017/3/17.
 */

public interface IAdapterState {
    /**
     * 具体业务修改父节点抬头和子节点抬头的字段
     *
     * @param holder
     * @param viewType
     */
    void onBindViewHolder(ViewHolder holder, int viewType);
}
