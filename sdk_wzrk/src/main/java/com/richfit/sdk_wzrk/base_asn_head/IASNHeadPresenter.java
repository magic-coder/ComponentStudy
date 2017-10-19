package com.richfit.sdk_wzrk.base_asn_head;


import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadPresenter;

import java.util.Map;

/**
 * Created by monday on 2016/11/16.
 */

public interface IASNHeadPresenter extends IBaseHeadPresenter<IASNHeadView> {

    /**
     * 初始化工厂列表
     */
    void getWorks(int flag);

    /**
     * 初始化供应商列表
     *
     * @param workCode:工厂编码
     */
    void getAutoComList(String workCode, Map<String,Object> extraMap,String keyWord, int defaultItemNum,
                        int flag,String key);

    /**
     * 获取移动类型列表
     */
    void getMoveTypeList(int flag);

    /**
     * 删除整单缓存
     * @param bizType：业务类型
     * @param userId:用户id
     */
    void deleteCollectionData(String refType, String bizType, String userId, String companyCode);
}
