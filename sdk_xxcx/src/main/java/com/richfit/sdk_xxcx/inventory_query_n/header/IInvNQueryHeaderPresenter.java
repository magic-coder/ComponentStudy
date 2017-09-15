package com.richfit.sdk_xxcx.inventory_query_n.header;


import com.richfit.common_lib.lib_mvp.IPresenter;

/**
 * Created by monday on 2017/5/25.
 */

public interface IInvNQueryHeaderPresenter extends IPresenter<IInvNQueryHeaderView> {

    /**
     * 获取发出工厂列表
     */
    void getWorks(int flag);

    /**
     * 通过工厂id获取该工厂下的接收库存地点列表
     * @param workId
     */
    void getInvsByWorkId(String workId, int flag);

    void getDictionaryData(String... codes);

}
