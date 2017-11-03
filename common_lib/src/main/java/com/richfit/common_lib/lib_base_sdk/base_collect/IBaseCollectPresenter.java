package com.richfit.common_lib.lib_base_sdk.base_collect;

import com.richfit.common_lib.lib_mvp.IPresenter;
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;

/**
 * Created by monday on 2017/7/26.
 */

public interface IBaseCollectPresenter<V extends IBaseCollectView> extends IPresenter<V> {
    /**
     * 保存本次采集的数据
     *
     * @param result:用户采集的数据(json格式)
     */
    void uploadCollectionDataSingle(ResultEntity result);
    /**
     * 保存本次盘点的数量
     * @param result
     */
    void uploadCheckDataSingle(ResultEntity result);

    /**
     * 有参考的管理批次状态
     * @param data
     * @param workId
     * @return
     */
    MaterialEntity addBatchManagerStatus(MaterialEntity data, String workId);

    /**
     * 无参考的批次管理
     * @param refData
     * @param workId
     * @return
     */
    ReferenceEntity addBatchManagerStatus(ReferenceEntity refData, String workId);

    /**
     * 获取字典数据，最常用的是获取仓储类型
     * @param codes:对应数据的类型，由Global里面定义的字符常量确定
     */
    void getDictionaryData(String... codes);
}
