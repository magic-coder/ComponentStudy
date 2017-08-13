package com.richfit.sdk_wzrk.base_as_head;


import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadView;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.SimpleEntity;

import java.util.List;

/**
 * Created by monday on 2016/11/11.
 */

public interface IASHeadView extends IBaseHeadView {

    /**
     * 删除缓存成功
     */
    void deleteCacheSuccess();

    /**
     * 删除缓存失败
     *
     * @param message
     */
    void deleteCacheFail(String message);

    /**
     * 为公共控件绑定数据
     */
    void bindCommonHeaderUI();

    /**
     * 读取单据数据
     *
     * @param refData:单据数据
     */
    void getReferenceSuccess(ReferenceEntity refData);

    /**
     * 读取单据数据失败
     *
     * @param message
     */
    void getReferenceFail(String message);

    /**
     * 整单缓存处理
     *
     * @param cacheFlag：缓存标志
     * @param transId：缓存id,用于删除缓存
     * @param refNum：单据号
     * @param bizType：业务类型
     */
    void cacheProcessor(String cacheFlag, String transId, String refNum, String refCodeId, String refType, String bizType);

    /**
     * 请求单据数据，清除抬头必要的控件信息
     */
    void clearAllUI();

    /**
     * 获取抬头缓存失败
     * @param message
     */
    void getTransferInfoFail(String message);

    void loadInvsFail(String message);
    void showInvs(List<InvEntity> list);
    void loadInvsComplete();

    void loadDictionaryDataSuccess(List<SimpleEntity> data);
    void loadDictionaryDataFail(String message);


}
