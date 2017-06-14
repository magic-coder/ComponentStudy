package com.richfit.common_lib.lib_adapter_rv.base;


/**
 * Created by monday on 16/6/22.
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

}
