package com.richfit.module_qhyt.module_ys.detail;


import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailView;

import java.util.List;

/**
 * Created by monday on 2017/2/28.
 */

public interface IQHYTAODetailView<T> extends IBaseDetailView<T> {
    /**
     * 显示明细。注意这里需要给出单据的抬头的TransId
     *
     * @param nodes
     */
    void showNodes(List<T> nodes, String transId);
}
