package com.richfit.common_lib.lib_base_sdk.base_edit;


import com.richfit.common_lib.lib_mvp.BaseView;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/4/6.
 */

public interface IBaseEditView extends BaseView {
    /**
     * 提供单条数据保存
     * @return
     */
    ResultEntity provideResult();

    /**
     * 修改数据成功
     * @param message
     */
    void saveEditedDataSuccess(String message);

    /**
     * 修改数据失败
     * @param message
     */
    void saveEditedDataFail(String message);

    /**
     * 获取字典成功
     * @param data
     */
    void loadDictionaryDataSuccess(Map<String,List<SimpleEntity>> data);
    void loadDictionaryDataFail(String message);
}
