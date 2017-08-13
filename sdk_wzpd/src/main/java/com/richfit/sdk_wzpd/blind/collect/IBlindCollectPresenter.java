package com.richfit.sdk_wzpd.blind.collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectPresenter;
import com.richfit.domain.bean.ResultEntity;

/**
 * Created by monday on 2017/3/3.
 */

public interface IBlindCollectPresenter extends IBaseCollectPresenter<IBlindCollectView> {
    /**
     * 获取单条盘点缓存
     *
     * @param checkId
     * @param queryType
     * @param materialNum
     */
    void getCheckTransferInfoSingle(String checkId, String location, String queryType,
                                    String materialNum, String bizType);


}
